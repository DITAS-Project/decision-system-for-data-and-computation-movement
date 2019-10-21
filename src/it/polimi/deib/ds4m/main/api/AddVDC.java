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


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
		//retrieve the list of VDCs
		//if it is not set create a collection of VDCs
		ArrayList<VDC> VDCs;
		if  (this.getServletConfig().getServletContext().getAttribute("VDCs") == null)
		{
			VDCs = new ArrayList<VDC>();
			this.getServletConfig().getServletContext().setAttribute("VDCs", VDCs);
		}
		else
			VDCs = (ArrayList<VDC>) this.getServletConfig().getServletContext().getAttribute("VDCs");
		
		
		String VDCID=request.getHeader("VDCID");
		
		//if the VDC already exists the skip it
		for (VDC vdc : VDCs)
		{
			if (vdc.getId().equals(VDCID))
			{
				response.getWriter().println("AddVDC: VDC already present, ignored");
				response.setStatus(HttpStatus.SC_OK);
				response.setContentType("application/json");
				return;
			}
				
		}
		
		String concreteBlueprintJSON = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));	

		//check if the persistent volume folder exists. if not, it is not mounted (it is a junit test execution) and skip the save
		String movementsJSON;
		try {
			movementsJSON = MovementsActionsManager.loadMovementClass(this);
		} catch (Exception e) 
		{
			System.err.println("AddVDC: " + e.getMessage());
        	response.getWriter().println("AddVDC: " + e.getMessage());
			 
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		System.out.println("ADDVDC: parsed movements");

		//created a VDC from the json files
		VDC vdc;
		try 
		{
			vdc = VDCManager.createVDC(concreteBlueprintJSON, movementsJSON);
		} catch (Exception e) 
		{
			System.err.println("AddVDC: " + e.getMessage());
        	response.getWriter().println("AddVDC: " + e.getMessage());
			
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		
		// i change the name with the name in the parameter
		if (VDCID!=null)//this check is for test environment(integration)
			vdc.setId(VDCID);
		
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
		
		//add the VDC
		VDCs.add(vdc);
		
		//check if the persistent volume folder exists. if not, it is not mounted (it is a junit test execution) and skip the save
		if ((new File(PathSetting.pv)).exists())
		{		
			//create the directory structure if needed
			new File(PathSetting.statusSerialize_pv).mkdirs();
			
			//save VDCs list
			ObjectOutputStream oos = null;
			FileOutputStream fout = null;
			try{
			    fout = new FileOutputStream(PathSetting.statusSerializeSer, false);
			    oos = new ObjectOutputStream(fout);
			    oos.writeObject(VDCs);
			} catch (Exception ex) 
			{
			    ex.printStackTrace();
			} finally 
			{
			    if(oos != null){
			        oos.close();
			    } 
			}
		}
		else
		{
			System.err.println("AddVdc: folder "+PathSetting.pv+" does not exists, status not saved");
		}
	
		
		
		response.setStatus(HttpStatus.SC_OK);
		response.setContentType("application/json");
	
	}

}
