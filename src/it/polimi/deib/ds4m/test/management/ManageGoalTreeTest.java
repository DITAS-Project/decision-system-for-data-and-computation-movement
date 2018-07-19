package it.polimi.deib.ds4m.test.management;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.junit.Test;

import it.polimi.deib.ds4m.main.model.Metric;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Attribute;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.Property;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.TreeStructure;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.movement.GoalTreeManager;

public class ManageGoalTreeTest {
	
	/**
	 * tests if the function correctly retrieves goal from a violation that is under a specified range
	 * 
	 * @throws IOException
	 */
	@Test
    public void findViolatedVDC_Correct_find_below_range() throws IOException 
	{
		
		//set up violation		
		Violation violation = new Violation();
		violation.setMethodId("GetAllBloodTests");
		violation.setVdcId("VDC_2");

		ArrayList<Metric> metrics = new ArrayList<Metric>();
		
		Metric metric1_vio1 = new Metric();
		metric1_vio1.setKey("Availability");
		metric1_vio1.setValue("90.0");
		metrics.add(metric1_vio1);
		
		violation.setMetrics(metrics);
		
		//*** set up other VDC ( it takes the complete one, not necessary)
		VDC violatedVDC = ManageMovementsActionsTest.setUpVDC();
		
		
		Set<TreeStructure> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		
		assertTrue(violatedGoals.size()==1);
		
		for (TreeStructure goal : violatedGoals)
		{
			assertTrue(goal.getID().equals("serviceAvailable"));
		}
		
		
	}
	
	/**
	 * tests if the function correctly retrieves goal from a violation that is above a specified range
	 * 
	 * @throws IOException
	 */
	@Test
    public void findViolatedVDC_Correct_find_above_range() throws IOException 
	{
		//create violation			
		Violation violation = new Violation();
		violation.setMethodId("GetAllBloodTests");
		violation.setVdcId("VDC_2");

		ArrayList<Metric> metrics = new ArrayList<Metric>();
		
		Metric metric1_vio1 = new Metric();
		metric1_vio1.setKey("Availability");
		metric1_vio1.setValue("100.0");
		metrics.add(metric1_vio1);
		
		violation.setMetrics(metrics);
		
		//*** set up other VDC ( it takes the complete one, not necessary)
		VDC violatedVDC = ManageMovementsActionsTest.setUpVDC();
		
		
		Set<TreeStructure> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		
		assertTrue(violatedGoals.size()==1);
		
		for (TreeStructure goal : violatedGoals)
		{
			assertTrue(goal.getID().equals("serviceAvailable"));
		}
		
		
	}
	
	/**
	 * tests if the function correctly retrieves goal from a violation that is below the minimum specified
	 * 
	 * @throws IOException
	 */
	@Test
    public void findViolatedVDC_Correct_find_minimum() throws IOException 
	{		
		Violation violation = new Violation();
		violation.setMethodId("GetAllBloodTests");
		violation.setVdcId("VDC_2");

		ArrayList<Metric> metrics = new ArrayList<Metric>();
		
		Metric metric1_vio1 = new Metric();
		metric1_vio1.setKey("Precision");
		metric1_vio1.setValue("0.5");
		metrics.add(metric1_vio1);
		
		violation.setMetrics(metrics);
		
		//*** set up other VDC ( it takes the complete one, not necessary)
		VDC violatedVDC = ManageMovementsActionsTest.setUpVDC();
		
		
		Set<TreeStructure> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		
		assertTrue(violatedGoals.size()==1);
		
		for (TreeStructure goal : violatedGoals)
		{
			assertTrue(goal.getID().equals("consistency"));
		}
		
		
	}
	
