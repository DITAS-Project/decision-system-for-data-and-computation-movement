package it.polimi.deib.ds4m.main.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.model.concreteBlueprint.DataManagement;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.movement.ManageMovementsActions;

/**
 * Servlet implementation class SetUp
 */
@WebServlet("/SetUp")
public class SetUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetUp() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("SetUp, Use POST method");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//retrieve concrete blueprint
		String concreteBlueprintJSON = request.getParameter("ConcreteBlueprint");
		
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root; 
		try {
			root = mapper.readTree(concreteBlueprintJSON);
		}
		catch (JsonParseException e)
		{
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;
		}
		
		//retrieve DATA MANAGEMENT
		JsonNode dataManagementJson = root.get("DATA_MANAGEMENT");
		DataManagement dataManagement;
		try {
			dataManagement = mapper.treeToValue(dataManagementJson, DataManagement.class);
		}
		catch (JsonProcessingException e) 
		{
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		
		//retrieve data sources
		JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
		Vector<DataSource> dataSources;
		try {
			dataSources = new Vector<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
		}
		catch (JsonProcessingException e) 
		{
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		
		StringBuilder movementsJSON;
		try {
			//retrieve movement classes
			InputStream inputstream = this.getServletConfig().getServletContext().getResourceAsStream("/WEB-INF/movementClasses.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
		    movementsJSON = new StringBuilder();
		    String line;
		    while ((line = reader.readLine()) != null) {
		    	movementsJSON.append(line);
		    }
		    reader.close();
		}
		catch (IOException  e) 
		{
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
	    //instantiate movement classes for each data source 
	    if (!ManageMovementsActions.instantiateMovementActions(dataSources,movementsJSON.toString()))
	    {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			return;
	    }
	    
		//set up vdc
		VDC vdc = new VDC();
		vdc.setDataManagement(dataManagement);
		vdc.setDataSources(dataSources);
		vdc.setId("01");//TODO insert VDC ID in comncrete blueprint
		
		//if it is not set create a collection of appl.s requirements
		Vector<VDC> VDCs;
		if  (this.getServletConfig().getServletContext().getAttribute("VDCs") == null)
		{
			VDCs = new Vector<VDC>();
			this.getServletConfig().getServletContext().setAttribute("VDCs", VDCs);
		}
		else
			VDCs = (Vector<VDC>) this.getServletConfig().getServletContext().getAttribute("VDCs");
		
		//add the application requirements 
		VDCs.addElement(vdc);
		
		response.setStatus(HttpStatus.SC_OK);
	
	}
	

}
