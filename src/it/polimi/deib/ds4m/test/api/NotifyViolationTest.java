package it.polimi.deib.ds4m.test.api;


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

public class NotifyViolationTest 
{	
	//set URLS and costants
	private String URSDS4M_notifyViolation = ":50003/DS4M/NotifyViolation";
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

		//set up class
		violations = new Violations();
		
		Vector<Violation> violationsVector = new Vector<Violation>();
		violationsVector.add(new Violation("violation type", 1, "guarantee name", "12/01", "availability", 1.0));
		violationsVector.add(new Violation("violation type2", 2, "guarantee name2", "12/02", "responce time", 2.0));
		
		violations.setViolations(violationsVector);
	} 
    
	@Test
    public void testAPI_correct() 
	{
        //setup a mock server for data movement
		stubFor(post(urlEqualTo(URLdataMovementEnactor))
	            .willReturn(aResponse()
	                .withHeader("Content-Type", "application/x-www-form-urlencoded")
	                .withStatus(200)
	                .withBody("ack")));
		
		//set up connection 
        HttpClient client = HttpClientBuilder.create().build();
        
        
       String address; 
        try 
        {
        	address = InetAddress.getLocalHost().getHostAddress();
		} 
        catch (UnknownHostException e1) 
        {
		
			e1.printStackTrace();
			address= "http://localhost";
		}
        
        HttpPost post = new HttpPost(address + URSDS4M_notifyViolation);
		
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
    public void testAPI_notCorrect() 
	{
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
    public void testAPI_content() 
	{
		
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
	
	
}
