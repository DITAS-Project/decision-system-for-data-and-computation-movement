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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.AbstractProperty;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.DataManagement;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DAL;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.methodsInput.DataSourceInput;
import it.polimi.deib.ds4m.main.model.methodsInput.Method;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.resources.Infrastructure;
import it.polimi.deib.ds4m.main.movement.MovementsActionsManager;
import it.polimi.deib.ds4m.main.movement.VDCManager;

/**
 * Servlet implementation class AddVDC
 */
@WebServlet("/v2/AddVDC")
public class AddVDC extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddVDC() {
        super();
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked") 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		
		//retrieve concrete blueprint
		//String concreteBlueprintJSON = request.getReader().toString(); //request.getParameter("ConcreteBlueprint");
		String concreteBlueprintJSON = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));	
		
		
		//retrive the movement class
//		String movementsJSON;
//		try {
//			//retrieve movement classes
//			InputStream inputstream = this.getServletConfig().getServletContext().getResourceAsStream("/WEB-INF/movementClasses.json");
//			BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
//			StringBuilder movementsJSONBuilder = new StringBuilder();
//		    String line;
//		    while ((line = reader.readLine()) != null) {
//		    	movementsJSONBuilder.append(line);
//		    }
//		    reader.close();
//		    movementsJSON = movementsJSONBuilder.toString();
//		    
//		}
//		catch (IOException  e) 
//		{
//			String message = "AddVDC: error in loading the movement action configuration file /n" + e.getStackTrace().toString();
//        	System.err.println(message);
//        	response.getWriter().println(message);
//			
//			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
//			return;
//		}
		
		String pathMovementJson= "/Users/mattia/git/decision-system-for-data-and-computation-movement2/testResources/configuration/DS4M_movementClasses.json";
		
		 String movementsJSON=null;
			try {
				BufferedReader movementClassesJSONBR = new BufferedReader(new FileReader(pathMovementJson ));
				movementsJSON= movementClassesJSONBR.lines().collect(Collectors.joining("\n"));
				movementClassesJSONBR.close();
				
			} catch (FileNotFoundException e1) 
			{
				System.out.println("bootConfigurator: error in reading the movement Classes");
				return;
			} catch (IOException e) {
				System.out.println("bootConfigurator: failed closing the BufferedReader for movement classes");
			}
		

		//created a VDC from the json files
		VDC vdc;
		try 
		{
			vdc = VDCManager.createVDC(concreteBlueprintJSON, movementsJSON);
		} catch (Exception e) 
		{
			System.err.println(e.getMessage());
        	response.getWriter().println(e.getMessage());
			
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		//if it is not set create a collection of VDCs
		ArrayList<VDC> VDCs;
		if  (this.getServletConfig().getServletContext().getAttribute("VDCs") == null)
		{
			VDCs = new ArrayList<VDC>();
			this.getServletConfig().getServletContext().setAttribute("VDCs", VDCs);
		}
		else
			VDCs = (ArrayList<VDC>) this.getServletConfig().getServletContext().getAttribute("VDCs");
		
		//add the VDC
		VDCs.add(vdc);
		
		response.setStatus(HttpStatus.SC_OK);
		response.setContentType("application/json");
	
	}

}
