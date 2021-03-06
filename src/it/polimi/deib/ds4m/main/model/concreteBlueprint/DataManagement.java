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

import java.io.Serializable;

public class DataManagement implements Serializable
{
	private static final long serialVersionUID = -6877662954239747722L;
	
	String method_id;
	Attributes attributes;
	/**
	 * @return the method_id
	 */
	public String getMethod_id() {
		return method_id;
	}
	/**
	 * @param method_id the method_id to set
	 */
	public void setMethod_id(String method_id) {
		this.method_id = method_id;
	}
	/**
	 * @return the attributes
	 */
	public Attributes getAttributes() {
		return attributes;
	}
	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}



}
