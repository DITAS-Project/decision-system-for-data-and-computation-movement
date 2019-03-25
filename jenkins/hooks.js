var hooks = require('hooks');
var before = hooks.before;
var after = hooks.after;
//to read from file
var fs = require('fs');


beforeAll(function (transactions) {
	  hooks.log('setup order tests');
	  
	  transactions.push(clone(transactions[0]));
	  
	  //copy the transaction array (i don't need a deep copy)
	  var transactions2=transactions.slice(); 
	  
	  transactions[0]=transactions2[3];
	  transactions[1]=transactions2[0];
	  transactions[2]=transactions2[1];
	  transactions[3]=transactions2[2];
	  transactions[4]=transactions2[4];
	  transactions[5]=transactions2[5];
	  
	  
	});


// call the addVDC with the blueprint
before("/AddVDC > add VDC > 200 > application/json", function (transaction) {
	//get the json file to send
	var VDC = fs.readFileSync('./testResources/example_V5_complete_withResourcesMoved.json', 'utf8');
	
	transaction.request.headers['content-type'] = "text";
	transaction.request.body = VDC;
});

//call the notify violations with the same ID of the VDC
before("/NotifyViolation > notifyViolation > 200 > application/json", function (transaction) {
	//get the json file to send
	var violations = fs.readFileSync('./testResources/test_violation.json', 'utf8');
	
	transaction.request.headers['content-type'] = "application/json";
	transaction.request.body  = violations;
});



