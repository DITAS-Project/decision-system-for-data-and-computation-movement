# Decision System for Data and Computation Movement
The goal of the component consists in selecting the best data and computation movement actions to be adopted in order to restore the level of data quality, quality of service and security defined by the application developer.

## Input
* application developer requirements
* list of violated requirements

## Output
* list of data and computation actions

## list of functionalities
* notifyViolation
  * description: the method receives in input the appllication developer's requirements and the list of violated requirements.
  * caller SLA manager
  * input
    * application developer requirements
    * list of violated requirements
  * output
    * list of data and computation movement actions 
  

## Implementation language
Node.js

## Requirements
In order to work, this component requires the following modules to be installed:

* express
* body-parser

## Execution
To launch this component, execute the following command:
* node main.js [port]

[port] specifies the HTTP port number where the component will listen to requests. If not specified, default value 8081 will be used.
