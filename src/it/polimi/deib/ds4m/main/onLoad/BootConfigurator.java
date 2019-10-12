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

		System.out.println("bootConfigurator: Importing concrete blueprints");

		// create the array of VDC
		ArrayList<VDC> VDCs;

		// read the JSON of movement action classes
		String movementsJSON = null;
		try {
			BufferedReader movementClassesJSONBR = new BufferedReader(new FileReader(PathSetting.movementClassJson));
			movementsJSON = movementClassesJSONBR.lines().collect(Collectors.joining("\n"));
			movementClassesJSONBR.close();

		} catch (FileNotFoundException e1) {
			System.err.println("bootConfigurator: error in reading the movement Classes");
			return;
		} catch (IOException e) {
			System.err.println("bootConfigurator: failed closing the BufferedReader for movement classes");
		}

		// read all concrete blueprints and instantiate the vdcs
		Set<String> listBlueprints;
		int blueprintsCount=0;
		
		try {
			listBlueprints = Utility.listFilesUsingFileWalk(PathSetting.blueprints_pv, 1);

			for (String blueprint : listBlueprints) {
				String path = PathSetting.blueprints_pv + "/" + blueprint;
				System.out.println(path);
				File blueprintJSONFile = new File(path);

				BufferedReader blueprintJSONBR = new BufferedReader(new FileReader(blueprintJSONFile));

				String blueprintJson = blueprintJSONBR.lines().collect(Collectors.joining("\n"));

				VDC vdc = VDCManager.createVDC(blueprintJson, movementsJSON);

				blueprintJSONBR.close();
				blueprintsCount++;
			}

		} catch (IOException e) {
			System.out.println("bootConfigurator: problem in reading the concrete blueprints");
		} catch (Exception e) {
			System.out.println("bootConfigurator: problem in generating the VDC: " + e.getMessage());
		}
		
		System.out.println("imported " + blueprintsCount + " blueprints");

		System.out.println("importing vdc settings and status");

	}

}
