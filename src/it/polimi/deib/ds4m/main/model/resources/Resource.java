package it.polimi.deib.ds4m.main.model.resources;

import java.util.ArrayList;

public class Resource 
{
	private String name;
	private String type;
	private String location;//cloud edge
	private ArrayList<Characteristic> characteristics;
	
	
	
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


}
