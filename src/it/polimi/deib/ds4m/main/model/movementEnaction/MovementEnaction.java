package it.polimi.deib.ds4m.main.model.movementEnaction;

import java.util.ArrayList;

import it.polimi.deib.ds4m.main.model.movement.Movement;
import it.polimi.deib.ds4m.main.model.movement.Transformation;

public class MovementEnaction 
{
	String from;
	String to;
	ArrayList<String> transformations;
	
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
	public ArrayList<String> getTransformations() {
		return transformations;
	}
	/**
	 * @param transformations the transformations to set
	 */
	public void setTransformations(ArrayList<String> transformations) {
		this.transformations = transformations;
	}
	
	public void importMovement (Movement movement) 
	{
		this.from = movement.getFromLinked().getName();
		this.to = movement.getToLinked().getName();
		
		
		if (transformations==null)
			transformations = new ArrayList<String>();
		
		if (movement.getTransformations()!=null)	
			for(Transformation transformationMovement : movement.getTransformations())
			{
				transformations.add(transformationMovement.getType());
			}
	}


}
