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
    request2('http://localhost:3000/task', function (error, response, body) {
        if (!error && response.statusCode == 200) {
            res.send(JSON.parse(body));
        }
    })
});
app.post('/task', jsonParser, function (req, res) {
    // Ensure to call publish to fire events from the observable
    emitter.publish();
    request2.post(
        {
            url: 'http://localhost:3000/task',
            headers: {'Content-Type': 'application/json'},
            body: '{"title":"' + req.body.title + '"}'
        },
        function (error, response, body) {
            res.send(req.body);

        }
    );

});

var nowe = [];
setTimeout(function () {
    emitter.on('data', function (data) {
        nowe = ['1@wp.pl', '2@wp.pl']
    });
}, 0);


app.get('/task-new', function (req, res) {


    var index = nowe.indexOf(req.query.email);
    if (index > -1) {
        nowe.splice(index, 1);
        res.send(JSON.parse('{"email":"' + req.query.email + '"}'));
    }  else {
        res.send(JSON.parse('{"email":"brak nowych"}'));
    }
});


app.listen(2000, function () {
    console.log('Example app listening on port 2000!')
})
