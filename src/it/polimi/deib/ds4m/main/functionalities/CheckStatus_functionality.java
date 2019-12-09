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
package it.polimi.deib.ds4m.main.functionalities;

import java.util.ArrayList;

import it.polimi.deib.ds4m.main.evaluation.VDC_evaluation;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;

/**
 * implementation class CheckStatus
 */
public class CheckStatus_functionality  {
	private static final long serialVersionUID = 1L;
       

	protected void doFunctionality(ArrayList<VDC_evaluation> VDCs)  
	{
		String content = "";
		
		content=content.concat("<h1>DS4M is up and running <h1> <br>");
		content=content.concat("Number of VDCs: ");
		
		//if it is not set create a collection of appl.s requirements
		if  (VDCs == null)
			content=content.concat("0 <br>");
		else
		{
			content=content.concat(VDCs.size()+" <br>");
		}
		
		System.out.println(content);
		
	}


}
