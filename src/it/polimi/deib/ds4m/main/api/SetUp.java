package it.polimi.deib.ds4m.main.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.model.applicationRequirement.ApplicationRequirements;
import it.polimi.deib.ds4m.main.model.applicationRequirement.ApplicationsRequirements;

/**
 * Servlet implementation class SetUp
 */
@WebServlet("/SetUp")
public class SetUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetUp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().println("SetUp, Use POST method");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//estract application requirements
		//System.out.println("received application requirements"); 
		String applicationRequirementsJSON = request.getParameter("ConcreteBlueprint");
		
		//coonvert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(applicationRequirementsJSON);
		JsonNode dataManagement = root.get("DATA_MANAGEMENT");
		 System.out.println(dataManagement.toString());
		
		ApplicationRequirements applicationRequirements = mapper.treeToValue(dataManagement, ApplicationRequirements.class);
		
		//ApplicationRequirements applicationRequirements = mapper.readValue(applicationRequirementsJSON,
	    //        new TypeReference<ApplicationRequirements>() {});
		
		
		//if it is not set create a collection of appl.s requirements
		ApplicationsRequirements applicationsRequirements;
		if  (this.getServletConfig().getServletContext().getAttribute("applicationRequirements") == null)
		{
			applicationsRequirements = new ApplicationsRequirements();
			this.getServletConfig().getServletContext().setAttribute("applicationsRequirements", applicationsRequirements);
		}
		else
			applicationsRequirements = (ApplicationsRequirements) this.getServletConfig().getServletContext().getAttribute("applicationRequirements");
		
		//add the application requirements 
		applicationsRequirements.addApplicationRequirement(applicationRequirements);
		
		
	
	}

}
