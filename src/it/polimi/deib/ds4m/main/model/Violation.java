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
package it.polimi.deib.ds4m.main.model;

import java.util.ArrayList;

public class Violation 
{
	
	public ArrayList<Metric> metrics;
	public String methodId;
	public String vdcId;
	

	/**
	 * @return the methodID
	 */
	public String getMethodId() {
		return methodId;
	}
	/**
	 * @param methodID the methodID to set
	 */
	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}
	/**
	 * @return the vdcID
	 */
	public String getVdcId() {
		return vdcId;
	}
	/**
	 * @param vdcID the vdcID to set
	 */
	public void setVdcId(String vdcId) {
		this.vdcId = vdcId;
	}
	/**
	 * @return the metrics
	 */
	public ArrayList<Metric> getMetrics() {
		return metrics;
	}
	/**
	 * @param metrics the metrics to set
	 */
	public void setMetrics(ArrayList<Metric> metrics) {
		this.metrics = metrics;
	}


}
