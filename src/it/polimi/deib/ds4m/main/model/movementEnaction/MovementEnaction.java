package it.polimi.deib.ds4m.main.model.movementEnaction;

import java.util.Vector;

import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.movement.Transformation;

public class MovementEnaction 
{
	String from;
	String to;
	Vector<String> transformations;
	
	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}
	/**
	 * @return the transformations
	 */
	public Vector<String> getTransformations() {
		return transformations;
	}
	/**
	 * @param transformations the transformations to set
	 */
	public void setTransformations(Vector<String> transformations) {
		this.transformations = transformations;
	}
	
	public void importMovement (Movement movement) 
	{
		this.from = movement.getFrom();
		this.to = movement.getTo();
		

		for(Transformation transformationMovement : movement.getTransformations())
		{
			transformations.addElement(transformationMovement.getType());
		}
	}


}
