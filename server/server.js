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


app.get('/task-new', function (req, res) {


    new Promise(
        function (resolve, reject) {
            emitter.on('data', function (data) {

                // console.log('Data: ' + data);

            });

            emitter.on('end', function () {
                console.log('End');
                resolve();


            });

            setTimeout(function () {
                resolve();
                console.log("1");
            }, 5000)

        }).then(function () {
        res.send('nowe taski');
    });


});


app.listen(2000, function () {
    console.log('Example app listening on port 2000!')
})
