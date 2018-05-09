package it.polimi.deib.ds4m.test.management;

import static org.junit.Assert.assertTrue;


import java.io.IOException;
import java.util.Vector;

import org.junit.Test;

import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.movement.ManageVDC;

public class ManageVDCTest {
	
	/**
	 * Test if it returns the correct id
	 * 
	 * @throws IOException
	 */
	@Test
    public void findViolatedVDC_Correct_find() throws IOException 
	{
		//create violation		
		Violation violation = new Violation();
		violation.setType("violation type");
		violation.setAgreementid(1);
		violation.setGuaranteename("guarantee name");
		violation.setDate("12/01");
		violation.setMetric("availability");
		violation.setValue("90.0");
		violation.setMethodID("1");
		violation.setVdcID("01");
		

		//set up vdc
		VDC vdc = new VDC();
		vdc.setId("01");
		
		//if it is not set create a collection of appl.s requirements
		Vector<VDC> VDCs = new Vector<VDC>();
		
		//add the application requirements 
		VDCs.addElement(vdc);
		
		
		//identify VDC
        VDC violatedVDC = ManageVDC.findViolatedVDC(violation, VDCs);
        
        assertTrue(violatedVDC.getId().equals("01"));
		
		
	}
	
	@Test
    public void findViolatedVDC_Correct_noResults() throws IOException 
	{
		//create violation		
		Violation violation = new Violation();
		violation.setType("violation type");
		violation.setAgreementid(1);
		violation.setGuaranteename("guarantee name");
		violation.setDate("12/01");
		violation.setMetric("availability");
		violation.setValue("90.0");
		violation.setMethodID("1");
		violation.setVdcID("10");

		
		//set up vdc
		VDC vdc = new VDC();
		vdc.setId("01");
		
		//if it is not set create a collection of appl.s requirements
		Vector<VDC> VDCs = new Vector<VDC>();
		
		//add the application requirements 
		VDCs.addElement(vdc);
		
		
		//identify VDC
        VDC violatedVDC = ManageVDC.findViolatedVDC(violation, VDCs);
        
        assertTrue(violatedVDC==null);
		
		
	}

}
