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
package it.polimi.deib.ds4m.main.api;

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


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import it.polimi.deib.ds4m.main.configuration.PathSetting;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.TreeStructure;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
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
public class NotifyViolation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NotifyViolation() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().println("<h1>notify violation, DS4M is up<h1>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// @SuppressWarnings("unchecked")
	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// create the json parser
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);// to serialize arrays with only one
																					// element
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

		// retrieve the list of VDC
		@SuppressWarnings("unchecked")
		ArrayList<VDC> VDCs = (ArrayList<VDC>) this.getServletConfig().getServletContext().getAttribute("VDCs");

		// retrieve parameter (the list of violations)
		String violationsJSON = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

		try {
			// convert
			ArrayList<Violation> violations = new ArrayList<Violation>(
					Arrays.asList(mapper.readValue(violationsJSON, Violation[].class)));

			// while the set of violations contain a violation, keep analysing them
			for (Violation violation : violations) {
				// identify VDC
				VDC violatedVDC = VDCManager.findViolatedVDC(violation, VDCs);
				if (violatedVDC == null) {
					String message = "NotifyViolation: No violated VDC found";
					System.err.println(message);
					response.getWriter().println(message);

					response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					return;
				}

				// identify goal
				Set<TreeStructure> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
				if (violatedGoals == null) {
					String message = "NotifyViolation: No violated goals found";
					System.err.println(message);
					response.getWriter().println(message);

					response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					return;
				}

				// identify movement actions with positive effect on goal
				ArrayList<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals,
						violatedVDC);
				if (movementsToBeEnacted == null || movementsToBeEnacted.size() == 0) {
					String message = "NotifyViolation: No movements to be enacted found";
					System.err.println(message);
					response.getWriter().println(message);

					response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					return;
				}

				// order the dm action using a strategy
				MovementsActionsManager.orderMovementAction(movementsToBeEnacted, "MONETARY");

				// check other trees of other VDCs, for all method?
				movementsToBeEnacted = VDCManager.chechOtherVDC(movementsToBeEnacted, VDCs, violatedVDC);
				if (movementsToBeEnacted == null) {
					String message = "NotifyViolation: all movements to be enacted have been removed";
					System.err.println(message);
					response.getWriter().println(message);

					response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					response.setContentType("application/json");
					return;
				}

				if (movementsToBeEnacted.size() == 0) {
					String message = "NotifyViolation: No movement actions to be enacted";
					System.err.println(message);
					response.getWriter().println(message);

					response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					response.setContentType("application/json");
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

					System.out.println("skip query DAL");
					// //if empty is it a test i skip connection with DAL
					// if (movement.getDalToMove().getOriginal_ip()!=null &&
					// (!movement.getDalToMove().getOriginal_ip().equals("")) )
					// {
					// try {
					// //ManagedChannel channel =
					// ManagedChannelBuilder.forAddress(PathSetting.urlDAL,
					// 50054).usePlaintext().build();
					// ManagedChannel channel =
					// ManagedChannelBuilder.forAddress(movement.getDalToMove().getOriginal_ip(),
					// 50054).usePlaintext().build();
					// MetricsServiceGrpc.MetricsServiceBlockingStub stub =
					// MetricsServiceGrpc.newBlockingStub(channel);
					// GetDataSourceMetricsReply responseDAL =
					// stub.getDataSourceMetrics(GetDataSourceMetricsRequest.newBuilder().build());
					// dalResourceJSON = responseDAL.getMetrics();
					//
					// channel.shutdown();
					// }
					// catch (Exception e)
					// {
					// String message = "NotifyViolation: DAL not reached";
					// System.err.println(message);
					// response.getWriter().println(message);
					//
					// response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					// response.setContentType("application/json");
					// return;
					// }
					//
					// }
					// else
					// {
					// System.out.println("NotifyViolation: URL dal empty, skipped connection");
					// }

					// 2-check if the target node/infrastructure has enough space

					System.out.println("Performing query AL");
					// call to data analytics
					HttpClient client = HttpClientBuilder.create().build();
					String callDA = PathSetting.urlDA_Resources + violatedVDC.getId() + "/"
							+ movement.getToLinked().getName() + "/";
					System.out.println("\nSending 'GET' request to URL : " + callDA);

					HttpGet requestDA = new HttpGet(callDA);

					HttpResponse responseDA;

					try {						
						responseDA = client.execute(requestDA);

						System.out.println("\nSending 'GET' request to URL : " + callDA);
						System.out.println("Response Code : " + responseDA.getStatusLine().getStatusCode());

						BufferedReader rd = new BufferedReader(
								new InputStreamReader(responseDA.getEntity().getContent()));

						StringBuffer result = new StringBuffer();
						String line = "";
						while ((line = rd.readLine()) != null) {
							result.append(line);
						}

						//System.out.println(result.toString());
						
						//String test = "{\"cpu\": 4000, \"mem\": 4096, \"storage\": 150}";
						//ResultValueDA resultValueDA= mapper.readValue(result.toString(), ResultValueDA.class);
						
						

					} catch (IOException e) {
						System.out.println("Data Analitics not reached or bad input");
					}

				}

				// end call data analytics

				// if it's a computation movement
				if (movement.getType().equalsIgnoreCase("ComputationMovement")) {
					violatedVDC.setCurrentInfrastructure(movement.getToLinked());

					String call = PathSetting.urlComputationMovementEnactor + "/vdc/" + violatedVDC.getId()
							+ "?sourceInfra=" + movement.getFromLinked().getId() + "&targetInfra="
							+ movement.getToLinked().getId();

					System.out.println("Nofifyviolation: call CME: " + call);

					// call to computation movement enactors
					HttpClient client = HttpClientBuilder.create().build();

					// call CME
					HttpPut httpPut = new HttpPut(call);

					try {
						@SuppressWarnings("unused")
						HttpResponse responseDE = client.execute(httpPut);// response empty
					} catch (IOException e) {
						System.out.println("Computation Movement Enactor not reached");// for test purposes don't stop
																						// execution
					}

				} else if (movement.getType().equalsIgnoreCase("ComputationDuplication")) {
					// add a new vdc, with a different name?
					// deep copy VDC
					// load the blueprint, load the classes of movement actions
					// instantiate it calls the VDCmanager.cretaeVDC()
					// change the current infrastructure with the one in the movement
					// add the new VDC to the list of blueprints

					String errMessage = "NotifyViolation: ComputationDuplication not supported currently";
					System.err.println(errMessage);
					response.getWriter().println(errMessage);

					response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					return;
				}

				// if it's a data movement i update the existing dal
				// if it's a data duplication i add the dal to the dal of the vdc
				else if (movement.getType().equalsIgnoreCase("DataMovement")
						|| movement.getType().equalsIgnoreCase("dataMovementComputationMovement")) {
					// call DME
					MovementsActionsManager.DMECall(movementEnaction);

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
					MovementsActionsManager.DMECall(movementEnaction);

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
						concreteBlueprintJSON = VDCManager.loadConcreteBlueprint(PathSetting.blueprints_pv + "/"+violatedVDC.getId() + ".json");
					} catch (PathNotFoundException pnfE) {
						System.err.println("NofifyViolation: " + pnfE.getMessage());
						System.err.println("NofifyViolation: skip update VDC");
					} catch (Exception e) {
						System.err.println("NotifyViolation: " + e.getMessage());
						response.getWriter().println("NotifyViolation: " + e.getMessage());

						response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
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
							movementsJSON = MovementsActionsManager.loadMovementClass(this);
						} catch (Exception e) {
							System.err.println("NotifyViolation: " + e.getMessage());
							System.err.println("NotifyViolation: skip update VDC");
							response.getWriter().println("NotifyViolation: " + e.getMessage());

							response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
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
			response.setStatus(HttpStatus.SC_OK);
			response.setContentType("application/json");

		} catch (JsonParseException e) {
			String message = "NotifyViolation: non-well-formed violations";
			e.printStackTrace();
			System.err.println(message);
			response.getWriter().println(message);

			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			response.setContentType("application/json");
		} catch (UnrecognizedPropertyException e) {
			String message = "NotifyViolation: unrecognized property in violations";
			e.printStackTrace();
			System.err.println(message);
			response.getWriter().println(message);

			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			response.setContentType("application/json");
		} catch (Exception e) {
			String message = "NotifyViolation: violations not well-formed";
			e.printStackTrace();
			System.err.println(message);
			response.getWriter().println(message);

			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			response.setContentType("application/json");
		}
	}

}
