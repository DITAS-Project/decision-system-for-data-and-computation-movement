/**
 * Copyright 2018 Politecnico di Milano
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * This is being developed for the DITAS Project: https://www.ditas-project.eu/
 */
package it.polimi.deib.ds4m.main.functionalities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;

import com.ditas.ehealth.GetDataSourceMetricsReply;
import com.ditas.ehealth.GetDataSourceMetricsRequest;
import com.ditas.ehealth.MetricsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.configuration.PathSetting;
import it.polimi.deib.ds4m.main.evaluation.DAL_evaluation;
import it.polimi.deib.ds4m.main.evaluation.Infrastructure_evaluation;
import it.polimi.deib.ds4m.main.evaluation.ResourceManager;
import it.polimi.deib.ds4m.main.evaluation.VDC_evaluation;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.TreeStructure;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.da.ResultValueDA;
import it.polimi.deib.ds4m.main.model.dataSources.DAL;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.movementEnaction.MovementEnaction;
import it.polimi.deib.ds4m.main.movement.GoalTreeManager;
import it.polimi.deib.ds4m.main.movement.MovementsActionsManager;
import it.polimi.deib.ds4m.main.movement.VDCManager;
import wiremock.com.jayway.jsonpath.PathNotFoundException;

/**
 * Servlet implementation class NotifyViolation
 */
@WebServlet("/v2/NotifyViolation")
public class NotifyViolation_functionality {
	private static final long serialVersionUID = 1L;

