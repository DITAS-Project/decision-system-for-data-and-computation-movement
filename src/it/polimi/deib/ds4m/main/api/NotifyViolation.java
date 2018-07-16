package it.polimi.deib.ds4m.main.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.Violations;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
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
@WebServlet("/NotifyViolation")
public class NotifyViolation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//create the json parser
		ObjectMapper mapper = new ObjectMapper();
		
		//retrieve the application requirements
		ArrayList<VDC> VDCs = (ArrayList<VDC>) this.getServletConfig().getServletContext().getAttribute("VDCs");
		
		//retrieve parameter (the list of violations)
		String violationsJSON = request.getParameter("violations");

		try 
		{
			//convert 
	        Violations violations = mapper.readValue(violationsJSON, Violations.class);
	         
	        //while the set of violations contain a violation, keep analysing them
	        while (violations.getViolations().size()!=0)
	        {
	        	ArrayList<Violation> violationToBeExamined = new ArrayList<Violation>();
	        	
	        	//collect all violations to the same VDC and method
	        	String vdcViolated = null;
	        	String methodViolated = null;
	        	for (Violation violation : violations.getViolations())
	        	{
	        		//if it is the first violation, put it in the list of violation to be examined
	        		if (vdcViolated==null)
	        		{
	        			vdcViolated=violation.getVdcID();
	        			methodViolated = violation.getMethodID();
	        			violationToBeExamined.add(violation);
	        		}
	        		//and collect similar (with same method and VDC IDs) violations
	        		else if (vdcViolated.equals(violation.getVdcID()) & methodViolated.equals(violation.getMethodID()))
	        		{
	        			violationToBeExamined.add(violation);
	        		}	
	        	}
	        	
    			//if the violation is examined, then i remove it from the set of violation that have yet to be examined
    			violations.getViolations().removeAll(violationToBeExamined);
		        
		        //identify VDC
		        VDC violatedVDC = VDCManager.findViolatedVDC(violationToBeExamined, VDCs);
		        if (violatedVDC==null)
		        {
		        	System.err.println("NotifyViolation: No violated VDC found");
		        	response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		        	return;
		        }

		        //identify goal
		        Set<Goal> violatedGoals = GoalTreeManager.findViolatedGoals(violationToBeExamined, violatedVDC);
		        if (violatedGoals==null)
		        {
		        	System.out.println("NotifyViolation: No violated goals found");
		        	response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		        	return;
		        }
		        
		        
		        //identify movement actions with positive effect on goal
		        ArrayList<Movement> movementsToBeEnacted = MovementsActionsManager.findMovementAction(violatedGoals, violatedVDC);
		        if (movementsToBeEnacted==null)
		        {
		        	System.err.println("NotifyViolation: No movements to be enacted found");
		        	response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		        	return;
		        }
		        
		        //filter dm actions that operates on data sources used by the method who generate the conflicts
		        //NOT NEEDED?
		        
		        //order the dm action using a strategy
		        MovementsActionsManager.orderMovementAction(movementsToBeEnacted, MovementsActionsManager.Strategy.MONETARY);
		        
		        //check other trees of other VDCs, for all method? 
		        movementsToBeEnacted = VDCManager.chechOtherVDC(movementsToBeEnacted, VDCs, violatedVDC);
		        if (movementsToBeEnacted==null)
		        {
		        	System.err.println("NotifyViolation: all movements to be enacted have been removed");
		        	response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		        	return;
		        }
		        
		        //select first data movement action	        
		       //Movement movement = movementsToBeEnacted.firstElement(); 
		       
		       //TODO differentiate between data and computation movements
		        
		       //transform the selected movement actions in element to be sent
		       MovementsEnaction movementsEnaction = new MovementsEnaction();//container of movements  
		       ArrayList<MovementEnaction> movementEnactions = new ArrayList<MovementEnaction>();// vector to be dded to the container
		       
		       for (Movement movement : movementsToBeEnacted)//for each movement to be enacted selected, add it to the vector to be sent
		       {
		    	   MovementEnaction movementEnaction = new MovementEnaction();
		    	   movementEnaction.importMovement(movement);
		    	   movementEnactions.add(movementEnaction);
		       }
		       movementsEnaction.setMovementsEnaction(movementEnactions);
		        
		        //call to movement enactors
		        HttpClient client = HttpClientBuilder.create().build();
		        HttpPost post = new HttpPost("http://localhost:8089/dataEnactor/action");
		        
		        //System.out.println(mapper.writeValueAsString(movementsEnaction));
		        
		        // Create some NameValuePair for HttpPost parameters
		        List<NameValuePair> arguments = new ArrayList<>(3);
		        arguments.add(new BasicNameValuePair("movementsEnaction", mapper.writeValueAsString(movementsEnaction)));
		        try {
		            post.setEntity(new UrlEncodedFormEntity(arguments));
		            @SuppressWarnings("unused")
					HttpResponse responseDE = client.execute(post);//response empty

		            //Print out the response of the data movement
		            //System.out.println(EntityUtils.toString(responseDE.getEntity()));
		            
		            
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	        }	
	        //set answer status
	        response.setStatus(HttpStatus.SC_OK);
		}
		catch (JsonParseException e)
		{
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
		}
		catch (UnrecognizedPropertyException e)
		{
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
		}
	}
}
