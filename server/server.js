var express = require('express')
var app = express();
var bodyParser = require('body-parser');
var jsonParser = bodyParser.json();
var request2 = require('request');
var Promise = require("bluebird");

var Rx = require('rx');
var RxNode = require('rx-node');

var source = Rx.Observable.return(42);

var emitter = RxNode.toEventEmitter(source, 'data');


app.get('/task', function (req, res) {
    request2('http://localhost:3000/task', function (error, response, body) { //http://localhost:8080/api/messages
        if (!error && response.statusCode == 200) {
            res.send(JSON.parse(body));
        }
    })
});
var nowe = {};
app.post('/task', jsonParser, function (req, res) {

    nowe[req.body.sender] = "tak";
    nowe[req.body.receiver] = "tak";

    emitter.publish();
    var bodyMessage = {};
    bodyMessage["sender"] = req.body.sender;
    bodyMessage["content"] = req.body.content;
    bodyMessage["receiver"] = req.body.receiver;
    request2.post(
        {
            url: 'http://localhost:3000/task',  //http://localhost:8080/api/messages
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(bodyMessage)
        },
        function (error, response, body) {
            res.status(201).send(JSON.parse('{"title":"' + req.body.content + '","id":"dowolne"}'));

        }
    );

});


setTimeout(function () {
    emitter.on('data', function (data) {
        console.log(nowe);
    });
}, 0);


app.get('/task-new', function (req, res) {
    return new Promise(function (resolve, reject) {
        if (nowe[req.query.email] == "tak") {
            nowe[req.query.email] = "nie";
            resolve();
        }
        emitter.on('end', function () {
            resolve();
        });
    }).then(function () {
        nowe[req.query.email] = "nie";
        res.send(JSON.parse('{"email":"' + req.query.email + '"}'));

    });
});


app.listen(2000, function () {
    console.log('Example app listening on port 2000!')
})
