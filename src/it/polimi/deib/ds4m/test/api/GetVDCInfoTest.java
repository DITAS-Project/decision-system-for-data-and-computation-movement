package it.polimi.deib.ds4m.test.api;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.configuration.PathSetting;


public class GetVDCInfoTest 
{
	//set mockup server
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
	
	ObjectMapper mapper;
	
	/**
	 * this class thest the correct function of the API
	 * ATTENTION:to be executed corrctly, the onlyCurrentBlueprint parameter in NotifyViolationTest must be set to FALSE
	 */
	@Test
	public void testNotify_correct()
	{
		//set up connection to ds4m, tonget info
        HttpClient client = HttpClientBuilder.create().build();    
        HttpPost post = new HttpPost(PathSetting.urlDS4M_GetVDCInfo+"/VDC_2");
        

        //connect to service
        try {
        	
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
	
	
    @AfterClass
	public static void cleanUp() 
	{
    	// read all concrete blueprints and instantiate the vdcs
 		Set<String> listBlueprints;
 		
 		try {
 			listBlueprints = Utility.listFilesUsingFileWalk(PathSetting.blueprints_pv, 1);

 			for (String blueprint : listBlueprints) {
 				String path = PathSetting.blueprints_pv + blueprint;
 				Files.deleteIfExists(Paths.get(path));
 			}

 		} catch (IOException e) {
 			System.out.println("GetVDCInfoTest: problem in reading the concrete blueprints");
 		} catch (Exception e) {
 			System.out.println("GetVDCInfoTest: problem in generating the VDC: " + e.getMessage());
 		}
    	
	}

}
