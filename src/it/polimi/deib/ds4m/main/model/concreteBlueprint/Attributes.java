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
package it.polimi.deib.ds4m.main.model.concreteBlueprint;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes 
{
	ArrayList<Attribute> dataUtility;
	//ArrayList<Attribute> security;//TODO fix array
	//ArrayList<Attribute> privacy;//commented because the value of attribute is a list
	
	/**
	 * @return the dataUtility
	 */
	public ArrayList<Attribute> getDataUtility() {
		return dataUtility;
	}
	/**
	 * @param dataUtility the dataUtility to set
	 */
	public void setDataUtility(ArrayList<Attribute> dataUtility) {
		this.dataUtility = dataUtility;
	}
//	/**
//	 * @return the security
//	 */
//	public ArrayList<Attribute> getSecurity() {
//		return security;
//	}
//	/**
//	 * @param security the security to set
//	 */
//	public void setSecurity(ArrayList<Attribute> security) {
//		this.security = security;
//	}
//	/**
//	 * @return the privacy
//	 */
//	public ArrayList<Attribute> getPrivacy() {
//		return privacy;
//	}
//	/**
//	 * @param privacy the privacy to set
//	 */
//	public void setPrivacy(ArrayList<Attribute> privacy) {
//		this.privacy = privacy;
//	}

}
