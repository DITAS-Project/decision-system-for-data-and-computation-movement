# Decision System for Data and Computation Movement
The goal of the component consists in selecting the best data and computation movement actions to be adopted in order to restore the level of data quality, quality of service and security defined by the application developer.

## Input
* Concrete blueprint
* List of violated requirements

## Output
* List of data and computation actions

## List of functionalities
* /api/NotifyViolation
  * description: the method receives in input a list of violations.
  * caller SLA manager
  * input
    * list of violated requirements
  * output
    * list of data and computation movement actions
    
* /api/SetUP
  * description: the method receives in input the concrete blueprint of the first VDC deployed.
  * caller deployment module
  * input
    * concrete blueprint
  * output
    * none
     
* /api/AddVDC
  * description: the method receives in input the concrete blueprint of the VDC deployed.
  * caller deployment module
  * input
    * concrete blueprint
  * output
    * none

## Implementation language
Java

## Installation
In order to deploy the DS4M it is not mnandatory to compile the code since an already compiled version of the software is already provided in /target/ROOT.war. If you don't want to compile teh source code skip step _Compilation_.

### Requirements for deployment
In order to be executed, this component requires the following elements to be installed:
* JRE 8
* Apache Tomcat 8.5

### Requirements for copiling the code
* JRE 8
* Maven

### Compilation
* Download the source code from this repository
* Copile it using maven: with a shell navigate to the main folder and use the command  _mvn package_

### Deployment
* Move the war file _/target/ROOT.war_ just created in the _webapps_ folder of Apache Tomcat
* Start Apache Tomcat

### Check deployment
To test if the application is deployed correctly:
* open a browser 
* go to __address__:__port__/NotifyViolation
* a web page saying that the service is up and running should be displayed.

__address__: address of the machine where tomcat is installed

__port__: port of the application, if no port forwarding has been specified, it is 8080

## License
The  project is under Apache 2.0 license. You can find the license [here](https://github.com/DITAS-Project/decision-system-for-data-and-computation-movement/blob/master/LICENSE).

## Acknowledge 
This is being developed for the DITAS Project: https://www.ditas-project.eu/
