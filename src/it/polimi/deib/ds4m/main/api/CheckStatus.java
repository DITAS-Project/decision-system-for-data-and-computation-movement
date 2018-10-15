/**
 * Copyright 2018 Politecnico di Milano
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * This is being developed for the DITAS Project: https://www.ditas-project.eu/
 */
package it.polimi.deib.ds4m.main.api;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;

/**
 * Servlet implementation class CheckStatus
 */
@WebServlet("/CheckStatus")
public class CheckStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String content = "";
		
		content=content.concat("<h1>DS4M is up and running <h1> <br>");
		content=content.concat("Number of VDCs: ");
		
		//if it is not set create a collection of appl.s requirements
		ArrayList<VDC> vdcs;
		if  (this.getServletConfig().getServletContext().getAttribute("VDCs") == null)
			content=content.concat("0 <br>");
		else
		{
			vdcs = (ArrayList<VDC>) this.getServletConfig().getServletContext().getAttribute("VDCs");
			content=content.concat(vdcs.size()+" <br>");
		}
		
		System.out.println(content);
		
		response.getWriter().println(content);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
