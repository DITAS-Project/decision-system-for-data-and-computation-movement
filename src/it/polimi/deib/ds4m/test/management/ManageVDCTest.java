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
package it.polimi.deib.ds4m.test.management;

import static org.junit.Assert.assertTrue;


import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import it.polimi.deib.ds4m.main.model.Metric;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.movement.Cost;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.movement.VDCManager;

public class ManageVDCTest {
	
	/**
	 * Test if it returns the correct id
	 * 
	 * @throws IOException
	 */
	@Test
    public void findViolatedVDC_Correct_find() throws IOException 
	{		
		//set up violation		
		Violation violation = new Violation();
		violation.setMethodId("GetAllBloodTests");
		violation.setVdcId("01");

		ArrayList<Metric> metrics = new ArrayList<Metric>();
		
		Metric metric1_vio1 = new Metric();
		metric1_vio1.setKey("Availability");
		metric1_vio1.setValue("90.0.");
		metrics.add(metric1_vio1);
		
		
		ArrayList<Violation> violations = new ArrayList<Violation>();
		violations.add(violation);

		//set up vdc
		VDC vdc = new VDC();
		vdc.setId("01");
		
		//if it is not set create a collection of appl.s requirements
		ArrayList<VDC> VDCs = new ArrayList<VDC>();
		
		//add the application requirements 
		VDCs.add(vdc);
		
		
		//identify VDC
        VDC violatedVDC = VDCManager.findViolatedVDC(violation, VDCs);
        
        assertTrue(violatedVDC.getId().equals("01"));
		
		
	}
	
	/**
	 * test if the method returns null if no VDC are found
	 * 
	 * @throws IOException
	 */
	@Test
    public void findViolatedVDC_Correct_noResults() throws IOException 
	{
		
		//set up violation		
		Violation violation = new Violation();
		violation.setMethodId("GetAllBloodTests");
		violation.setVdcId("10");

		ArrayList<Metric> metrics = new ArrayList<Metric>();
		
		Metric metric1_vio1 = new Metric();
		metric1_vio1.setKey("Availability");
		metric1_vio1.setValue("90.0.");
		metrics.add(metric1_vio1);
		
		ArrayList<Violation> violations = new ArrayList<Violation>();
		violations.add(violation);

		
		//set up vdc
		VDC vdc = new VDC();
		vdc.setId("01");
		
		//if it is not set create a collection of appl.s requirements
		ArrayList<VDC> VDCs = new ArrayList<VDC>();
		
		//add the application requirements 
		VDCs.add(vdc);
		
		
		//identify VDC
        VDC violatedVDC = VDCManager.findViolatedVDC(violation, VDCs);
        
        assertTrue(violatedVDC==null);
		
		
	}

	/**
	 * test if the function returns the movement to be enacted list untouched, if the movements are not present in other VDC  
	 * 
	 * @throws IOException
	 */
	@Test
    public void chechOtherVDC_Correct_noMovementsCommon() throws IOException 
	{
		//*** set up other VDC ( it takes the complete one, not necessary)
		VDC vdc = ManageMovementsActionsTest.setUpVDC();
		vdc.setId("VDC_2");

		
		//create a collection of vdcs
		ArrayList<VDC> VDCs = new ArrayList<VDC>();
		
		//add the application requirements 
		VDCs.add(vdc);
	    
        
		//set up the vdc selected ( it needs only the id)
		VDC VDCselected = new VDC();
		VDCselected.setId("00");
		
		
		//setup the list of movement to be enacted
		ArrayList<Movement> movementsToBeEnacted = new ArrayList<Movement>();
		
		//set up the first movement
		ArrayList<Cost> costs = new ArrayList<Cost>();
		Cost cost1= new Cost();
		cost1.setType("monetary");
		cost1.setUnit("dollars/MB");
		cost1.setValue(15.0);
		costs.add(cost1);
		
		Cost cost2= new Cost();
		cost2.setType("time");
		cost2.setUnit("ms/MB");
		cost2.setValue(0.3);
		costs.add(cost2);
		
		Movement movement1 =  new Movement();
		movement1.setFromType("CCC");//no movement with this parameter in other VDC
		movement1.setToType("DDD");//no movement with this parameter in other VDC
		movement1.setRestTime(12.);
		movement1.setType("ComputationMovement");
		movement1.setCosts(costs);
		//end set up movement
		
		//set up another movement (non matching to check only that the first one is moved back an empty data movement will not match any data movement in other VDC, meaning that it has no influence on other vdc goals)
		Movement movement2 =  new Movement();
		movement2.setFromType("AAA");//no movement with this parameter in other VDC
		movement2.setToType("BBB");//no movement with this parameter in other VDC
		movement2.setRestTime(12.);
		movement2.setType("ComputationMovement");
		movement2.setCosts(costs);
		//end setup movment
		
		movementsToBeEnacted.add(movement1);
		movementsToBeEnacted.add(movement2);
		
		movementsToBeEnacted = VDCManager.chechOtherVDC(movementsToBeEnacted, VDCs, VDCselected);
        
        //check that the movement is put back
        assertTrue(movementsToBeEnacted.indexOf(movement1)==0);
        assertTrue(movementsToBeEnacted.indexOf(movement2)==1);
		
		
	}
	
