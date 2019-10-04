package it.polimi.deib.ds4m.main.onLoad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import it.polimi.deib.ds4m.main.Utility;
import it.polimi.deib.ds4m.main.model.concreteBlueprint.VDC;
import it.polimi.deib.ds4m.main.movement.VDCManager;

public class BootConfigurator implements ServletContextListener 
{
	//path to concrete blueprints
	//to chenge to
	String pathToBlueprint="/Users/mattia/git/decision-system-for-data-and-computation-movement2/testResources/bootConfiguration";
	
	//path to configuration
	//to change to /etc/ditas
	String pathMovementJson= "/Users/mattia/git/decision-system-for-data-and-computation-movement2/testResources/configuration/DS4M_movementClasses.json";

	public BootConfigurator() {}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {}

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
	 System.out.println("Importing blueprints");
	 

	 //create the array of VDC
	 ArrayList<VDC> VDCs;
	 
	 
	 //read the JSON of movement action classes
	 String movementsJSON=null;
	try {
		BufferedReader movementClassesJSONBR = new BufferedReader(new FileReader(pathMovementJson ));
		movementsJSON= movementClassesJSONBR.lines().collect(Collectors.joining("\n"));
		movementClassesJSONBR.close();
		
	} catch (FileNotFoundException e1) 
	{
		System.out.println("bootConfigurator: error in reading the movement Classes");
		return;
	} catch (IOException e) {
		System.out.println("bootConfigurator: failed closing the BufferedReader for movement classes");
	}

		
	 
	 //read all concrete blueprints and instantiate the vdcs
	 Set<String> listBlueprints;
	 try {
		 listBlueprints=listFilesUsingFileWalk(pathToBlueprint, 1);
		 
		 for (String blueprint: listBlueprints)
		 {
			 String path = pathToBlueprint+"/"+blueprint;
			 System.out.println(path);
			 File blueprintJSONFile = new File(path);
			 
			 BufferedReader blueprintJSONBR = new BufferedReader(new FileReader(blueprintJSONFile ));
			 
			 String blueprintJson= blueprintJSONBR.lines().collect(Collectors.joining("\n"));			  

			 
			 VDC vdc = VDCManager.createVDC(blueprintJson, movementsJSON);
			 
			 blueprintJSONBR.close();
		 }
			 
	} catch (IOException e) 
	 {
		System.out.println("bootConfigurator: problem in reading the concrete blueprints");
	} catch (Exception e) {
		System.out.println("bootConfigurator: problem in generating the VDC: "+ e.getMessage());
	}

	 
	 System.out.println("importing vdc settings and status");

	}
	
	
	/**
	 * source https://www.baeldung.com/java-list-directory-files
	 * returns a list of files
	 * 
	 * @param dir the path of the directory to visit
	 * @param depth the level of folder to inspect, to inspect a folder it must be set to 1
	 * @return
	 * @throws IOException
	 */
	public Set<String> listFilesUsingFileWalk(String dir, int depth) throws IOException {
	    try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
	        return stream
	          .filter(file -> !Files.isDirectory(file))
	          .map(Path::getFileName)
	          .map(Path::toString)
	          .collect(Collectors.toSet());
	    }
	}

}
