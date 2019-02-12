var hooks = require('hooks');
var before = hooks.before;
var after = hooks.after;

var responseStash = {};


// call the addVDC with the blueprint
before("/AddVDC > POST > 200", function (transaction) {
	transaction.request.headers['content-type'] = "application/json"
	transaction.request.body = ""	

  
});

//call the notify violations with the same ID of the VDC
before("/NotifyViolation > POST > 200", function (transaction) {
	
	transaction.request.headers['content-type'] = "application/json"
	transaction.request.headers['Violations'] = "[ { \"vdcId\": \"1\", \"methodId\": \"getAllValuesForBloodTestComponent\", \"metrics\": [ { \"key\": \"availability\", \"value\": 90, \"datetime\": \"2018-08-07T10:50:23.674337517+02:00\"}]}]"
	transaction.request.body = ""
});