	/**
	 * test if the function correctly move 1 movement from the first position, behind.
	 * 
	 * @throws IOException
	 */
	@Test
    public void chechOtherVDC_CorrectFirstPosition() throws IOException 
	{
		//*** set up other VDC ( it takes the complete one, not necessary)
		
		//set up vdc
		VDC vdc = ManageMovementsActionsTest.setUpVDC();

		
		//create a collection of vdcs
		ArrayList<VDC> VDCs = new ArrayList<VDC>();
		
		//add the application requirements 
		VDCs.add(vdc);
	    
        
		//set up the vdc selected ( it needs only the id)
		VDC VDCselected = new VDC();
		VDCselected.setId("00");
		
		
		//setup the list of movement to be enacted
		ArrayList<Movement> movementsToBeEnacted = new ArrayList<Movement>();
		
		//set up the first movement
		//the data movement selected (movements to be enacted) are two. 
		//movement1 has a negative impact to "id=completeness" so the function "checkOtherVDC" checks if the movement action has impact on other vdc. in this case the set of "other VDC" is composed of
		//the "complete VDC" set up in the beginning of this test. such VDC has a similar data movement with only negative impact in the goal "id=completeness". so the movement 1 should be moved behind.
		//movemen2 should be left untouched since there is no similar data movement
		
		ArrayList<String> positiveImpacts1 = new ArrayList<String>();
		positiveImpacts1.add("AAA");//positive impacts are not considered for the movement to be enacted 
		
		ArrayList<String> negativeImpacts1 = new ArrayList<String>();
		negativeImpacts1.add("completeness");
		
		ArrayList<Cost> costs = new ArrayList<Cost>();
		Cost cost1= new Cost();
		cost1.setType("monetary");
		cost1.setUnit("dollars/MB");
		cost1.setValue(50.0);
		costs.add(cost1);
		
		Cost cost2= new Cost();
		cost2.setType("time");
		cost2.setUnit("ms/MB");
		cost2.setValue(0.3);
		costs.add(cost2);
		
		Movement movement1 =  new Movement();
		movement1.setPositiveImpacts(positiveImpacts1);//impacts are not considered in the comparison of data movement (extension of "equals" interface)
		movement1.setNegativeImpacts(negativeImpacts1);//impacts are not considered in the comparison of data movement (extension of "equals" interface)
		movement1.setFromType("edge");
		movement1.setToType("cloud");
		movement1.setRestTime(12.);
		movement1.setType("DataMovement");
		movement1.setCosts(costs);
		//end set up movement
		
		//set up another movement (non matching to check only that the first one is moved back an empty data movement will not match any data movement in other VDC, meaning that it has no influence on other vdc goals])
		Movement movement2 =  new Movement();
		movement2.setPositiveImpacts(positiveImpacts1);
		movement2.setNegativeImpacts(negativeImpacts1);
		movement2.setFromType("AAA");//no movement with this parameter in other VDC
		movement2.setToType("BBB");//no movement with this parameter in other VDC
		movement2.setRestTime(12.);
		movement2.setType("ComputationMovement");
		movement2.setCosts(costs);
		//end setup movement
		
		movementsToBeEnacted.add(movement1);
		movementsToBeEnacted.add(movement2);
		
		movementsToBeEnacted = VDCManager.chechOtherVDC(movementsToBeEnacted, VDCs, VDCselected);
        
        //check that the movement is put back
        assertTrue(movementsToBeEnacted.indexOf(movement1)==1);
		
	}
	
