package it.polimi.deib.ds4m.main.onLoad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.configuration.PathSetting;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.movement.VDCManager;

public class BootConfigurator implements ServletContextListener {
	// path to concrete blueprints
	// to chenge to
	// String
	// pathToBlueprint="/Users/mattia/git/decision-system-for-data-and-computation-movement2/testResources/bootConfiguration";

	public BootConfigurator() {
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		if ((new File(PathSetting.statusSerializeSer)).exists())
		{
			System.out.println("BootConfigurator: importing vdc settings and status");
			try
	        {    
	            // Reading the object from a file 
	            FileInputStream file = new FileInputStream(PathSetting.statusSerializeSer); 
	            ObjectInputStream in = new ObjectInputStream(file); 
	              
	            // Method for deserialization of object 
	            ArrayList<VDC> VDCs = (ArrayList<VDC>)in.readObject(); 
	              
	            //set the list of loaded VDCs in the servlet context
	            arg0.getServletContext().setAttribute("VDCs", VDCs);
	            
	            in.close(); 
	            file.close(); 

	        } 
	        catch(IOException ex) 
	        { 
	            System.out.println("IOException is caught"); 
	        } 
	          
	        catch(ClassNotFoundException ex) 
	        { 
	            System.out.println("ClassNotFoundException is caught"); 
	        } 
		} 
		//check if the persistent volume folder exists. if not, it is not mounted (it is a junit test execution) and skip the save
		else if ((new File(PathSetting.pv)).exists())
		{
			System.out.println("BootConfigurator: status file does not exists, importing concrete blueprints");
	
			// create the array of VDC
			ArrayList<VDC> VDCs= new ArrayList<VDC>();
	
			// read the JSON of movement action classes
			//check if the persistent volume folder exists. if not, it is not mounted (it is a junit test execution) and skip the save
			String movementsJSON=null;
			if ((new File(PathSetting.movementClassJson)).exists())
			{
				try {
					BufferedReader movementClassesJSONBR = new BufferedReader(new FileReader(PathSetting.movementClassJson));
					movementsJSON = movementClassesJSONBR.lines().collect(Collectors.joining("\n"));
					movementClassesJSONBR.close();
		
				} catch (FileNotFoundException e1) {
					System.err.println("BootConfigurator: error in reading the movement Classes");
					return;
				} catch (IOException e) {
					System.err.println("BootConfigurator: failed closing the BufferedReader for movement classes");
				}
			}
			else//if the volume is not mounted, i skip the boot load
			{
				System.err.println("BootConfigurator: folder "+PathSetting.pv+" does not exists, concrete blueprints not loaded");
				return;
			}
	
			// read all concrete blueprints and instantiate the vdcs
			Set<String> listBlueprints;
			int blueprintsCount=0;
			
			try {
				
				listBlueprints = Utility.listFilesUsingFileWalk(PathSetting.blueprints_pv, 1);
	
				for (String blueprint : listBlueprints) {
					String concreteBlueprintJSON = VDCManager.loadConcreteBlueprint(blueprint);
					VDC vdc = VDCManager.createVDC(concreteBlueprintJSON, movementsJSON);
					vdc.setId(blueprint.substring(0,blueprint.lastIndexOf(".")));
					VDCs.add(vdc);
					blueprintsCount++;
				}
				
	            //set the list of loaded VDCs in the servlet context
	            arg0.getServletContext().setAttribute("VDCs", VDCs);
	
			} catch (IOException e) {
				System.out.println("BootConfigurator: problem in reading the concrete blueprints: " + e.getMessage());
			} catch (Exception e) {
				System.out.println("BootConfigurator: problem in generating the VDC: " + e.getMessage());
			}
			
			System.out.println("BootConfigurator: imported " + blueprintsCount + " blueprints");
		}
		else
		{
			System.err.println("BootConfigurator: folder "+PathSetting.pv+" does not exists, concrete blueprints not loaded");
		}


	}

}
