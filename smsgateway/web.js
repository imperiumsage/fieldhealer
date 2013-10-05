var express = require("express");
var app = express();
app.use(express.logger());
app.use(express.bodyParser());

auth = express.basicAuth("stupidAuth", "7h15155tupid");

app.get('/', function(request, response) {
  	response.send("Hello World!");
});

app.post('/receive', auth, function(request, response) {		
	console.log(request.body.From);
	console.log(request.body.Body);

});

var port = process.env.PORT || 5000;
app.listen(port, function() {
  	console.log("Listening on " + port);
});
