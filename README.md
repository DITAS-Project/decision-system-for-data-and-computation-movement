# Decision System for Data and Computation Movement
The goal of the component consists in selecting the best data and computation movement actions to be adopted in order to restore the level of data quality, quality of service and security defined by the application developer.

## Input
* Concrete blueprint (Goal model)
* List of violated requirements

## Output
* List of data and computation actions (adaptation actions)

## List of functionalities
* /v2/CheckStatus
  * description: the api shows the number of active VDCs (goal models)
  * HTTP call: GET
  * input
    * list of violated requirements
  * output
    * HTTP code 200 if goal model correcly parsed

* /v2/NotifyViolation
  * description: the method receives in input a list of violations.
  * HTTP call: POST
  * input
    * list of violated requirements
  * output
    * HTTP code 200 if goal model correcly parsed
    * HTTP code 500 if problems occurred
     
* /v2/AddVDC   
  * description: the method receives in input the concrete blueprint of the VDC deployed.
  * HTTP call: POST
  * input
    * concrete blueprint
  * output
    * HTTP code 200 if goal model correcly parsed
    * HTTP code 500 if problems occurred

* /v2/Reset
  * description: the method reset the internal statius of the software
  * HTTP call: GET
  * input
    * empty
  * output
    * HTTP code 200 
    
## Implementation language
Java

## Installation
In order to deploy the DS4M it is not mandatory to compile the code since an already compiled version of the software is already provided in /target/ROOT.war. If you don't want to compile the source code, skip step _Compilation_.

### Requirements for deployment
In order to be executed, this component requires the following elements to be installed:
* JRE 8
* Apache Tomcat 8.5

### Requirements for copiling the code
* JRE 8
* Maven

### Compilation
* install Apache Tomcat 
* Download the source code from this repository
* Copile it using maven: 
 * with a shell navigate to the main folder and use the command  _mvn package_
* compile using eclipse
 * import the prioject usinh eclipse (oxygen, with pligin for maven)
 * right click on the main project and run _maven install_ 

### Deployment
* the file war file _/target/ROOT.war_ creted with maven should be moved to the _webapps_ folder of Apache Tomcat, if it is not the case, move it manually
* Start Apache Tomcat

### Check deployment
To test if the application is deployed correctly:
* open a browser 
* go to __address__:__port__/NotifyViolation
* a web page saying that the service is up and running should be displayed.

__address__: address of the machine where tomcat is installed (_localhost_,depending on the settings, you may need to add _/ROOT_ at the end fo the address

__port__: port of the application, if no port forwarding has been specified, it is 8080

## Deployment with Doker
It is possible to find a doker container boundled with the last version of DS4M ar https://hub.docker.com/r/ditas/decision-system-for-data-and-computation-movement.
In this case, you need [docker](https://www.docker.com) to be installed in your computer. 
Once docker is installed, run on your comman line
```
docker run -d \
  --name DS4M \
  -v <path to a local folder containing configuration file>:/etc/ditas/ \
  -v <path to a empty local folder>:/var/ditas/vdm/ \
  ditas/decision-system-for-data-and-computation-movement:production
```

This will create a running container listening on port 8080.
DS4M saves the status of the application and read configuration in two permanet folders. 
```<path to a local folder containing configuration file>``` must contain a file ```DS4M_movementClasses.json``` that contains the descriprtion on adaptation action classes. an example of this file can bve found [here](https://github.com/DITAS-Project/VDC-Shared-Config/blob/master/vdm/DS4M_movementClasses.json) 
```<path to a empty local folder>:/var/ditas/vdm/``` must contain an empty folder ```DS4M```. If this folder is not provided the DS4M will still work, but is a completely stateless mode (i.e. it will not save nor the status of the client nor the status of the VDCs ( goal models) provided).


## Test DS4M
To test DS4M you can use the files in /testResources, or run the Junit tests.


## License
The  project is under Apache 2.0 license. You can find the license [here](https://github.com/DITAS-Project/decision-system-for-data-and-computation-movement/blob/master/LICENSE).

## Acknowledge 
This is being developed for the DITAS Project: https://www.ditas-project.eu/
