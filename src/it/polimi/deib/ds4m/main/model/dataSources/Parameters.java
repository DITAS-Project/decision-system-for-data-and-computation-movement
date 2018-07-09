package it.polimi.deib.ds4m.main.model.dataSources;

import com.fasterxml.jackson.annotation.JsonProperty;
import wiremock.org.apache.commons.lang3.builder.HashCodeBuilder;

public class Parameters {
	
    String hostname;
    String port;
    @JsonProperty("proxy-hostname")
    String proxy_hostname;
    @JsonProperty("proxy-port")
    String proxy_port;
    String username;
    String password;
    
	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}
	/**
	 * @param hostname the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	/**
	 * @return the proxy_hostname
	 */
	public String getProxy_hostname() {
		return proxy_hostname;
	}
	/**
	 * @param proxy_hostname the proxy_hostname to set
	 */
	public void setProxy_hostname(String proxy_hostname) {
		this.proxy_hostname = proxy_hostname;
	}
	/**
	 * @return the proxy_port
	 */
	public String getProxy_port() {
		return proxy_port;
	}
	/**
	 * @param proxy_port the proxy_port to set
	 */
	public void setProxy_port(String proxy_port) {
		this.proxy_port = proxy_port;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		//standard behavior of equals 
	    if (obj == null) {
	        return false;
	    }
	    
	    if (!Parameters.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    
	    //check all the fields
	    final Parameters other = (Parameters) obj;	    
	    if (!this.hostname.equals(other.getHostname()) ) {
	        return false;
	    }
	    
	    if (!this.port.equals(other.getPort()) ) {
	        return false;
	    }
	    
	    if (!this.proxy_hostname.equals(other.getProxy_hostname()) ) {
	        return false;
	    }
	    
	    if (!this.proxy_port.equals(other.getProxy_port()) ) {
	        return false;
	    }
	    
	    if (!this.username.equals(other.getUsername()) ) {
	        return false;
	    }
	    
	    if (!this.password.equals(other.getPassword()) ) {
	        return false;
	    }
	    
	    
	    
	    return true;
	}
	
	//Whenever equals is modified, also hasCode has to be modified
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            // if deriving: appendSuper(super.hashCode()).
            append(hostname).
            append(port).
            append(proxy_hostname).
            append(proxy_port).
            append(username).
            append(password).
            toHashCode();
    }	
    
}
