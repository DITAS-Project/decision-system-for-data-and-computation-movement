# Decision System for Data and Computation Movement
The goal of the component consists in selecting the best data and computation movement actions to be adopted in order to restore the level of data quality, quality of service and security defined by the application developer.

## Input
* application developer requirements
* list of violated requirements

## Output
* list of data and computation actions

## List of functionalities
* /api/notifyViolation
  * description: the method receives in input a list of violations.
  * caller SLA manager
  * input
    * list of violated requirements
  * output
    * list of data and computation movement actions
    
* /api/setUP
  * description: the method receives in input the concrete blueprint of the first VDC deployed.
  * caller deployment module
  * input
    * concrete blueprint
  * output
    * none
     
* /api/addVDC
  * description: the method receives in input the concrete blueprint of the VDC deployed.
  * caller deployment module
  * input
    * concrete blueprint
  * output
    * none
  
## API definition
API definition in [SwaggerHub](https://app.swaggerhub.com/apis/ditas-project/DecisionSystemForDataAndComputationMovement/0.0.1).

## Implementation language
Java

## Requirements
In order to work, this component requires the following elements to be installed:

* jre 8
* tomcat 8.5

## Execution
To launch this component, execute the following command:
* copy the file decision-system-for-data-and-computation-movement/DS4M/target/ROOT.war in /usr/local/tomcat/webapps/. of the machine where tomcat is installed
* start tomcat

## check deployment
To test if the application is deployed correctly:
* open a browser 
* go to __address__:__port__/NotifyViolation
* a web page saying that the service is up and running should be displayed.

__address__: address of the machine where tomcat is installed

__port__: port of the application, if no port forwarding has been specified, it is 8080
