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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.TreeStructure;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.movementEnaction.MovementEnaction;
import it.polimi.deib.ds4m.main.model.movementEnaction.MovementsEnaction;
import it.polimi.deib.ds4m.main.movement.GoalTreeManager;
import it.polimi.deib.ds4m.main.movement.MovementsActionsManager;
import it.polimi.deib.ds4m.main.movement.VDCManager;

/**
 * Servlet implementation class NotifyViolation
 */
@WebServlet("/v2/NotifyViolation")
public class NotifyViolation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String urlDA_Resources = "http://178.22.69.180:8080/data-analytics/resources/cloudsigma-deployment/usage/";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NotifyViolation() 
    {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.getWriter().println("<h1>notify violation, DS4M is up<h1>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	//@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//create the json parser
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		
		//retrieve the application requirements
		ArrayList<VDC> VDCs = (ArrayList<VDC>) this.getServletConfig().getServletContext().getAttribute("VDCs");
		
		//retrieve parameter (the list of violations)
		String violationsJSON = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		
		try 
		{
			//convert 
	        ArrayList<Violation> violations = new ArrayList<Violation>(Arrays.asList(mapper.readValue(violationsJSON, Violation[].class)));
	        
	        //while the set of violations contain a violation, keep analysing them
	        for (Violation violation : violations)
	        {
		        //identify VDC
		        VDC violatedVDC = VDCManager.findViolatedVDC(violation, VDCs);
		        if (violatedVDC==null)
		        {
		        	String message = "NotifyViolation: No violated VDC found";
		        	System.err.println(message);
		        	response.getWriter().println(message);
		        	
		        	response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		        	return;
		        }

		        //identify goal
		        Set<TreeStructure> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		        if (violatedGoals==null)
		        {
		        	String message = "NotifyViolation: No violated goals found";
		        	System.err.println(message);
		        	response.getWriter().println(message);
		        	
		        	response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		        	return;
		        }
		        
		        
		        //identify movement actions with positive effect on goal
		        ArrayList<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals, violatedVDC);
		        if (movementsToBeEnacted==null)
		        {
		        	String message = "NotifyViolation: No movements to be enacted found";
		        	System.err.println(message);
		        	response.getWriter().println(message);
		        	
		        	response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		        	return;
		        }
		        
		        //filter dm actions that operates on data sources used by the method who generate the conflicts
		        //NOT NEEDED?
		        
		        //order the dm action using a strategy
		        MovementsActionsManager.orderMovementAction(movementsToBeEnacted, "MONETARY");
		        
		        //check other trees of other VDCs, for all method? 
		        movementsToBeEnacted = VDCManager.chechOtherVDC(movementsToBeEnacted, VDCs, violatedVDC);
		        if (movementsToBeEnacted==null)
		        {
		        	String message = "NotifyViolation: all movements to be enacted have been removed";
		        	System.err.println(message);
		        	response.getWriter().println(message);
		        	
		        	response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		        	response.setContentType("application/json");
		        	return;
		        }
		        
		        //select first data movement action	        
		       //Movement movement = movementsToBeEnacted.firstElement(); 
		       
		       //TODO differentiate between data and computation movements
		        
		        
		        
		       //transform the selected movement actions in element to be sent
		       MovementsEnaction movementsEnaction = new MovementsEnaction();//container of movements  
		       ArrayList<MovementEnaction> movementEnactions = new ArrayList<MovementEnaction>();// vector to be added to the container
		       
		       for (Movement movement : movementsToBeEnacted)//for each movement to be enacted selected, add it to the vector to be sent
		       {
		    	   MovementEnaction movementEnaction = new MovementEnaction();
		    	   movementEnaction.importMovement(movement);
		    	   movementEnactions.add(movementEnaction);
		       }
		       movementsEnaction.setMovementsEnaction(movementEnactions);
		       
		       //once the movement action has been selected, 
		       //1-check the amount of space that is used by the sourse DAL
		       
		       //TODO
		       
		       //2-check if the target node/infrastructure has enough space
		       
		       
				HttpClient client = HttpClientBuilder.create().build();
				HttpGet requestDA = new HttpGet(urlDA_Resources);
				   
				
				HttpResponse responseDA = client.execute(requestDA);
					
				System.out.println("\nSending 'GET' request to URL : " + urlDA_Resources);
				System.out.println("Response Code : " + 
						responseDA.getStatusLine().getStatusCode());
				
				BufferedReader rd = new BufferedReader(
				               new InputStreamReader(responseDA.getEntity().getContent()));
				
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				
				System.out.println(result.toString());
		       
				//parse the input
		       
		       
				//System.out.println("DS4M: Violation processed, movement action enacted");
		        
		        //call to movement enactors
//		        HttpClient client = HttpClientBuilder.create().build();
//		        
//		        //this is for tests
//		        //HttpPost post = new HttpPost("http://localhost:8089/dataEnactor/action");
//		        
//		        //call to dma in kubernetes
//		        HttpPost post = new HttpPost("http://dme:8080/DataMovementEnactor/EnactMovement");
//		        
		        //System.out.println(mapper.writeValueAsString(movementsEnaction));
//		        
//		        // Create some NameValuePair for HttpPost parameters
//		        List<NameValuePair> arguments = new ArrayList<>(3);
//		        arguments.add(new BasicNameValuePair("movementsEnaction", mapper.writeValueAsString(movementsEnaction)));
//		        try {
//		            post.setEntity(new UrlEncodedFormEntity(arguments));
//		            @SuppressWarnings("unused")
//					HttpResponse responseDE = client.execute(post);//response empty
//
//		            //Print out the response of the data movement
//		            System.out.println("Answer of DME: "+EntityUtils.toString(responseDE.getEntity()));
//		            
//		            
//		        } catch (IOException e) {
//		            e.printStackTrace();
//		        }
	        }	
	        //set answer status
	        response.setStatus(HttpStatus.SC_OK);
	        response.setContentType("application/json");
	        
		}
		catch (JsonParseException e)
		{
			String message = "NotifyViolation: non-well-formed violations";
			e.printStackTrace();
        	System.err.println(message);
        	response.getWriter().println(message);
			
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			response.setContentType("application/json");
		}
		catch (UnrecognizedPropertyException e)
		{
			String message = "NotifyViolation: unrecognized property in violations";
			e.printStackTrace();
        	System.err.println(message);
        	response.getWriter().println(message);
			
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			response.setContentType("application/json");
		}
		catch (Exception e)
		{
			String message = "NotifyViolation: violations not well-formed";
			e.printStackTrace();
        	System.err.println(message);
        	response.getWriter().println(message);
			
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			response.setContentType("application/json");
		}
	}
}
