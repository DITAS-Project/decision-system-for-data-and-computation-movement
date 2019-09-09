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
import java.io.InputStream;
import java.io.InputStreamReader;
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
		
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		
		JsonNode root; 
		try {
			root = mapper.readTree(concreteBlueprintJSON);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			String message = "AddVDC: error in reading the tree of the Blueprint";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;
		}
		
		//retrieve DATA MANAGEMENT
		JsonNode dataManagementJson = root.get("DATA_MANAGEMENT");
		if (dataManagementJson ==null)
		{
			String message = "AddVDC: the DATA MANAGEMENT Section of the Blueprint is empty/n";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		ArrayList<DataManagement> dataManagement; 
		try {
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
			dataManagement = new ArrayList<DataManagement>(Arrays.asList(mapper.treeToValue(dataManagementJson, DataManagement[].class)));
		}
		
		catch (JsonProcessingException e) 
		{
			String message = "AddVDC: error in parsing the DATA MANAGEMENT Section of the Blueprint /n" + e.getStackTrace().toString();
        	System.err.println(message);
        	response.getWriter().println(message);
			
			response.setStatus(HttpStatus.SC_BAD_REQUEST); 
			return;			
		}
		
		//retrieve ABSTRACT_PROPERTIES
		JsonNode abstractPropertiesJson = root.get("ABSTRACT_PROPERTIES");
		if (abstractPropertiesJson ==null)
		{
			String message = "AddVDC: the ABSTRACT PROPERTIES Section of the Blueprint is empty/n";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		ArrayList<AbstractProperty> abstractProperties; 
		try {
			abstractProperties = new ArrayList<AbstractProperty>(Arrays.asList(mapper.treeToValue(abstractPropertiesJson, AbstractProperty[].class)));
		}
		
		catch (JsonProcessingException e) 
		{
			String message = "AddVDC: error in parsing the ABSTRACT PROPERTIES Section of the Blueprint /n" + e.getStackTrace().toString();
			e.printStackTrace();
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);

			return;			
		}
		
		//retrieve resources (infrastucture)
		JsonNode infrastructureJSON = root.get("COOKBOOK_APPENDIX").get("infrastructure");
		if (infrastructureJSON ==null)
		{
			String message = "AddVDC: the INFRASTRUCURE Section of the Blueprint is empty/n";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		
		ArrayList<Infrastructure> infrastructures;
		try {
			infrastructures = new ArrayList<Infrastructure>(Arrays.asList(mapper.treeToValue(infrastructureJSON, Infrastructure[].class)));
		}
		catch (JsonProcessingException e) 
		{
			String message = "AddVDC: error in parsing the INFRASTRUCURE Section of the Blueprint /n";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		
		//retrieve data sources
		JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
		if (dataSourcesJSON ==null)
		{
			String message = "AddVDC: the DATA SOURCES Section of the Blueprint is empty/n";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		ArrayList<DataSource> dataSources;
		try {
			dataSources = new ArrayList<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
		}
		catch (JsonProcessingException e) 
		{
			String message = "AddVDC: error in parsing the DATA SOURCE Section of the Blueprint /n";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		
		//retrive methods input
		JsonNode methodsInputsJSON = root.get("INTERNAL_STRUCTURE").get("Methods_Input").get("Methods");
		if (methodsInputsJSON ==null)
		{
			String message = "AddVDC: the Methods_Input Section of the Blueprint is empty/n";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		ArrayList<Method> methodsInputs;
		try {
			methodsInputs = new ArrayList<Method>(Arrays.asList(mapper.treeToValue(methodsInputsJSON, Method[].class)));
		}
		catch (JsonProcessingException e) 
		{
			String message = "AddVDC: error in parsing the Methods_Input Section of the Blueprint /n";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		
		//link data source in method input with data sources already retrieved in the blueprint
		try {
			//for each method in the method input
			for (Method method : methodsInputs)
			{
				//for each data sources input described
				for (DataSourceInput dataSourceInput : method.getDataSources())
				{
					dataSourceInput.linkDatasource(dataSources);
				}
			}
		}
		catch (Exception e) 
		{
			String message = "AddVDC:  the ids in methods input does not match the id in data source: " + e.getMessage();
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		
		
		//retrive DALs
		JsonNode DALsJSON = root.get("INTERNAL_STRUCTURE").get("DAL_Images");
		if (DALsJSON ==null)
		{
			String message = "AddVDC: the DAL_Images Section of the Blueprint is empty/n";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		
		Map<String,DAL> DALs;
		try {
			TypeReference<HashMap<String, DAL>> typeRef = new TypeReference<HashMap<String, DAL>>() {};
			DALs =mapper.readValue(
				    mapper.treeAsTokens(DALsJSON), 
				    mapper.getTypeFactory().constructType(typeRef));

		}
		catch (JsonProcessingException e) 
		{
			String message = "AddVDC: error in parsing the DAL_Images Section of the Blueprint /n";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			return;			
		}
		
		//for each data source [DAL] creates a fake (initial) infrastructure for the data source, to allow movement
		//create an array list to it in the VDC object
		ArrayList<DAL> DALsArrayList = new ArrayList<DAL>();
		for (DAL DAL: DALs.values())
		{
			DAL.createResource(infrastructures);
			DALsArrayList.add(DAL);
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
	
		
		//store info
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
		ArrayList<Movement> instantiatedMovements = MovementsActionsManager.instantiateMovementActions(infrastructures,movementsJSON.toString(),DALsArrayList); 
	    if (instantiatedMovements==null)
	    {
	    	String message = "AddVDC: error in instantiating the data movement actions";
        	System.err.println(message);
        	response.getWriter().println(message);
	    	
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			return;
	    }

	    
		//set up vdc
		VDC vdc = new VDC();
		vdc.setDataManagement(dataManagement);
		vdc.setAbstractProperties(abstractProperties);
		vdc.setDataSources(dataSources);
		vdc.setMovements(instantiatedMovements);
		vdc.connectAbstractProperties();
		vdc.setResources(infrastructures);
		vdc.setId(vdcName);
		vdc.setMethodsInputs(methodsInputs);
		vdc.setDALs(DALsArrayList);
		
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
