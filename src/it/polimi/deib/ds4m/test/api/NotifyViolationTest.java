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
package it.polimi.deib.ds4m.test.api;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.model.Metric;
import it.polimi.deib.ds4m.main.model.Violation;

public class NotifyViolationTest 
{	
	//set URLS and costants
	private String urlDS4M_notifyViolation = "http://localhost:8080/ROOT/NotifyViolation";
	private String urlDS4M_addVDC = "http://localhost:8080/ROOT/AddVDC";
	//private String URSDS4M_notifyViolation = "http://31.171.247.162:50003/NotifyViolation";
	//private String URSDS4M_setUp = "http://31.171.247.162:50003/SetUp";
	private String urlDS4M_RemoveVDC = "http://localhost:8080/ROOT/RemoveVDC";
	private String urlDS4M_RemoveAllVDC = "http://localhost:8080/ROOT/RemoveAllVDC";
	
	
	private String urlDataMovementEnactor = "/dataEnactor/action";
	private String urlComputationMovementEnactor = "/dataEnactor/action";
	
	//paths to blueprint and violations
	private String pathCorrectBlueprint="./testResources/test_Blueprint_V7.json"; 
	private String pathCorrectViolations="./testResources/test_violationCorrect.json";
	
	//set mockup server
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
	
	//set global variables
    ArrayList<Violation> violations;
    ObjectMapper mapper;
   
    
    //True if the vdc (for second test on current vdc) has been added once.
    //this is a test executed only one, to test occasional errors
    boolean addVDC_current = false;
    
    
    //test current 
    //test when we want to verify an example of blueprint or violations sent by other partners 
    
    //boolean variable to control which tests to execute.
    //if false all tests are executed
    //if true only testNotifyViolations_current is executed
    boolean onlyCurrentBlueprint = false; //when is true, check that the method testNotifyViolations_current()  has been enables as test. 
    
    //paths to current files
	//private String pathCurrentBlueprint="./testResources/test_Blueprint_V6_correct.json";
	private String pathCurrentBlueprint="./testResources/test_Blueprint_V7.json";
	private String pathViolationCurrent="./testResources/test_violationCorrect.json";
    
	//setup to be called to instantiate variables
    @Before
	public void setUp() 
	{
    	//create class for JSON conversion
    	mapper = new ObjectMapper();
    	mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		
		//set up violations
		violations = new ArrayList<Violation>();
		
		Violation violation1 = new Violation();
		violation1.setMethodId("GetAllBloodTests");
		violation1.setVdcId("VDC_2");

		ArrayList<Metric> metrics = new ArrayList<Metric>();
		
		Metric metric1_vio1 = new Metric();
		metric1_vio1.setKey("Availability");
		metric1_vio1.setValue("90.0");
		metric1_vio1.setDatetime("2018-08-07T10:50:23.674337517+02:00");
		metrics.add(metric1_vio1);
		
		Metric metric2_vio1 = new Metric();
		metric2_vio1.setKey("Volume");
		metric2_vio1.setValue("200");
		metric2_vio1.setDatetime("2018-08-07T10:50:23.674337517+02:00");
		metrics.add(metric2_vio1);
		
		violation1.setMetrics(metrics);
		
		violations.add(violation1);
		
		Violation violation2 = new Violation();
		violation2.setMethodId("GetAllBloodTests");
		violation2.setVdcId("VDC_2");

		ArrayList<Metric> metrics2 = new ArrayList<Metric>();
		
		Metric metric1_vio2 = new Metric();
		metric1_vio2.setKey("Availability");
		metric1_vio2.setValue("90.0");
		metric1_vio2.setDatetime("2018-08-07T10:50:23.674337517+02:00");
		metrics2.add(metric1_vio2);
		
		Metric metric2_vio2 = new Metric();
		metric2_vio2.setKey("Volume");
		metric2_vio2.setValue("200");
		metric2_vio2.setDatetime("2018-08-07T10:50:23.674337517+02:00");
		metrics2.add(metric2_vio2);
		
		violation2.setMetrics(metrics2);
		
		violations.add(violation2);
		
	} 
    
