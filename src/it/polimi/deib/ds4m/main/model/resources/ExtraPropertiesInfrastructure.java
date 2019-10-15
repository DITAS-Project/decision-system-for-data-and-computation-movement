package it.polimi.deib.ds4m.main.model.resources;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtraPropertiesInfrastructure implements Serializable 
{
	private static final long serialVersionUID = -6493757737293325163L;
	//true if it is the first infrastructure where the VDC is deployed
	private Boolean ditas_default;

	/**
	 * @return the ditas_default
	 */
	public Boolean getDitas_default() {
		return ditas_default;
	}

	/**
	 * @param ditas_default the ditas_default to set
	 */
	public void setDitas_default(Boolean ditas_default) {
		this.ditas_default = ditas_default;
	}

}
