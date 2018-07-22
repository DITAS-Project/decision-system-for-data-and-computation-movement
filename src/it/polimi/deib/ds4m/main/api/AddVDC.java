package it.polimi.deib.ds4m.main.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.model.concreteBlueprint.AbstractProperty;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.DataManagement;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.resources.Resource;
import it.polimi.deib.ds4m.main.movement.MovementsActionsManager;

/**
 * Servlet implementation class AddVDC
 */
@WebServlet("/AddVDC")
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//retrieve concrete blueprint
		String concreteBlueprintJSON = request.getParameter("ConcreteBlueprint");
		
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
		
		JsonNode root; 
		try {
			root = mapper.readTree(concreteBlueprintJSON);
		}
		catch (JsonParseException e)
		{
			String message = "AddVDC: error in reading the tree of the Blueprint /n" + e.getStackTrace().toString();
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;
		}
		
		//retrieve DATA MANAGEMENT
		JsonNode dataManagementJson = root.get("DATA_MANAGEMENT");
		ArrayList<DataManagement> dataManagement; 
		try {
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
			dataManagement = new ArrayList<DataManagement>(Arrays.asList(mapper.treeToValue(dataManagementJson, DataManagement[].class)));
		}
		
		catch (JsonProcessingException e) 
		{
			String message = "AddVDC: error in parsing the DATA MANAGEMET Section of the Blueprint /n" + e.getStackTrace().toString();
        	System.err.println(message);
        	response.getWriter().println(message);
			
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		
		//retrieve ABSTRACT_PROPERTIES
		JsonNode abstractPropertiesJson = root.get("ABSTRACT_PROPERTIES");
		ArrayList<AbstractProperty> abstractProperties; 
		try {
			abstractProperties = new ArrayList<AbstractProperty>(Arrays.asList(mapper.treeToValue(abstractPropertiesJson, AbstractProperty[].class)));
		}
		
		catch (JsonProcessingException e) 
		{
			String message = "AddVDC: error in parsing the ABSTRACT PROPERTIES Section of the Blueprint /n" + e.getStackTrace().toString();
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);

			return;			
		}
		
		//retrieve resources
		JsonNode resourcesJSON = root.get("INTERNAL_STRUCTURE").get("resourcesAvailable");
		ArrayList<Resource> resources;
		try {
			resources = new ArrayList<Resource>(Arrays.asList(mapper.treeToValue(resourcesJSON, Resource[].class)));
		}
		catch (JsonProcessingException e) 
		{
			String message = "AddVDC: error in parsing the RESOURCE Section of the Blueprint /n" + e.getStackTrace().toString();
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		
		//retrieve data sources
		JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
		ArrayList<DataSource> dataSources;
		try {
			dataSources = new ArrayList<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
		}
		catch (JsonProcessingException e) 
		{
			String message = "AddVDC: error in parsing the DATA SOURCE Section of the Blueprint /n" + e.getStackTrace().toString();
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		
		//create a fake (initial) resource for the data source
		for (DataSource dataSource: dataSources)
		{
			dataSource.createResource(resources);
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
			String message = "AddVDC: error in loading the movement action configuration file /n" + e.getStackTrace().toString();
        	System.err.println(message);
        	response.getWriter().println(message);
			
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
	    //instantiate movement classes for each data source
		ArrayList<Movement> instantiatedMovements = MovementsActionsManager.instantiateMovementActions(resources,movementsJSON.toString()); 
	    if (instantiatedMovements==null)
	    {
	    	String message = "AddVDC: error in instantiating the data movement actions";
        	System.err.println(message);
        	response.getWriter().println(message);
	    	
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			return;
	    }
	    
	    //retrieve VDC name
		JsonNode vdcNameJSON = root.get("INTERNAL_STRUCTURE").get("Overview").get("name");
		String vdcName;
		try {
			vdcName = mapper.treeToValue(vdcNameJSON, String.class);
		}
		catch (JsonProcessingException e) 
		{
			String message = "AddVDC: error in parsing the NAME Section of the Blueprint /n" + e.getStackTrace().toString();
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
	    
		//set up vdc
		VDC vdc = new VDC();
		vdc.setDataManagement(dataManagement);
		vdc.setAbstractProperties(abstractProperties);
		vdc.setDataSources(dataSources);
		vdc.setMovements(instantiatedMovements);
		vdc.connectAbstractProperties();
		vdc.setResources(resources);
		vdc.setId(vdcName);
		
		//if it is not set create a collection of appl.s requirements
		ArrayList<VDC> VDCs;
		if  (this.getServletConfig().getServletContext().getAttribute("VDCs") == null)
		{
			VDCs = new ArrayList<VDC>();
			this.getServletConfig().getServletContext().setAttribute("VDCs", VDCs);
		}
		else
			VDCs = (ArrayList<VDC>) this.getServletConfig().getServletContext().getAttribute("VDCs");
		
		//add the application requirements 
		VDCs.add(vdc);
		
		response.setStatus(HttpStatus.SC_OK);
	
	}

}
