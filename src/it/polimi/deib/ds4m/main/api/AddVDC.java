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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import it.polimi.deib.ds4m.main.configuration.PathSetting;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
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

		//check if the persistent volume folder exists. if not, it is not mounted (it is a junit test execution) and skip the save
		String movementsJSON=null;
		if ((new File(PathSetting.movementClassJson)).exists())
		{
			try {
				BufferedReader movementClassesJSONBR = new BufferedReader(new FileReader(PathSetting.movementClassJson));
				movementsJSON= movementClassesJSONBR.lines().collect(Collectors.joining("\n"));
				movementClassesJSONBR.close();
				
			} catch (FileNotFoundException e1) 
			{
				System.out.println("addVDC: reading from classes movement from webinf");
				//if it fails the class path might not exists, therefore we might be in the unit test environement, try to load from web inf 
				try {
					//retrieve movement classes
					InputStream inputstream = this.getServletConfig().getServletContext().getResourceAsStream("/WEB-INF/movementClasses.json");
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
					StringBuilder movementsJSONBuilder = new StringBuilder();
				    String line;
				    while ((line = reader.readLine()) != null) {
				    	movementsJSONBuilder.append(line);
				    }
				    reader.close();
				    movementsJSON = movementsJSONBuilder.toString();
			    
				}
				catch (IOException  e) 
				{
					String message = "AddVDC: error in loading the movement action configuration file /n" + e.getStackTrace().toString();
		        	System.err.println(message);
		        	response.getWriter().println(message);
					
					response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					return;
				}


			} catch (IOException e) {
				System.out.println("AddVDC: failed closing the BufferedReader for movement classes");
			}
		}
		else//if the volume is not mounted, i retrive it for test from web-inf folder
		{

			try {
				//retrieve movement classes
				InputStream inputstream = this.getServletConfig().getServletContext().getResourceAsStream("/WEB-INF/movementClasses.json");
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
				StringBuilder movementsJSONBuilder = new StringBuilder();
			    String line;
			    while ((line = reader.readLine()) != null) {
			    	movementsJSONBuilder.append(line);
			    }
			    reader.close();
			    movementsJSON = movementsJSONBuilder.toString();
			    
			}
			catch (IOException  e) 
			{
				String message = "AddVDC: error in loading the movement action configuration file /n" + e.getStackTrace().toString();
	        	System.err.println(message);
	        	response.getWriter().println(message);
				
				response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				return;
			}
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
		
		
		//check if the persistent volume folder exists. if not, it is not mounted (it is a junit test execution) and skip the save
		if ((new File(PathSetting.pv)).exists())
		{		
			//create the directory structure if needed
			new File(PathSetting.blueprints_pv).mkdirs();
			
			//once i parsed it and it is correct, i save it
			try (PrintWriter out = new PrintWriter(PathSetting.blueprints_pv+vdc.getId()+".json")) {
			    out.println(concreteBlueprintJSON);
			}
			catch (java.io.FileNotFoundException e)
			{
				String errMessage = "ADDVDC: failed to write the VDC json file in persistent volume";
				System.err.println(errMessage);
	        	response.getWriter().println(errMessage);
				
				response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				return;
			}
		}
		else
		{
			System.err.println("AddVdc: folder "+PathSetting.pv+" does not exists, concrete blueprint not saved");
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
