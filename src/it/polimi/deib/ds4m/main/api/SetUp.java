package it.polimi.deib.ds4m.main.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import it.polimi.deib.ds4m.main.model.applicationRequirement.ApplicationRequirements;
import it.polimi.deib.ds4m.main.model.applicationRequirement.VDC;
import it.polimi.deib.ds4m.main.model.dataSources.DataSource;
import it.polimi.deib.ds4m.main.movement.ManageMovementsActions;

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
		//retrieve concrete blueprint
		String concreteBlueprintJSON = request.getParameter("ConcreteBlueprint");
		
		//convert the json in object
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(concreteBlueprintJSON);
		
		//retrieve DATA MANAGEMENT
		JsonNode dataManagement = root.get("DATA_MANAGEMENT");
		ApplicationRequirements applicationRequirements = mapper.treeToValue(dataManagement, ApplicationRequirements.class);
		
		//retrieve data sources
		JsonNode dataSourcesJSON = root.get("INTERNAL_STRUCTURE").get("Data_Sources");
		List<DataSource> dataSources = Arrays.asList(mapper.treeToValue(dataSourcesJSON, DataSource[].class));
		
		//retrieve movement classes
		InputStream inputstream = this.getServletConfig().getServletContext().getResourceAsStream("/WEB-INF/movementClasses.json");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
	    StringBuilder movementsJSON = new StringBuilder();
	    String line;
	    while ((line = reader.readLine()) != null) {
	    	movementsJSON.append(line);
	    }
	    reader.close();
		
	    //instantiate movement classes for each data source 
	    ManageMovementsActions.instantiateMovementActions(dataSources,movementsJSON.toString() );
	    
		//set up vdc
		VDC vdc = new VDC();
		vdc.addApplicationRequirement(applicationRequirements);
		vdc.setDataSources(dataSources);
		vdc.setId("ID");
		
		//if it is not set create a collection of appl.s requirements
		Vector<VDC> VDCs;
		if  (this.getServletConfig().getServletContext().getAttribute("VDCs") == null)
		{
			VDCs = new Vector<VDC>();
			this.getServletConfig().getServletContext().setAttribute("VDCs", VDCs);
		}
		else
			VDCs = (Vector<VDC>) this.getServletConfig().getServletContext().getAttribute("VDCs");
		
		//add the application requirements 
		VDCs.addElement(vdc);
	
	}
	
	static String readFile(String path, Charset encoding) throws IOException 
	{		
		
		System.out.println(System.getProperty("user.dir"));
		
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
