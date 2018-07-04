package it.polimi.deib.ds4m.test.management;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.Vector;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.DataManagement;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Goal;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.GoalTree;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Metric;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Property;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.movement.GoalTreeManager;

public class ManageGoalTreeTest {
	
	/**
	 * tests if the function correctly retrieves goal from a violation that is under a specified range
	 * 
	 * @throws IOException
	 */
	//@Test
    public void findViolatedVDC_Correct_find_below_range() throws IOException 
	{
		//create violation		
		Violation violation = new Violation();
		violation.setType("violation type");
		violation.setAgreementid(1);
		violation.setGuaranteename("guarantee name");
		violation.setDate("12/01");
		violation.setMetric("Availability");
		violation.setValue("90.0");
		violation.setMethodID("GetAllBloodTests");
		violation.setVdcID("01");
		
		
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

	    
		//set up vdc
		VDC violatedVDC = new VDC();
		violatedVDC.setDataManagement(dataManagement);
		violatedVDC.setId("ID");
		
		
		Set<Goal> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		
		assertTrue(violatedGoals.size()==1);
		
		for (Goal goal : violatedGoals)
		{
			assertTrue(goal.getID().equals("1"));
		}
		
		
	}
	
	/**
	 * tests if the function correctly retrieves goal from a violation that is above a specified range
	 * 
	 * @throws IOException
	 */
	//@Test
    public void findViolatedVDC_Correct_find_above_range() throws IOException 
	{
		//create violation		
		Violation violation = new Violation();
		violation.setType("violation type");
		violation.setAgreementid(1);
		violation.setGuaranteename("guarantee name");
		violation.setDate("12/01");
		violation.setMetric("Availability");
		violation.setValue("100.0");
		violation.setMethodID("GetAllBloodTests");
		violation.setVdcID("01");
		
		
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

	    
		//set up vdc
		VDC violatedVDC = new VDC();
		violatedVDC.setDataManagement(dataManagement);
		violatedVDC.setId("ID");
		
		
		Set<Goal> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		
		assertTrue(violatedGoals.size()==1);
		
		for (Goal goal : violatedGoals)
		{
			assertTrue(goal.getID().equals("1"));
		}
		
		
	}
	
	/**
	 * tests if the function correctly retrieves goal from a violation that is below the minimum specified
	 * 
	 * @throws IOException
	 */
	//@Test
    public void findViolatedVDC_Correct_find_minimum() throws IOException 
	{
		//create violation		
		Violation violation = new Violation();
		violation.setType("violation type");
		violation.setAgreementid(1);
		violation.setGuaranteename("guarantee name");
		violation.setDate("12/01");
		violation.setMetric("Precision");
		violation.setValue("0.5");
		violation.setMethodID("GetAllBloodTests");
		violation.setVdcID("01");
		
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

	    
		//set up vdc
		VDC violatedVDC = new VDC();
		violatedVDC.setDataManagement(dataManagement);
		violatedVDC.setId("ID");
		
		
		Set<Goal> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		
		assertTrue(violatedGoals.size()==1);
		
		for (Goal goal : violatedGoals)
		{
			assertTrue(goal.getID().equals("10"));
		}
		
		
	}
	
	/**
	 * tests if the function correctly retrieves goal from a violation that is above the maximum specified
	 * 
	 * @throws IOException
	 */
	//@Test
    public void findViolatedVDC_Correct_find_maximum() throws IOException 
	{
		//create violation		
		Violation violation = new Violation();
		violation.setType("violation type");
		violation.setAgreementid(1);
		violation.setGuaranteename("guarantee name");
		violation.setDate("12/01");
		violation.setMetric("ResponseTime");
		violation.setValue("2.");
		violation.setMethodID("GetAllBloodTests");
		violation.setVdcID("01");

		
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

	    
		//set up vdc
		VDC violatedVDC = new VDC();
		violatedVDC.setDataManagement(dataManagement);
		violatedVDC.setId("ID");
		
		
		Set<Goal> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		
		assertTrue(violatedGoals.size()==1);
		
		for (Goal goal : violatedGoals)
		{
			assertTrue(goal.getID().equals("2"));
		}
		
		
	}
	
	
	/**
	 * tests if the function correctly retrieves goal from a violation of a complex metric, with an exact value
	 * 
	 * @throws IOException
	 */
	//@Test
    public void findViolatedVDC_Correct_find_complex_metric_1Property() throws IOException 
	{
		//create violation		
		Violation violation = new Violation();
		violation.setType("violation type");
		violation.setAgreementid(1);
		violation.setGuaranteename("guarantee name");
		violation.setDate("12/01");
		violation.setMetric("RAM gain");
		violation.setValue("40.0");
		violation.setMethodID("GetAllBloodTests");
		violation.setVdcID("01");
		
		
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

	    
		//set up vdc
		VDC violatedVDC = new VDC();
		violatedVDC.setDataManagement(dataManagement);
		violatedVDC.setId("ID");
		
		
		Set<Goal> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		
		assertTrue(violatedGoals.size()==1);
		
		for (Goal goal : violatedGoals)
		{
			assertTrue(goal.getID().equals("6"));
		}	
	}
	

	/**
	 * tests if the function correctly retrieves 2 goals from a violation 
	 * 
	 * @throws IOException
	 */
	//@Test
    public void findViolatedVDC_Correct_find_complex_metric_2Goals() throws IOException 
	{
		//create violation		
		Violation violation = new Violation();
		violation.setType("violation type");
		violation.setAgreementid(1);
		violation.setGuaranteename("guarantee name");
		violation.setDate("12/01");
		violation.setMetric("Availability");
		violation.setValue("90.0");
		violation.setMethodID("GetAllBloodTests");
		violation.setVdcID("01");
		
		
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

		//add another goal to data utility
		GoalTree dataUtilityGT = dataManagement.getMethods().firstElement().getConstraints().getDataUtility();

		//create a new goal
		String goalID="15";
		
		Property property = new Property();		
		property.setMaximum(99.);
		property.setMinimum(95.);
		property.setName("Availability");
		property.setUnit("percentage");
		
		Metric metric = new Metric();
		metric.setId("11");
		metric.setName("Availability2");
		metric.setType("Availability");
		metric.setProperties(new Vector<Property>());
		metric.getProperties().addElement(property);
		
		Vector<Metric> metrics = new Vector<Metric>();
		metrics.add(metric);
		
		Goal newGoal = new Goal();
		newGoal.setID(goalID);
		newGoal.setName("new goal");
		newGoal.setWeight(1.0);
		newGoal.setMetrics(metrics);
		//end creation goal
			
		
		//insert the goal in the goal list
		dataUtilityGT.getGoals().addElement(newGoal);
		
		//insert the goal in the tree structure
		//it takes the first element of the chiller, if doesn't mater where it puts the new goal
		dataUtilityGT.getTreeStructure().getChildern().firstElement().getLeaves().addElement(goalID);
		
		
		//set up vdc
		VDC violatedVDC = new VDC();
		violatedVDC.setDataManagement(dataManagement);
		violatedVDC.setId("ID");
		
		
		Set<Goal> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		
		assertTrue(violatedGoals.size()==2);
		
		for (Goal goal : violatedGoals)
		{
			assertTrue(goal.getID().equals("1") || goal.getID().equals("15"));
		}	
	}

}
