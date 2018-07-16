package it.polimi.deib.ds4m.main.model.resources;

public class Characteristic {
	
    private String type;
    private Double total;
    private Double free;
    private String unit;
    
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
	 * @return the total
	 */
	public Double getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(Double total) {
		this.total = total;
	}
	/**
	 * @return the free
	 */
	public Double getFree() {
		return free;
	}
	/**
	 * @param free the free to set
	 */
	public void setFree(Double free) {
		this.free = free;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

}