    private void removeAllVDCs()
    {
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();    
        HttpPost post = new HttpPost(urlDS4M_RemoveAllVDC);

        //connect to service
        try {
        	
            HttpResponse response = client.execute(post);//response empty

            // Print out the response message
            //System.out.println(EntityUtils.toString(response.getEntity()));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testAddVDC_correct() 
	{
		//if the VDC has already been added, i skip this call
		if (onlyCurrentBlueprint)
		{
			System.out.println("skipped testAddVDC_correct");
			assertTrue(true);
			return;
		}
		
		String concreteBlueprint;
		
		try 
		{
			concreteBlueprint=Utility.readFile(pathCorrectBlueprint, Charset.forName("UTF-8"));
			
		} catch (IOException e) 
		{
			System.err.println("error in reading file applicationRequiorements");
			return;
		}
		
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();    
        HttpPost post = new HttpPost(urlDS4M_addVDC);
        
        //set the body
        StringEntity params=null;
		try {
			params = new StringEntity(concreteBlueprint);
		} 
		catch (UnsupportedEncodingException e1) 
		{
			e1.printStackTrace();
		}
		

        //connect to service
        try {
        	post.setEntity(params);
        	
            HttpResponse response = client.execute(post);//response empty

            // Print out the response message
            //System.out.println(EntityUtils.toString(response.getEntity()));
            
            //check the status received
            assertEquals(0,
            		response.getStatusLine().getStatusCode(),
            	     HttpStatus.SC_OK);

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testNotifyViolations_correct() 
	{
		//if onlyCurrentBlueprint is true, skip the test
		if (onlyCurrentBlueprint)
		{
			System.out.println("skipped testNotifyViolations_correct");
			assertTrue(true);
			return;
		}
		
		//clear from all vdcs
		this.removeAllVDCs();
		
		this.testAddVDC_correct();
		
        //setup a mock server for data movement
		stubFor(post(urlEqualTo(urlDataMovementEnactor))
	            .willReturn(aResponse()
	                .withHeader("Content-Type", "application/x-www-form-urlencoded")
	                .withStatus(200)));
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(urlDS4M_notifyViolation);
		
        
        //read violations
        String violationJSON=null;
        try {
        	violationJSON=Utility.readFile(pathCorrectViolations, Charset.forName("UTF-8"));
        	
		} catch (IOException e2) 
        {
			e2.printStackTrace();
		}
        
        //create body
        StringEntity params=null;
        try
        {
        	//String violationJSON = mapper.writeValueAsString(violations);
        	//System.out.println(violationJSON);
        	params = new StringEntity(violationJSON);
		}
        catch (UnsupportedEncodingException e1) 
		{
			e1.printStackTrace();
		}
        

        //connect to service
        try {
        	post.setEntity(params);
            HttpResponse response = client.execute(post);//response empty

            // Print out the response message
            //System.out.println(EntityUtils.toString(response.getEntity()));
            
            //check the status received
            assertEquals(0,
            		response.getStatusLine().getStatusCode(),
            	     HttpStatus.SC_OK);

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    @Test
    public void testNotifyViolations_notCorrect() 
	{
		//if onlyCurrentBlueprint is true, skip the test
		if (onlyCurrentBlueprint)
		{
			System.out.println("skipped testNotifyViolations_notCorrect");
			assertTrue(true);
			return;
		}
		
		//clear from all vdcs
		this.removeAllVDCs();
		
		this.testAddVDC_correct();
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(urlDS4M_notifyViolation);
		

        //set the body 
        StringEntity params=null;
		try {
			params = new StringEntity("violations\", \"{\\\"violations\\\":[{\\\"type_wrong\\\":\\\"violation type\\\",\\\"methodID_wrong\\\":\\\"GetAllBloodTests\\\",\\\"vdcID\\\":\\\"VDC_02\\\",\\\"agreementid\\\":1,\\\"guaranteename\\\":\\\"guarantee name\\\",\\\"date\\\":\\\"12/01\\\",\\\"metric\\\":\\\"Availability\\\",\\\"value\\\":1.0},{\\\"type\\\":\\\"violation type2\\\",\\\"methodID\\\":\\\"GetAllBloodTests\\\",\\\"agreementid\\\":2,\\\"vdcID\\\":\\\"VDC_02\\\",\\\"guaranteename\\\":\\\"guarantee name2\\\",\\\"date\\\":\\\"12/02\\\",\\\"metric\\\":\\\"ResponseTime\\\",\\\"value\\\":5.0}]}");
		} 
		catch (UnsupportedEncodingException e1) 
		{
			e1.printStackTrace();
		}
        
        
        //connect to service
        try {
        	post.setEntity(params);
            HttpResponse response = client.execute(post);//response empty
                        
            assertEquals(0,
            		response.getStatusLine().getStatusCode(),
            	     HttpStatus.SC_BAD_REQUEST);

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    
    @Test
    public void testNotifyViolations_content() 
	{
		//if onlyCurrentBlueprint is true, skip the test
		if (onlyCurrentBlueprint)
		{
			System.out.println("skipped testNotifyViolations_content");
			assertTrue(true);
			return;
		}
		
		//clear from all vdcs
		this.removeAllVDCs();
		
		//this.testAddVDC_correct();
		
        //setup a mock server
		stubFor(post(urlEqualTo(urlDataMovementEnactor))
	            .willReturn(aResponse()
	                .withHeader("Content-Type", "application/x-www-form-urlencoded")
	                .withStatus(200)
	                .withBody("ack")));
		
		
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(urlDS4M_notifyViolation);

		this.setUp();
        
        //create body
        StringEntity params=null;
        try
        {
        	String violationJSON = mapper.writeValueAsString(violations);
        	params = new StringEntity(violationJSON);
		} 
        catch (JsonProcessingException e1) 
        {
			e1.printStackTrace();
		}
        catch (UnsupportedEncodingException e1) 
		{
			e1.printStackTrace();
		}
        
        

        //connect to service
        try {
            post.setEntity(params);
            @SuppressWarnings("unused")
			HttpResponse response = client.execute(post);//response empty

            // Print out the response message
            //System.out.println("responce enactors" + EntityUtils.toString(response.getEntity()));
            
            //check the answer to the mocked server
            //verify(postRequestedFor(urlEqualTo(urlDataMovementEnactor)).withHeader("Content-Type", equalTo("application/x-www-form-urlencoded")));
            
            //verify(postRequestedFor(urlEqualTo(urlDataMovementEnactor)).withRequestBody(containing("movementsEnaction=%7B%22movementsEnaction%22%3A%5B%7B%22from%22%3A%22rescource1%22%2C%22to%22%3A%22rescource3%22%2C%22transformations%22%3A%5B%22Encryption%22%2C%22Aggregation%22%5D%2C%22type%22%3A%22DataDuplication%22%7D%2C%7B%22from%22%3A%22rescource2%22%2C%22to%22%3A%22rescource3%22%2C%22transformations%22%3A%5B%22Encryption%22%2C%22Aggregation%22%5D%2C%22type%22%3A%22DataDuplication%22%7D%2C%7B%22from%22%3A%22MinioDS1%22%2C%22to%22%3A%22rescource3%22%2C%22transformations%22%3A%5B%22Encryption%22%2C%22Aggregation%22%5D%2C%22type%22%3A%22DataDuplication%22%7D%2C%7B%22from%22%3A%22MinioDS2%22%2C%22to%22%3A%22rescource3%22%2C%22transformations%22%3A%5B%22Encryption%22%2C%22Aggregation%22%5D%2C%22type%22%3A%22DataDuplication%22%7D%2C%7B%22from%22%3A%22rescource1%22%2C%22to%22%3A%22rescource3%22%2C%22transformations%22%3A%5B%22Encryption%22%2C%22Aggregation%22%5D%2C%22type%22%3A%22DataMovement%22%7D%2C%7B%22from%22%3A%22rescource2%22%2C%22to%22%3A%22rescource3%22%2C%22transformations%22%3A%5B%22Encryption%22%2C%22Aggregation%22%5D%2C%22type%22%3A%22DataMovement%22%7D%5D%7D")));
            
            	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
    //@Test
    public void testNotifyViolations_current() 
	{
		//clear from all vdcs
		this.removeAllVDCs();
		
		//add the current blueprint
		this.testAddVDC_current();
		
        //setup a mock server for data movement
		stubFor(post(urlEqualTo(urlDataMovementEnactor))
	            .willReturn(aResponse()
	                .withHeader("Content-Type", "application/x-www-form-urlencoded")
	                .withStatus(200)));
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(urlDS4M_notifyViolation);
		

        //read file
        String violations=null;        
        try {
			violations=Utility.readFile(pathViolationCurrent, Charset.forName("UTF-8"));
		} catch (IOException e2) 
        {
			e2.printStackTrace();
		}
        
        //set the body
        StringEntity params=null;
		try {
			params = new StringEntity(violations);
		} 
		catch (UnsupportedEncodingException e1) 
		{
			e1.printStackTrace();
		}
       

        //connect to service
        try {
            post.setEntity(params);
            HttpResponse response = client.execute(post);//response empty

            // Print out the response message
            //System.out.println(EntityUtils.toString(response.getEntity()));
            
            //check the status received
            assertEquals(0,
            		response.getStatusLine().getStatusCode(),
            	     HttpStatus.SC_OK);

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public void testAddVDC_current() 
	{
		
		String concreteBlueprint;
		
		try 
		{
			concreteBlueprint=Utility.readFile(pathCurrentBlueprint, Charset.forName("UTF-8"));
		} catch (IOException e) 
		{
			System.err.println("error in reading file blueprint");
			return;
		}
		
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();    
        HttpPost post = new HttpPost(urlDS4M_addVDC);
		
        //set the body
        StringEntity params=null;
		try {
			params = new StringEntity(concreteBlueprint);
		} 
		catch (UnsupportedEncodingException e1) 
		{
			e1.printStackTrace();
		}

        //connect to service
        try {
            post.setEntity(params);
            HttpResponse response = client.execute(post);//response empty

            // Print out the response message
            //System.out.println(EntityUtils.toString(response.getEntity()));
            
            //check the status received
            assertEquals(0,
            		response.getStatusLine().getStatusCode(),
            	     HttpStatus.SC_OK);

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	
}
