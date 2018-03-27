package it.polimi.deib.ds4m.main.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import it.polimi.deib.ds4m.main.model.Violation;
import it.polimi.deib.ds4m.main.model.Violations;
import it.polimi.deib.ds4m.main.model.applicationRequirement.ApplicationsRequirements;

/**
 * Servlet implementation class NotifyViolation
 */
@WebServlet("/NotifyViolation")
public class NotifyViolation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NotifyViolation() 
    {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.getWriter().println("<h1>notify violation, DS4M is up<h1>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//test
		Gson gsonConverter = new Gson();
		ApplicationsRequirements applicationsRequirements = (ApplicationsRequirements) this.getServletConfig().getServletContext().getAttribute("applicationsRequirements");
		
		System.out.println("Application requirements API: " + gsonConverter.toJson(applicationsRequirements));
		
		//retrieve parameter (the list of violations)
		String violationsJSON = request.getParameter("violations");
		System.out.println("received violations");
		
		Gson g = new Gson();
		
		try 
		{
	        Violations violations = g.fromJson(violationsJSON, Violations.class);
	        Violation violation = violations.getViolations().firstElement();
	        
	        //select data movement

	        
	        
	        //answer to movement enactors
	        HttpClient client = HttpClientBuilder.create().build();
	        HttpPost post = new HttpPost("http://localhost:8089/dataEnactor/action");
	        
	        // Create some NameValuePair for HttpPost parameters
	        List<NameValuePair> arguments = new ArrayList<>(3);
	        arguments.add(new BasicNameValuePair("violations", g.toJson(violations)));
	        try {
	            post.setEntity(new UrlEncodedFormEntity(arguments));
	            HttpResponse responseDE = client.execute(post);//response empty

	            //Print out the response message
	            System.out.println(EntityUtils.toString(responseDE.getEntity()));
	            
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	       
	        //set answer status
	        response.setStatus(HttpStatus.SC_OK);

        
		}
		catch (JsonParseException e)
		{
			response.setStatus(HttpStatus.SC_BAD_REQUEST);	
		}
		
		
	}

}
