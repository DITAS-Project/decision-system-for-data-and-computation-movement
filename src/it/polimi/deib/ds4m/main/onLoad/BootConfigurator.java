package it.polimi.deib.ds4m.main.onLoad;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BootConfigurator implements ServletContextListener {

	public BootConfigurator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
	 System.out.println("Importing blueprints");
	 
	 
	 System.out.println("importing vdc settings and status");

	}

}
