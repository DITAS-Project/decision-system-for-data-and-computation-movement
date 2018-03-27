package it.polimi.deib.ds4m.main.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

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
		String applicationRequirementsJSON = request.getParameter("applicationRequirements");
		
		//coonvert the json in object
		Gson g = new Gson();
		ApplicationRequirements applicationRequirements = g.fromJson(applicationRequirementsJSON, ApplicationRequirements.class);
		
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
