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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.Violations;

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
    Violations violations;
    ObjectMapper mapper;
    
    //True if the vdc has been added once.
    boolean addVDC = false;
    
	//setup to be called to instantiate variables
    @Before
	public void setUp() 
	{
    	//create class for JSON conversion
    	mapper = new ObjectMapper();
		
		//set up violations
		violations = new Violations();
		
		ArrayList<Violation> violationsArrayList = new ArrayList<Violation>();
		
		Violation violation1 = new Violation();
		violation1.setType("violation type");
		violation1.setAgreementid(1);
		violation1.setGuaranteename("guarantee name");
		violation1.setDate("12/01");
		violation1.setMetric("Availability");
		violation1.setValue("90.0");
		violation1.setMethodID("GetAllBloodTests");
		violation1.setVdcID("VDC_2");
		
		violationsArrayList.add(violation1);
		
		Violation violation2 = new Violation();
		violation2.setType("violation type");
		violation2.setAgreementid(2);
		violation2.setGuaranteename("guarantee name");
		violation2.setDate("12/01");
		violation2.setMetric("Volume");
		violation2.setValue("200");
		violation2.setMethodID("GetAllBloodTests");
		violation2.setVdcID("VDC_2");
		
		violationsArrayList.add(violation2);
	
		
		violations.setViolations(violationsArrayList);
		
	} 
    
	@Test
    public void testAddVDC_correct() 
	{
		//if the VDC has already been added, i skip this call
		if (addVDC)
		{
			assertTrue(true);
			return;
		}
		
		String concreteBlueprint;
		
		try 
		{
			//applicationRequirements=readFile("./testResources/example_ApplicationRequirements_V11.json", Charset.forName("UTF-8"));
			//concreteBlueprint=Utility.readFile("./testResources/example_ConcreteBluePrint_V3_complete.json", Charset.forName("UTF-8"));//before change
			//concreteBlueprint=Utility.readFile("./testResources/example_V5_complete.json", Charset.forName("UTF-8"));
			//concreteBlueprint=Utility.readFile("./testResources/example_V3_ricercatore.json", Charset.forName("UTF-8")); //test reseracher
			concreteBlueprint=Utility.readFile("./testResources/example_V3_medico.json", Charset.forName("UTF-8")); //test physician 
			
			
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
			arguments.add(new BasicNameValuePair("violations", mapper.writeValueAsString(violations)));
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
    public void testNotifyViolations_notCorrect() 
	{
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

	
}
