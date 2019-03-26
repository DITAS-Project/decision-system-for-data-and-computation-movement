var hooks = require('hooks');
var before = hooks.before;
var after = hooks.after;
//to read from file
var fs = require('fs');


//hooks.beforeAll(function (transactions) {	
//	  hooks.log('setup order tests');
//	  
//	  //copy the transaction array (i don't need a deep copy)
//	  var transactions2_tmp=transactions[0]; 
//	  
//	  transactions[0]=transactions[3];
//	  transactions[3]=transactions[2];
//	  transactions[2]=transactions[1];
//	  transactions[1]=transactions2_tmp;	  
//
//	  //transactions[4]=transactions2[4];
//	  //transactions[5]=transactions2[5];
//	  
//	  
//	});

//beforeAll(function (transactions, done) {
//	  console.log('before all');
//	  done();
//	});


// call the addVDC with the blueprint
before("/AddVDC > add VDC > 200 > application/json", function (transaction) {
	//get the json file to send
	var VDC = fs.readFileSync('./testResources/test_Blueprint_V6_correct.json', 'utf8');
	
	transaction.request.headers['content-type'] = "text";
	transaction.request.body = VDC;
});

//call the notify violations with the same ID of the VDC
before("/NotifyViolation > notifyViolation > 200 > application/json", function (transaction) {
	//get the json file to send
	var violations = fs.readFileSync('./testResources/test_violationCorrect.json', 'utf8');
	
	transaction.request.headers['content-type'] = "application/json";
	transaction.request.body  = violations;
});