	/**
	 * test if the function correctly move 1 movement from the middle of the array of movements, behind.
	 * 
	 * @throws IOException
	 */
	@Test
    public void chechOtherVDC_CorrectMiddlePosiotion() throws IOException 
	{
		
		//*** set up other VDC ( it takes the complete one, not necessary)
		VDC vdc = ManageMovementsActionsTest.setUpVDC();

		
		//create a collection of vdcs
		ArrayList<VDC> VDCs = new ArrayList<VDC>();
		
		//add the application requirements 
		VDCs.add(vdc);
	    
        
		//set up the vdc selected ( it needs only the id)
		VDC VDCselected = new VDC();
		VDCselected.setId("00");
		
		
		//setup the list of movement to be enacted
		ArrayList<Movement> movementsToBeEnacted = new ArrayList<Movement>();
		
		//set up the first movement
		//the data movement selected (movements to be enacted) are two. 
		//movement1 has a negative impact to "id=completeness" so the function "checkOtherVDC" checks if the movement action has impact on other vdc. in this case the set of "other VDC" is composed of
		//the "complete VDC" set up in the beginning of this test. such VDC has a similar data movement with only negative impact in the goal "id=completeness". so the movement 1 should be moved behind.
		//movemen2 should be left untouched since there is no similar data movement
		
		ArrayList<String> positiveImpacts1 = new ArrayList<String>();
		positiveImpacts1.add("AAA");//positive impacts are not considered for the movement to be enacted 
		
		ArrayList<String> negativeImpacts1 = new ArrayList<String>();
		negativeImpacts1.add("completeness");
		
		ArrayList<Cost> costs = new ArrayList<Cost>();
		Cost cost1= new Cost();
		cost1.setType("monetary");
		cost1.setUnit("dollars/MB");
		cost1.setValue(50.0);
		costs.add(cost1);
		
		Cost cost2= new Cost();
		cost2.setType("time");
		cost2.setUnit("ms/MB");
		cost2.setValue(0.3);
		costs.add(cost2);
		
		Movement movement1 =  new Movement();
		movement1.setPositiveImpacts(positiveImpacts1);//impacts are not considered in the comparison of data movement (extension of "equals" interface)
		movement1.setNegativeImpacts(negativeImpacts1);//impacts are not considered in the comparison of data movement (extension of "equals" interface)
		movement1.setFromType("edge");
		movement1.setToType("cloud");
		movement1.setRestTime(12.);
		movement1.setType("DataMovement");
		movement1.setCosts(costs);
		//end set up movement
		
		//set up another movement (non matching to check only that the first one is moved back an empty data movement will not match any data movement in other VDC, meaning that it has no influence on other vdc goals])
		Movement movement2 =  new Movement();
		movement2.setPositiveImpacts(positiveImpacts1);
		movement2.setNegativeImpacts(negativeImpacts1);
		movement2.setFromType("AAA");//no movement with this parameter in other VDC
		movement2.setToType("BBB");//no movement with this parameter in other VDC
		movement2.setRestTime(12.);
		movement2.setType("ComputationMovement");
		movement2.setCosts(costs);
		//end setup movment
		
		//setup the third movement
		Movement movement0 =  new Movement();
		movement0.setPositiveImpacts(positiveImpacts1);
		movement0.setNegativeImpacts(negativeImpacts1);
		movement0.setFromType("CCC");//no movement with this parameter in other VDC
		movement0.setToType("BBB");//no movement with this parameter in other VDC
		movement0.setRestTime(12.);
		movement0.setType("ComputationMovement");
		movement0.setCosts(costs);
		//end setup
		
		movementsToBeEnacted.add(movement0);
		movementsToBeEnacted.add(movement1);
		movementsToBeEnacted.add(movement2);
		
		movementsToBeEnacted = VDCManager.chechOtherVDC(movementsToBeEnacted, VDCs, VDCselected);
        
        //check that the movement is put back
        assertTrue(movementsToBeEnacted.indexOf(movement1)==2);
		
		
	}

}
