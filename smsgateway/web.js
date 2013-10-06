var express = require("express");
var request = require('request');

var app = express();
app.use(express.logger());
app.use(express.bodyParser());


var data = [];
var patientData = {
	"123": {"name":"Bob","age":23,"gender":"M"},
	"133": {"name":"Alice","age":33,"gender":"F"},
	"143": {"name":"Charlie","age":43,"gender":"M"}
};

var fieldWorkers = {
	"+14084218783":{"location":"Stanford, CA"},
	"+19192603435":{"location":"Mountain View, CA"},
	"+13129650602":{"location":"Stanford, CA"},
	"+19196278855":{"location":"Mountain View, CA"},
	"+15125246630":{"location":"Stanford, CA"},
	"+12016485398":{"location":"Mountain View, CA"}
};

var locationLookup = {
	"Stanford, CA":["+14084218783","+13129650602","+15125246630"],
	"Mountain View, CA":["+19192603435","+19196278855","+12016485398"]
};

var auth = express.basicAuth("stupidAuth", "7h15155tupid");

app.get('/', function(req, res) {
  	res.send(JSON.stringify(data));
});

app.post('/receive', auth, function(req, res) {

	var fieldWorkerId = req.body.From;
	var text = req.body.Body;

	var textData = text.split("|");
	var type = textData[0];

	if(type == "SYM") {
		var patientId = textData[1];
		var symptomString = textData[2];
		var symptoms = symptomString.split(",");

		var patientName = patientData[patientId]["name"];
		var patientAge = patientData[patientId]["age"];
		var patientGender = patientData[patientId]["gender"];
		var location = fieldWorkers[fieldWorkerId]["location"];

		var ts = new Date().getTime();
		var symptomPayload = {
			"fieldWorkerId":fieldWorkerId,
			"type":type,
			"age":patientAge,
			"gender":patientGender,
			"name":patientName,
			"symptoms": symptoms,
			"timestamp": ts,
			"location": location
		};
		data.push(symptomPayload);
		request.get('http://fieldhealer.appspot.com/diagnose?data='+JSON.stringify(symptomPayload), function (error, response, body) {
		  if (!error && response.statusCode == 200) {
		    console.log(body);
		    // Load the twilio module
		    var twilio = require('twilio');
		    data.push(body); 
		    // Create a new REST API client to make authenticated requests against the
		    // twilio back end
		    var client = new twilio.RestClient('AC2a23dcf1594605fec24d935810164d73', '8513242fd7087ef35c7761123398855a');
		     
		    // Pass in parameters to the REST API using an object literal notation. The
		    // REST client will handle authentication and response serialzation for you.
		    client.sms.messages.create({
			    to:fieldWorkerId,
			    from:'408-627-7306',
			    body:body
		    }, function(error, message) {
			    // The HTTP request to Twilio will run asynchronously. This callback
			    // function will be called when a response is received from Twilio
			    // The "error" variable will contain error information, if any.
			    // If the request was successful, this value will be "falsy"
			    if (!error) {
				    // The second argument to the callback will contain the information
				    // sent back by Twilio for the request. In this case, it is the
				    // information about the text messsage you just sent:
				    console.log('Success! The SID for this SMS message is:');
				    console.log(message.sid);
				     
				    console.log('Message sent on:');
				    console.log(message.dateCreated);
			    }
			    else {
			    	console.log('Oops! There was an error.');
			    }
		    });



		  }
		});

	}




	
});


app.get("/send",function(req,res){
	var msg = req.query.message;
	console.log(msg);
	var location = req.query.location;
	console.log(location);
    var twilio = require('twilio');
    // Create a new REST API client to make authenticated requests against the
    // twilio back end
    var client = new twilio.RestClient('AC2a23dcf1594605fec24d935810164d73', '8513242fd7087ef35c7761123398855a');
    var workers = locationLookup[location];
    for(var x in workers) {
	    // Pass in parameters to the REST API using an object literal notation. The
	    // REST client will handle authentication and response serialzation for you.
	    client.sms.messages.create({
		    to:workers[x],
		    from:'408-627-7306',
		    body:msg
	    }, function(error, message) {
		    // The HTTP request to Twilio will run asynchronously. This callback
		    // function will be called when a response is received from Twilio
		    // The "error" variable will contain error information, if any.
		    // If the request was successful, this value will be "falsy"
		    if (!error) {
			    // The second argument to the callback will contain the information
			    // sent back by Twilio for the request. In this case, it is the
			    // information about the text messsage you just sent:
			    console.log('Success! The SID for this SMS message is:');
			    console.log(message.sid);
			     
			    console.log('Message sent on:');
			    console.log(message.dateCreated);
		    }
		    else {
		    	console.log('Oops! There was an error.');
		    }
	    });
	}
	res.send("done!");

});

var port = process.env.PORT || 5000;
app.listen(port, function() {
  	console.log("Listening on " + port);
});
