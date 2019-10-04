package it.polimi.deib.ds4m.main.onLoad;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BootConfigurator implements ServletContextListener 
{
	String pathToBlueprint="/Users/mattia/git/decision-system-for-data-and-computation-movement2/testResources/bootConfiguration";

	public BootConfigurator() {}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {}

	@Override
	public void contextInitialized(ServletContextEvent arg0) 
	{
	 System.out.println("Importing blueprints");
	 
//	 try (Stream<Path> paths = Files.walk(Paths.get(pathToBlueprint))) {
//		    paths
//		        .filter(Files::isRegularFile)
//		        .forEach(System.out::println);
//		    
//		} catch (IOException e) 
//	 	{
//			System.out.println("error in reading file lists");
//			e.printStackTrace();
//		} 
//	 
	 
	 List<File> filesInFolder=null;
	 try {
		filesInFolder = Files.walk(Paths.get("pathToBlueprint"))
		         .filter(Files::isRegularFile)
		         .map(Path::toFile)
		         .collect(Collectors.toList());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	 for(File blueprint: filesInFolder)
	 {
		 System.out.println(blueprint.getPath());
		 
		 
	 }
	 
	 
	 System.out.println("importing vdc settings and status");

	}

}
