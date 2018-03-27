package it.polimi.deib.ds4m.test.api;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.google.gson.Gson;

import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.Violations;
import it.polimi.deib.ds4m.main.model.applicationRequirement.ApplicationRequirements;
import it.polimi.deib.ds4m.main.model.applicationRequirement.ApplicationsRequirements;
import it.polimi.deib.ds4m.main.model.movement.Cost;
import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.movement.Movements;
import it.polimi.deib.ds4m.main.model.movement.Transformation;

public class NotifyViolationTest 
{	
	//set URLS and costants
	private String URSDS4M_notifyViolation = "http://localhost:8080/DS4M/NotifyViolation";
	private String URSDS4M_setUp = "http://localhost:8080/DS4M/SetUp";
	private String URLdataMovementEnactor = "/dataEnactor/action";
	private String URLcomputationMovementEnactor = "/dataEnactor/action";
	
	//set mockup server
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
	
	//set global variables
    Violations violations;
    Gson gsonConverter;
    
	//setup to be called to instantiate variables
    @Before
	public void setUp() 
	{
    		//create class for conversion
		gsonConverter = new Gson();

		//set up movements
		Boolean movementsFromFile = true;
		String movementsJSON;
		
		if (!movementsFromFile)
		{
			Vector<Cost> costs = new Vector<Cost>();
			costs.add(new Cost("moneraty","dollars/MB",12));
			costs.add(new Cost("time","ms/MB",0.3));
			
			Vector<String> impacts = new Vector<String>();
			impacts.add("1");
			impacts.add("2");
			impacts.add("4");
			
			Vector<Transformation> transformations = new Vector<Transformation>();
			transformations.add(new Transformation("Encryption", impacts, impacts, costs));
			transformations.add(new Transformation("Aggregation", impacts, impacts, costs));
			
			
			Vector<Movement> movementsTmp = new Vector<Movement>();
			movementsTmp.add(new Movement("ComputationMovement","Edge", "Cloud", impacts, impacts, transformations, costs));
			movementsTmp.add(new Movement("DataDuplication","Cloud", "Edge", impacts, impacts, transformations, costs));
			
			Movements movements = new Movements(movementsTmp);
			movementsJSON = gsonConverter.toJson(movements);
		}
		else
		{
			try 
			{
				movementsJSON=readFile("./testResources/example_MovementClasses_V1.json", Charset.forName("UTF-8"));
				
			} catch (IOException e) 
			{
				System.err.println("error in reading file applicationRequiorements");
				return;
			}
			
		}
		
		
		
		
		//set up violations
		violations = new Violations();
		
		Vector<Violation> violationsVector = new Vector<Violation>();
		violationsVector.add(new Violation("violation type", 1, "guarantee name", "12/01", "availability", 1.0));
		violationsVector.add(new Violation("violation type2", 2, "guarantee name2", "12/02", "responce time", 2.0));
		
		violations.setViolations(violationsVector);
	} 
    
	@Test
    public void testSetUp_correct() 
	{
		
		String applicationRequirements;
		
		try 
		{
			applicationRequirements=readFile("./testResources/example_ApplicationRequirements_V10.json", Charset.forName("UTF-8"));
			
		} catch (IOException e) 
		{
			System.err.println("error in reading file applicationRequiorements");
			return;
		}
		
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();    
        HttpPost post = new HttpPost(URSDS4M_setUp);
		
        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>(3);
        arguments.add(new BasicNameValuePair("applicationRequirements", applicationRequirements));

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
		this.testSetUp_correct();
		
        //setup a mock server for data movement
		stubFor(post(urlEqualTo(URLdataMovementEnactor))
	            .willReturn(aResponse()
	                .withHeader("Content-Type", "application/x-www-form-urlencoded")
	                .withStatus(200)
	                .withBody("ack")));
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(URSDS4M_notifyViolation);
		
        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>(3);
        arguments.add(new BasicNameValuePair("violations", gsonConverter.toJson(violations)));

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
		this.testSetUp_correct();
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(URSDS4M_notifyViolation);
		
        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>(3);
        arguments.add(new BasicNameValuePair("violations", "{\\\"violations\\\":[{\\\"type11\\\":\\\"violation type\\\",\\\"agreementid\\\":1,\\\"guaranteename\\\":\\\"guarantee name\\\",\\\"date\\\":\\\"12/01\\\",\\\"metric\\\":\\\"availability\\\",\\\"value\\\":1.0},{\\\"type\\\":\\\"violation type2\\\",\\\"agreementid\\\":2,\\\"guaranteename11\\\":\\\"guarantee name2\\\",\\\"date\\\":\\\"12/02\\\",\\\"metric\\\":\\\"responce time\\\",\\\"value\\\":2.0}]}"));

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
		this.testSetUp_correct();
		
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
        arguments.add(new BasicNameValuePair("violations", gsonConverter.toJson(violations)));

        //connect to service
        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
            HttpResponse response = client.execute(post);//response empty

            // Print out the response message
            //System.out.println(EntityUtils.toString(response.getEntity()));
            
            //check the answer to the mocked server
            verify(postRequestedFor(urlEqualTo(URLdataMovementEnactor))
                    .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded")));
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	
	
}
