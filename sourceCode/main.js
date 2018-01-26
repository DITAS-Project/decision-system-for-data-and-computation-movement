var express = require('express');
var app = express();
//directly load files (i.e, the home page)
app.use(express.static(__dirname));
var bodyParser = require('body-parser');

var port = 8081;
if(process.argv.length > 2) {
	//HTTP port can be specified as application parameter. Otherwise, port 8081 is used.
	var inputPort = process.argv[2];
	if(inputPort > 8000 && inputPort < 65536){
		port = inputPort;
	}
	else {
		console.log("Invalid port value, default 8081 used instead.");
	}
}

var server = app.listen(port, function () {
	console.log("Decision System for Data and Computation Movement listening on port "+port);
})

//required to parse JSON requests
app.use(bodyParser.urlencoded({
    extended: true
}));



//REST service
app.post('/api/notifyViolation', function (request, response) {
    
	//parse and extract non-functional properties from requirements
	var properties = JSON.parse(request.body.requirements.toString()).userRequirements.NonFunctionalRequirements.properties;
    
    //parse and extract goal model tree from requirements
	var goalTree = JSON.parse(request.body.requirements.toString()).userRequirements.NonFunctionalRequirements.goalTree;
    
	//parse list of service blueprints
	var violations = request.body.violations.toString();
    

    response.end("Move from cloud to edge");
})