	/**
	 * tests if the function correctly retrieves goal from a violation that is above the maximum specified
	 * 
	 * @throws IOException
	 */
	@Test
    public void findViolatedVDC_Correct_find_maximum() throws IOException 
	{
		
		//set up violation		
		Violation violation = new Violation();
		violation.setMethodId("GetAllBloodTests");
		violation.setVdcId("VDC_2");

		ArrayList<Metric> metrics = new ArrayList<Metric>();
		
		Metric metric1_vio1 = new Metric();
		metric1_vio1.setKey("ResponseTime");
		metric1_vio1.setValue("2.");
		metrics.add(metric1_vio1);
		
		violation.setMetrics(metrics);
		
		//*** set up other VDC ( it takes the complete one, not necessary)
		VDC violatedVDC = ManageMovementsActionsTest.setUpVDC();
		
		
		Set<TreeStructure> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		
		assertTrue(violatedGoals.size()==1);
		
		for (TreeStructure goal : violatedGoals)
		{
			assertTrue(goal.getID().equals("fastDataProcess"));
		}
		
		
	}
	
	
	/**
	 * tests if the function correctly retrieves goal from a violation of a complex metric, with an exact value
	 * 
	 * @throws IOException
	 */
	@Test
    public void findViolatedVDC_Correct_find_complex_metric_1Property() throws IOException 
	{
		//set up violation		
		Violation violation = new Violation();
		violation.setMethodId("GetAllBloodTests");
		violation.setVdcId("VDC_2");

		ArrayList<Metric> metrics = new ArrayList<Metric>();
		
		Metric metric1_vio1 = new Metric();
		metric1_vio1.setKey("ramGain");
		metric1_vio1.setValue("40.0.");
		metrics.add(metric1_vio1);
		
		violation.setMetrics(metrics);
		
		//*** set up other VDC ( it takes the complete one, not necessary)
		VDC violatedVDC = ManageMovementsActionsTest.setUpVDC();
		
		
		Set<TreeStructure> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		
		assertTrue(violatedGoals.size()==1);
		
		for (TreeStructure goal : violatedGoals)
		{
			assertTrue(goal.getID().equals("serviceScalable"));
		}	
	}
	

	/**
	 * tests if the function correctly retrieves 2 goals from a violation 
	 * 
	 * @throws IOException
	 */
	@Test
    public void findViolatedVDC_Correct_find_complex_metric_2Goals() throws IOException 
	{
		//set up violation		
		Violation violation = new Violation();
		violation.setMethodId("GetAllBloodTests");
		violation.setVdcId("VDC_2");

		ArrayList<Metric> metrics = new ArrayList<Metric>();
		
		Metric metric1_vio1 = new Metric();
		metric1_vio1.setKey("Availability");
		metric1_vio1.setValue("90.0");
		metrics.add(metric1_vio1);
		
		violation.setMetrics(metrics);
		
		//*** set up other VDC ( it takes the complete one, not necessary)
		VDC violatedVDC = ManageMovementsActionsTest.setUpVDC();

		//add another goal to data utility
		//getAbstractProperties().get(0) -> selects the fist method ( there is only one)
		TreeStructure dataUtilityTS =violatedVDC.getAbstractProperties().get(0).getGoalTrees().getDataUtility();  

		//create a new goal
		String goalID="newGoal";
		
		Property property = new Property();		
		property.setMaximum(99.);
		property.setMinimum(95.);
		property.setName("Availability");
		property.setUnit("percentage");
		
		Attribute attribute = new Attribute();
		attribute.setId("Availability2");
		attribute.setDescription("Availability2");
		attribute.setType("Availability");
		attribute.setProperties(new HashMap<String, Property>());
		attribute.getProperties().put("Availability", property);
		
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(attribute);
		
		TreeStructure newGoal = new TreeStructure(null);//set the argument to null since i specify a goal
		newGoal.setID(goalID);
		newGoal.setDescription("new goal");
		newGoal.setWeight(1.0);
		newGoal.setAttributesLinked(attributes);
		//end creation goal
		
		//insert the goal in the leaves
		dataUtilityTS.getChildren().get(0).getChildren().get(0).getLeaves().add(newGoal);
	
		
		Set<TreeStructure> violatedGoals = GoalTreeManager.findViolatedGoals(violation, violatedVDC);
		
		assertTrue(violatedGoals.size()==2);
		
		for (TreeStructure goal : violatedGoals)
		{
			assertTrue(goal.getID().equals("serviceAvailable") || goal.getID().equals(goalID));
		}	
	}

}
