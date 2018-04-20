package it.polimi.deib.ds4m.test.management;

import static org.junit.Assert.assertTrue;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Vector;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.DataManagement;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.movement.ManageMovementsActions;
import it.polimi.deib.ds4m.main.movement.ManageVDC;

public class ManageVDCTest {
	
	@Test
    public void test_Correct() throws IOException 
	{
		//create violation		
		Violation violation = new Violation();
		violation.setType("violation type");
		violation.setAgreementid(1);
		violation.setGuaranteename("guarantee name");
		violation.setDate("12/01");
		violation.setMetric("availability");
		violation.setValue(90.0);
		violation.setMethodID("1");
		violation.setVdcID("1");
		
		
		//parse blueprint
		String concreteBlueprintJSON;
		
		try 
		{
			//applicationRequirements=readFile("./testResources/example_ApplicationRequirements_V11.json", Charset.forName("UTF-8"));
			concreteBlueprintJSON=Utility.readFile("./testResources/example_ConcreteBluePrint_V3_complete.json", Charset.forName("UTF-8"));
			
		} catch (IOException e) 
		{
			System.err.println("error in reading file applicationRequiorements");
			return;
		}
		
		
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(concreteBlueprintJSON);
		
		//retrieve DATA MANAGEMENT
		JsonNode dataManagementJson = root.get("DATA_MANAGEMENT");
		DataManagement dataManagement = mapper.treeToValue(dataManagementJson, DataManagement.class);
		
		//retrieve data sources
		JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
		Vector<DataSource> dataSources = new Vector<DataSource>(Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class)));
		
		
		//retrieve movement classes
	    String movementsJSON = Utility.readFile("./WEB-INF/movementClasses.json", Charset.forName("UTF-8"));
	    
	    //instantiate movement classes for each data source 
	    ManageMovementsActions.instantiateMovementActions(dataSources,movementsJSON);
	    
		//set up vdc
		VDC vdc = new VDC();
		vdc.setDataManagement(dataManagement);
		vdc.setDataSources(dataSources);
		vdc.setId("01");
		
		//if it is not set create a collection of appl.s requirements
		Vector<VDC> VDCs = new Vector<VDC>();
		
		//add the application requirements 
		VDCs.addElement(vdc);
		
		
		//identify VDC
        VDC violatedVDC = ManageVDC.findViolatedVDC(violation, VDCs);
        
        assertTrue(violatedVDC.getId().equals("01"));
		
		
	}

}
