package it.polimi.deib.ds4m.main.configuration;

/**
 * @author Mattia Salnitri
 * 
 * this class stores all paths 
 * for persistent volume folders a "_pv" is added at the end of the variable
 * for files the type is added after the name of the file 
 *  for folders the path must end with a slash
 *
 */
public class PathSetting 
{
	
	//***Paths
	//questa e' assluta, mettere controllo per test, se non funziona allora passare a webinf
	//path to cmovement class file
	public final static String movementClassJson= "/etc/ditas/DS4M_movementClasses.json";
	//public final static String movementClassJson= "/Users/mattia/git/decision-system-for-data-and-computation-movement2/testResources/configuration/DS4M_movementClasses.json";
	public final static String movementClassWEBINFJson= "/WEB-INF/DS4M_movementClasses.json";
	//path to folder that contains serialized status 
	public final static String statusSerializeSer= "/var/ditas/vdm/DS4M/status/status.ser";
	
	
	//path to shared, persistent volume volume
	public final static String pv= "/var/ditas/vdm/";
	
	//path to shared, persistent volume volume DS4M folder
	public final static String DS4M_pv= "/var/ditas/vdm/DS4M/";
	
	//path to concrete blueprint folder
	public final static String blueprints_pv= "/var/ditas/vdm/DS4M/blueprints/";
	//public final static String blueprints_pv="/Users/mattia/git/decision-system-for-data-and-computation-movement2/testResources/pvTest/DS4M/blueprints";
	
	//path to folder that contains serialized status 
	public final static String statusSerialize_pv= "/var/ditas/vdm/DS4M/status/";
	
	
	//***URLS 
	public final static String urlDS4M_notifyViolation = "http://localhost:8080/ROOT/v2/NotifyViolation";
	public final static String urlDS4M_addVDC = "http://localhost:8080/ROOT/v2/AddVDC";
	//public final static String URLSDS4M_notifyViolation = "http://31.171.247.162:50003/NotifyViolation";
	//public final static String URLSDS4M_setUp = "http://31.171.247.162:50003/SetUp";
	public final static String urlDS4M_RemoveVDC = "http://localhost:8080/ROOT/v2/RemoveVDC";
	public final static String urlDS4M_RemoveAllVDC = "http://localhost:8080/ROOT/v2/RemoveAllVDC";
	public final static String urlDS4M_GetVDCInfo = "http://localhost:8080/ROOT/v2/GetVDCInfo";
	
	//*URLS for tests
	//paths to blueprint and violations
	public final static String pathCorrectBlueprint="./testResources/test_Blueprint_V8.json"; 
	public final static String pathCorrectViolations="./testResources/test_violationCorrect.json";
	public final static String pathCorrectMovementActions="./testResources/configuration/DS4M_movementClasses.json";
	
    //paths to current files
	//private String pathCurrentBlueprint="./testResources/test_Blueprint_V6_correct.json";
	public final static String pathCurrentBlueprint="./testResources/test_Blueprint_V8.json";
	public final static String pathViolationCurrent="./testResources/test_violationCorrect.json";
	
	//*URL components
	public final static String urlDataMovementEnactor = "/dataEnactor/action";
	public final static String urlComputationMovementEnactor = "http://localhost:8085";
	public final static String urlDAL = "178.22.71.88";
	//public static final String urlDA_Resources = "http://178.22.69.180:8080/data-analytics/resources/cloudsigma-deployment/usage/";
	public static final String urlDA_Resources = "http://localhost:30006/resources/";
	
	

}
