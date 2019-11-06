package it.polimi.deib.ds4m.main.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.configuration.PathSetting;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DAL;
import it.polimi.deib.ds4m.main.model.dataSources.UpdatedDal;
import it.polimi.deib.ds4m.main.movement.VDCManager;

/**
 * Servlet implementation class NotifyDALMoved
 */
@WebServlet("/v2/NotifyDALMoved")
public class NotifyDALMoved extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NotifyDALMoved() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.getWriter().append("GET call not supported to this API");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.getWriter().append("POST call not supported to this API");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		
		//retrieve the list of VDC
		@SuppressWarnings("unchecked")
		ArrayList<VDC> VDCs = (ArrayList<VDC>) this.getServletConfig().getServletContext().getAttribute("VDCs");
		
		String bodyJson = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		
		JsonNode root; 
		try {
			root = mapper.readTree(bodyJson);
		}
		catch (Exception e)
		{
			e.printStackTrace();
        	
        	String message = "NotifyDALMoved: Error in reading the tree of the Blueprint";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
        	response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        	return;
		}
		
		//deserialise received json
		UpdatedDal updatedDal = mapper.treeToValue(root, UpdatedDal.class);
		
		//search for the DAL to be upodated
		DAL dal = VDCManager.searchDALbyID(updatedDal.getDalID(), VDCs);
		
		if (dal ==null)
		{
        	String message = "NotifyDALMoved: no DAL found with the given ID";
        	System.err.println(message);
        	response.getWriter().println(message);
        	
        	response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        	return;
		}
			
		
		dal.setOriginal_ip(updatedDal.getDalNewIP());
		
		//once it is set, call the CME to instantiate the VDC using the moved or duplicated DAL
		
		
		//for all VDCs
		for(VDC vdc: VDCs)
		{
			String call="";
			
			if (vdc.getNextMovement()!=null)
			{
				call = PathSetting.urlComputationMovementEnactor+"/vdc/"+
					vdc.getId()+
					"?sourceInfra="+vdc.getCurrentInfrastructure().getId()+ 
					"&targetInfra="+vdc.getNextMovement().getToLinked().getId();
				
				//i remove the next movement
				vdc.setNextMovement(null);
			}
			else
				call = PathSetting.urlComputationMovementEnactor+"/vdc/"+
						vdc.getId()+
						"?sourceInfra="+vdc.getCurrentInfrastructure().getId()+ 
						"&targetInfra="+vdc.getCurrentInfrastructure().getId();

			System.out.println("NotifyDALMoved: call CME: "+call);
			
	        //call to computation movement enactor
	        HttpClient client = HttpClientBuilder.create().build();
	        
	        //call CME
	        HttpPut httpPut = new HttpPut(call);
	        
	        try {
	            @SuppressWarnings("unused")
				HttpResponse responseDE = client.execute(httpPut);//response empty
	        } catch (IOException e) 
	        {
	            System.out.println("NotifyDALMoved: Computation Movement Enactor not reached");//for test purposes don't stop execution
	        }
			
		}
        

        //set answer status ok
        response.setStatus(HttpStatus.SC_OK);
        response.setContentType("application/json");
		

	}
	
	

}
