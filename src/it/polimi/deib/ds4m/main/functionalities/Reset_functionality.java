package it.polimi.deib.ds4m.main.functionalities;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.configuration.PathSetting;

/**
 * Servlet implementation class Reset
 */
@WebServlet("/v2/Reset")
public class Reset_functionality extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Reset_functionality() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//remove status
		Utility.removeAllFiles(PathSetting.statusSerialize_pv);
		
		//remove blueprints
		Utility.removeAllFiles(PathSetting.blueprints_pv);
		
		
		//remove status saved
		this.getServletConfig().getServletContext().setAttribute("VDCs", null);
		
		response.getWriter().append("DS4M resetted");
	}


}
