package it.polimi.deib.ds4m.main.model.movement;

import java.util.Vector;

public class Movements 
{
	private Vector<Movement> movements;
	
	public Movements(Vector<Movement> movements) 
	{
		this.movements=movements;
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
