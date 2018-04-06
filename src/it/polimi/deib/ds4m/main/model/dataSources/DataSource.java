package it.polimi.deib.ds4m.main.model.dataSources;

import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.polimi.deib.ds4m.main.model.movement.Movement;

public class DataSource 
{
	private String name;
	private String type;
	private Parameters parameters;
    
    @JsonIgnore
    private Vector<Movement> movements;
    
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
	 * @return the parameters
	 */
	public Parameters getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	/**
	 * @return the movements
	 */
	public Vector<Movement> getMovements() {
		return movements;
	}
	/**
	 * @param movements the movements to set
	 */
	public void setMovements(Vector<Movement> movements) {
		this.movements = movements;
	}

}
