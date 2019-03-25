var hooks = require('hooks');
var before = hooks.before;
var after = hooks.after;
//to read from file
var fs = require('fs');



// call the addVDC with the blueprint
before("/AddVDC > add VDC > 200 > application/json", function (transaction) {
	//get the json file to send
	var VDC = fs.readFileSync('../testResources/example_V5_complete_withResourcesMoved.json', 'utf8');
	
	transaction.request.headers['content-type'] = "text";
	transaction.request.body = VDC;
});

//call the notify violations with the same ID of the VDC
before("/NotifyViolation > notifyViolation > 200 > application/json", function (transaction) {
	//get the json file to send
	var violations = fs.readFileSync('../testResources/test_violation.json', 'utf8');
	
	transaction.request.headers['content-type'] = "application/json";
	//transaction.request.headers['Violations'] = "[ { \"vdcId\": \"1\", \"methodId\": \"getAllValuesForBloodTestComponent\", \"metrics\": [ { \"key\": \"availability\", \"value\": 90, \"datetime\": \"2018-08-07T10:50:23.674337517+02:00\"}]}]"
	transaction.request.body  = violations;
});



