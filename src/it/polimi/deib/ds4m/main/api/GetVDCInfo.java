package it.polimi.deib.ds4m.main.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;

/**
 * Servlet implementation class GetVDCInfo
 */
@WebServlet("/v2/GetVDCInfo/*")
public class GetVDCInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetVDCInfo() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{		
		String pathInfo = request.getPathInfo();
		
		if(pathInfo == null || pathInfo.equals("/"))
		{
			response.getWriter().println("no VDC ID specified");
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			response.setContentType("application/json");
			return;
		}
		
		String[] splits = pathInfo.split("/");
		
		if(splits.length != 2) 
		{
			response.getWriter().println("wrong REST path specified");
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			response.setContentType("application/json");
			//response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		String VDCid = splits[1];
		
		//create the json parser
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);//to serialize arrays with only one element
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		
		//retrieve parameter (the list of violations)
		//String VDCid = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		
		//retrieve the list of VDC
		ArrayList<VDC> VDCs = (ArrayList<VDC>) this.getServletConfig().getServletContext().getAttribute("VDCs");
		if (VDCs==null)
		{
			response.getWriter().println("no VDC yet instantiated");
			response.setStatus(HttpStatus.SC_BAD_REQUEST);
			response.setContentType("application/json");
			return;
		}
		
		for (VDC vdc : VDCs)
		{
			if (vdc.getId().equals(VDCid))
			{
				
				response.getWriter().println(mapper.writeValueAsString(vdc.getCurrentInfrastructure().getName()));
				response.setStatus(HttpStatus.SC_OK);
				response.setContentType("application/json");
				return;
				
			}
			
		}
		
		response.getWriter().println("no VDC found");
		response.setStatus(HttpStatus.SC_BAD_REQUEST);
		response.setContentType("application/json");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		
	}

}
