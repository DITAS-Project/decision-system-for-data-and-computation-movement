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
package it.polimi.deib.ds4m.main.model.resources;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Resource 
{
	private String name;
	private String type;
	private String location;//cloud edge
	private ArrayList<Characteristic> characteristics;
	
	@JsonIgnore
	private Boolean isDataSource = false;
	
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the characteristics
	 */
	public ArrayList<Characteristic> getCharacteristics() {
		return characteristics;
	}
	/**
	 * @param characteristics the characteristics to set
	 */
	public void setCharacteristics(ArrayList<Characteristic> characteristics) {
		this.characteristics = characteristics;
	}
	/**
	 * @return the isDataSource
	 */
	public Boolean getIsDataSource() {
		return isDataSource;
	}
	/**
	 * @param isDataSource the isDataSource to set
	 */
	public void setIsDataSource(Boolean isDataSource) {
		this.isDataSource = isDataSource;
	}


}
