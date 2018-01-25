var express = require('express');
var app = express();
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
app.post('/api/notifyViolation', function (req, res) {
    
	//parse and extract non-functional properties from requirements
	var properties = JSON.parse(req.body.requirements.toString()).userRequirements.NonFunctionalRequirements.properties;
    
    //parse and extract goal model tree from requirements
	var goalTree = JSON.parse(req.body.requirements.toString()).userRequirements.NonFunctionalRequirements.goalTree;
    
	//parse list of service blueprints
	var violations = JSON.parse(req.body.violations.toString());
    
	var result = [];
	for (var violation in violations){
		//verify if current blueprint satisfies non-functional requirements
		if (checkNode(violations[violation], properties, goalTree)) {
			console.log(violations[violation] + 'selected');
			result.push(violations[violation]);
		} else {
			console.log(violations[violation] + 'discarded');
		}
	}
    res.end(result.toString());
})

function checkNode(violation, properties, node) {
	console.log(node);
	//current node is not a leaf
	if (node.children != undefined) {
		var ret = false;
		for (var child in node.children){
			//recursively invoke function for each child nodes
			ret = checkNode(blueprint, properties, node.children[child]);
			//when current node type is OR, the first child node that is satisfied also satisfies the current node
			if (ret && node.type == 'OR'){
				return true;
			//when current node type is AND, the first child node that is not satisfied makes the current node also not satisfied 
			} else if (!ret && node.type == 'AND'){
				return false;
			}
		}
		//return result to parent node
		return ret;
	//current node is a leaf
	} else {
		for (var property in properties){
			//verify if referenced property exists
			if (properties[property].ID == node) {
				//assess if current blueprint satisfies the property
				return assessProperty(blueprint, properties[property]);
			}
		}
		//referenced property does not exist, node is not satisfied
		return false;
	}
}

function assessProperty(blueprint, property){
	console.log(blueprint);
	return true;
}