	public static void doPost(ArrayList<VDC_evaluation> VDCs, ArrayList<Violation> violations) 
	{
		// create the json parser
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);// to serialize arrays with only one
																					// element
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);


		try {

			// while the set of violations contain a violation, keep analysing them
			for (Violation violation : violations) {
				// identify VDC
				
				//i can cast since the object passed in input are VDC_evaluation
				VDC_evaluation violatedVDC = (VDC_evaluation) VDCManager.findViolatedVDC(violation,  Utility.cast(VDCs));
				
				
				if (violatedVDC == null) {
					String message = "NotifyViolation: No violated VDC found";
					System.err.println(message);
					return;
				}

				// identify goal
				Set<TreeStructure> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
				if (violatedGoals == null) {
					String message = "NotifyViolation: No violated goals found";
					System.err.println(message);
					return;
				}

				// identify movement actions with positive effect on goal
				ArrayList<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals,
						violatedVDC);
				if (movementsToBeEnacted == null || movementsToBeEnacted.size() == 0) {
					String message = "NotifyViolation: No movements to be enacted found";
					System.err.println(message);
					return;
				}

				// order the dm action using a strategy
				MovementsActionsManager.orderMovementAction(movementsToBeEnacted, "MONETARY");

				// check other trees of other VDCs, for all method?
				movementsToBeEnacted = VDCManager.chechOtherVDC(movementsToBeEnacted, Utility.cast(VDCs), violatedVDC);
				if (movementsToBeEnacted == null) {
					String message = "NotifyViolation: all movements to be enacted have been removed";
					System.err.println(message);
					return;
				}

				if (movementsToBeEnacted.size() == 0) {
					String message = "NotifyViolation: No movement actions to be enacted";
					System.err.println(message);
					return;
				}

				// select first data movement action
				Movement movement = movementsToBeEnacted.get(0);

				// transform the selected movement actions in element to be sent
				MovementEnaction movementEnaction = new MovementEnaction();
				movementEnaction.importMovement(movement, violatedVDC);

				// once the movement action has been selected,
				// 1-check the amount of space that is used by the sourse DAL

				// String dalResourceJSON=null;

				// i perform the call only if it is a data movement or data duplication
				if (movement.getType().equalsIgnoreCase("DataDuplication")
						|| movement.getType().equalsIgnoreCase("DataMovement")
						|| movement.getType().equalsIgnoreCase("dataMovementComputationMovement")
						|| movement.getType().equalsIgnoreCase("dataDuplicationComputationMovement")) {

					//TODO: check space

				}


				// if it's a computation movement
				if (movement.getType().equalsIgnoreCase("ComputationMovement")) 
				{
					violatedVDC.setCurrentInfrastructure(movement.getToLinked());

					//call computation movement
					ResourceManager.moveVDC((VDC_evaluation) violatedVDC, (Infrastructure_evaluation) movement.getToLinked());

				} else if (movement.getType().equalsIgnoreCase("ComputationDuplication")) {
					// add a new vdc, with a different name?
					// deep copy VDC
					// load the blueprint, load the classes of movement actions
					// instantiate it calls the VDCmanager.cretaeVDC()
					// change the current infrastructure with the one in the movement
					// add the new VDC to the list of blueprints
					
					ResourceManager.duplicateVDC((VDC_evaluation) violatedVDC, (Infrastructure_evaluation) movement.getToLinked());
				}

				// if it's a data movement i update the existing dal
				// if it's a data duplication i add the dal to the dal of the vdc
				else if (movement.getType().equalsIgnoreCase("DataMovement")
						|| movement.getType().equalsIgnoreCase("dataMovementComputationMovement")) {
					// call DME
					
					ResourceManager.moveDAL((DAL_evaluation) movement.getDalToMove(), (Infrastructure_evaluation) movement.getToLinked());

					// if there is a movement to perform after movement, I set it
					if (movement.getType().equalsIgnoreCase("dataMovementComputationMovement")) {
						violatedVDC.setNextMovement(movement.getNextMovement());
					}

				} else if (movement.getType().equalsIgnoreCase("DataDuplication")
						|| movement.getType().equalsIgnoreCase("dataDuplicationComputationMovement")) {

					// set new id DAL
					String newDALID = UUID.randomUUID().toString();

					movementEnaction.setDALid(newDALID);

					// call DME
					ResourceManager.duplicateDAL((DAL_evaluation) movement.getDalToMove(), (Infrastructure_evaluation) movement.getToLinked());

					// answer DME ignored

					// ***update VDC with new DAL
					// if there is a movement to perform after movement, I set it
					if (movement.getType().equalsIgnoreCase("dataDuplicationComputationMovement")) {
						violatedVDC.setNextMovement(movement.getNextMovement());
					}

					// create DAL
					DAL duplicatedDAL = new DAL();
					duplicatedDAL.setPosition(movement.getToLinked());
					duplicatedDAL.setDataSources(movement.getDalToMove().getDataSources());

					// create the unique identifier for the new DAL
					duplicatedDAL.setId(newDALID);

					// add dal to vdc
					violatedVDC.getDALs().add(duplicatedDAL);

					// update data movements (a new DAL means new data movements)
					String concreteBlueprintJSON = null;
					try {
						// the function needs the name of the file
						concreteBlueprintJSON = VDCManager.loadConcreteBlueprint(violatedVDC.getId() + ".json");
					} catch (PathNotFoundException pnfE) {
						System.err.println("NofifyViolation: " + pnfE.getMessage());
						System.err.println("NofifyViolation: skip update VDC");
					} catch (Exception e) {
						System.err.println("NotifyViolation: " + e.getMessage());
						return;
					}

					// i skip the update if i failed to load the movement class.
					// this control is needed because of the PathNotFoundException: i still continue
					// even if the share volume is not accessible
					if (concreteBlueprintJSON != null) {
						// check if the persistent volume folder exists. if not, it is not mounted (it
						// is a junit test execution) and skip the save
						String movementsJSON;
						try {
							movementsJSON = MovementsActionsManager.loadMovementClass(null);
						} catch (Exception e) {
							System.err.println("NotifyViolation: " + e.getMessage());
							System.err.println("NotifyViolation: skip update VDC");
							return;
						}

						// update the VDC
						try {
							VDCManager.updateVDCmovements(concreteBlueprintJSON, movementsJSON, violatedVDC);
						} catch (Exception e) {
							System.err.println("NotifyViolation: " + e.getMessage());
						}

						System.out.println("VDC updated");
					}
				}

			}

			// set answer status to trigger of violations

		} catch (Exception e) {
			String message = "NotifyViolation: violations not well-formed";
			e.printStackTrace();
			System.err.println(message);
		}
	}

}
