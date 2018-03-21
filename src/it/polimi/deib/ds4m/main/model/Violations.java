package it.polimi.deib.ds4m.main.model;

import java.util.Vector;

public class Violations 
{	
	//define a set of violations
	private Vector<Violation> violations;
	
	public Violations() 
	{
		
		violations = new Vector<Violation>();
		
	}



	/**
	 * @return the violations
	 */
	public Vector<Violation> getViolations() {
		return violations;
	}



	/**
	 * @param violations the violations to set
	 */
	public void setViolations(Vector<Violation> violations) {
		this.violations = violations;
	}

}
