package it.polimi.deib.ds4m.test.api;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
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
	private String URSDS4M_notifyViolation = "http://localhost:8080/ROOT/NotifyViolation";
	private String URSDS4M_addVDC = "http://localhost:8080/ROOT/AddVDC";
	//private String URSDS4M_notifyViolation = "http://31.171.247.162:50003/NotifyViolation";
	//private String URSDS4M_setUp = "http://31.171.247.162:50003/SetUp";
	private String URLdataMovementEnactor = "/dataEnactor/action";
	private String URLcomputationMovementEnactor = "/dataEnactor/action";
	
	//set mockup server
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
	
	//set global variables
    ArrayList<Violation> violations;
    ObjectMapper mapper;
    
    //True if the vdc has been added once.
    boolean addVDC = false;
    
    //True if the vdc (for second test on current vdc) has been added once.
    //this is a test executed only one, to test occasional errors
    boolean addVDC_current = false;
    
    
    //boolean variable to control which tests to execute.
    //if false all tests are executed
    //if true only testNotifyViolations_current is executed
    boolean onlyCurrentBlueprint = true;
    
    
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
		metrics.add(metric1_vio1);
		
		Metric metric2_vio1 = new Metric();
		metric2_vio1.setKey("Volume");
		metric2_vio1.setValue("200");
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
		metrics2.add(metric1_vio2);
		
		Metric metric2_vio2 = new Metric();
		metric2_vio2.setKey("Volume");
		metric2_vio2.setValue("200");
		metrics2.add(metric2_vio2);
		
		violation2.setMetrics(metrics2);
		
		violations.add(violation2);
		
	} 
    
	@Test
    public void testAddVDC_correct() 
	{
		//if the VDC has already been added, i skip this call
		if (addVDC | onlyCurrentBlueprint)
		{
			assertTrue(true);
			return;
		}
		
		String concreteBlueprint;
		
		try 
		{
			//applicationRequirements=readFile("./testResources/example_ApplicationRequirements_V11.json", Charset.forName("UTF-8"));
			//concreteBlueprint=Utility.readFile("./testResources/example_ConcreteBluePrint_V3_complete.json", Charset.forName("UTF-8"));//before change
			concreteBlueprint=Utility.readFile("./testResources/example_V5_complete.json", Charset.forName("UTF-8"));
			//concreteBlueprint=Utility.readFile("./testResources/example_V3_ricercatore.json", Charset.forName("UTF-8")); //test reseracher
			//concreteBlueprint=Utility.readFile("./testResources/example_V3_medico.json", Charset.forName("UTF-8")); //test physician 
			
			
		} catch (IOException e) 
		{
			System.err.println("error in reading file applicationRequiorements");
			return;
		}
		
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();    
        HttpPost post = new HttpPost(URSDS4M_addVDC);
		
        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>(3);
        arguments.add(new BasicNameValuePair("ConcreteBlueprint", concreteBlueprint));

        //connect to service
        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
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
			assertTrue(true);
			return;
		}
		
		this.testAddVDC_correct();
		
        //setup a mock server for data movement
		stubFor(post(urlEqualTo(URLdataMovementEnactor))
	            .willReturn(aResponse()
	                .withHeader("Content-Type", "application/x-www-form-urlencoded")
	                .withStatus(200)));
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(URSDS4M_notifyViolation);
		
        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>(3);
        
        

        try
        {
        	String violationJSON = mapper.writeValueAsString(violations);
        	//System.out.println(violationJSON);
        	
			arguments.add(new BasicNameValuePair("violations", violationJSON));
		} 
        catch (JsonProcessingException e1) 
        {
			e1.printStackTrace();
		}

        //connect to service
        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
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
    public void testNotifyViolations_correct_exampleJSONFile() 
	{
		
		//if onlyCurrentBlueprint is true, skip the test
		if (onlyCurrentBlueprint)
		{
			assertTrue(true);
			return;
		}	
		
		this.testAddVDC_correct();
		
        //setup a mock server for data movement
		stubFor(post(urlEqualTo(URLdataMovementEnactor))
	            .willReturn(aResponse()
	                .withHeader("Content-Type", "application/x-www-form-urlencoded")
	                .withStatus(200)));
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(URSDS4M_notifyViolation);
		
        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>(3);
        
        String violations=null;
        
        try {
			violations=Utility.readFile("./testResources/example_violations.json", Charset.forName("UTF-8"));
		} catch (IOException e2) 
        {
			e2.printStackTrace();
		}
        
        arguments.add(new BasicNameValuePair("violations",violations));

        //connect to service
        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
            HttpResponse response = client.execute(post);//response empty

            // Print out the response message
            //System.out.println(EntityUtils.toString(response.getEntity()));
            
            //check the status received
            assertEquals(0,
            		response.getStatusLine().getStatusCode(),
            	     HttpStatus.SC_INTERNAL_SERVER_ERROR);

            
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
			assertTrue(true);
			return;
		}
		
		this.testAddVDC_correct();
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(URSDS4M_notifyViolation);
		
        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>(3);
        arguments.add(new BasicNameValuePair("violations", "{\"violations\":[{\"type_wrong\":\"violation type\",\"methodID_wrong\":\"GetAllBloodTests\",\"vdcID\":\"VDC_02\",\"agreementid\":1,\"guaranteename\":\"guarantee name\",\"date\":\"12/01\",\"metric\":\"Availability\",\"value\":1.0},{\"type\":\"violation type2\",\"methodID\":\"GetAllBloodTests\",\"agreementid\":2,\"vdcID\":\"VDC_02\",\"guaranteename\":\"guarantee name2\",\"date\":\"12/02\",\"metric\":\"ResponseTime\",\"value\":5.0}]}"));
        //connect to service
        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
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
			assertTrue(true);
			return;
		}
		
		this.testAddVDC_correct();
		
        //setup a mock server
		stubFor(post(urlEqualTo(URLdataMovementEnactor))
	            .willReturn(aResponse()
	                .withHeader("Content-Type", "application/x-www-form-urlencoded")
	                .withStatus(200)
	                .withBody("ack")));
		
		
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(URSDS4M_notifyViolation);

		this.setUp();
		
        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>(3);
        try {
			arguments.add(new BasicNameValuePair("violations", mapper.writeValueAsString(violations)));
		} 
        catch (JsonProcessingException e1) 
        {
			e1.printStackTrace();
		}

        //connect to service
        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
            @SuppressWarnings("unused")
			HttpResponse response = client.execute(post);//response empty

            // Print out the response message
            //System.out.println("responce enactors" + EntityUtils.toString(response.getEntity()));
            
            //check the answer to the mocked server
            verify(postRequestedFor(urlEqualTo(URLdataMovementEnactor)).withHeader("Content-Type", equalTo("application/x-www-form-urlencoded")));
            
            verify(postRequestedFor(urlEqualTo(URLdataMovementEnactor)).withRequestBody(containing("movementsEnaction=%7B%22movementsEnaction%22%3A%5B%7B%22from%22%3A%22rescource1%22%2C%22to%22%3A%22rescource3%22%2C%22transformations%22%3A%5B%22Encryption%22%2C%22Aggregation%22%5D%2C%22type%22%3A%22DataDuplication%22%7D%2C%7B%22from%22%3A%22rescource2%22%2C%22to%22%3A%22rescource3%22%2C%22transformations%22%3A%5B%22Encryption%22%2C%22Aggregation%22%5D%2C%22type%22%3A%22DataDuplication%22%7D%2C%7B%22from%22%3A%22MinioDS1%22%2C%22to%22%3A%22rescource3%22%2C%22transformations%22%3A%5B%22Encryption%22%2C%22Aggregation%22%5D%2C%22type%22%3A%22DataDuplication%22%7D%2C%7B%22from%22%3A%22MinioDS2%22%2C%22to%22%3A%22rescource3%22%2C%22transformations%22%3A%5B%22Encryption%22%2C%22Aggregation%22%5D%2C%22type%22%3A%22DataDuplication%22%7D%2C%7B%22from%22%3A%22rescource1%22%2C%22to%22%3A%22rescource3%22%2C%22transformations%22%3A%5B%22Encryption%22%2C%22Aggregation%22%5D%2C%22type%22%3A%22DataMovement%22%7D%2C%7B%22from%22%3A%22rescource2%22%2C%22to%22%3A%22rescource3%22%2C%22transformations%22%3A%5B%22Encryption%22%2C%22Aggregation%22%5D%2C%22type%22%3A%22DataMovement%22%7D%5D%7D")));
            
            	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	@Test
    public void testNotifyViolations_current() 
	{
		this.testAddVDC_current();
		
        //setup a mock server for data movement
		stubFor(post(urlEqualTo(URLdataMovementEnactor))
	            .willReturn(aResponse()
	                .withHeader("Content-Type", "application/x-www-form-urlencoded")
	                .withStatus(200)));
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(URSDS4M_notifyViolation);
		
        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>(3);
        
        String violations=null;
        
        try {
			violations=Utility.readFile("./testResources/test_violation.json", Charset.forName("UTF-8"));
		} catch (IOException e2) 
        {
			e2.printStackTrace();
		}
        
        arguments.add(new BasicNameValuePair("violations",violations));

        //connect to service
        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
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
		//if the VDC has already been added, i skip this call
		if (addVDC_current)
		{
			assertTrue(true);
			return;
		}
		
		String concreteBlueprint;
		
		try 
		{
			concreteBlueprint=Utility.readFile("./testResources/test_current.json", Charset.forName("UTF-8")); 
		} catch (IOException e) 
		{
			System.err.println("error in reading file applicationRequiorements");
			return;
		}
		
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();    
        HttpPost post = new HttpPost(URSDS4M_addVDC);
		
        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>(3);
        arguments.add(new BasicNameValuePair("ConcreteBlueprint", concreteBlueprint));

        //connect to service
        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
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
