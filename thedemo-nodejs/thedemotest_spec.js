var frisby = require('frisby'),
        xml2json = require('xml2json');

frisby.create('Get JUNK API')
        .get("http://localhost:19000/junk/")
        .expectBodyContains("Greeting")
        .after(function(err,res,body) {
            // console.log('body:' + body);
        }).toss();

frisby.create('Get Person API')
        .get("http://localhost:19000/webservice/person/1")
        .expectJSON({
            id: 1,
            firstname: "haha",
            lastname: "last",
            age: 18,
            phone: "02-xxx-xxxx"
        }).expectJSONTypes({
            id:  Number,
            age: Number,
            phone: String
        })
        .afterJSON(function(json) {
            // console.log('we got person id:' + json.id);
        }).toss();

frisby.create('Create Person').post("http://localhost:19000/webservice/person",
        {
            firstname: "frisby",
            lastname: "thelast",
            age: 99,
            phone: "02-YYY-YYYY"
        },{json: true}).expectStatus(201)
        .expectJSON(
        {
            firstname: "frisby",
            lastname: "thelast",
            age: 99,
            phone: "02-YYY-YYYY"
        }).expectJSONTypes({
            id: Number,
            firstname: String,
            lastname: String
        });

frisby.create('Create Person with xml')
        .post("http://localhost:19000/webservice/person",
        new Buffer( xml2json.toXml(
                {
                    person: {
                        firstname: "frisby",
                        lastname: "thelast",
                        age: 99,
                        phone: "02-YYY-YYYY"
                    }
                }),"UTF-8" ),
        {
            json: false,
            headers: {
                'Content-type': 'application/xml; charset=utf-8',
                'Accept': 'application/xml'
            }
        }).expectBodyContains("frisbyz");

var person = {
    id: 1,
    firstname: "haha",
    lastname: "last",
    age: 18,
    phone: "02-xxx-xxxx"
};

frisby.create('Get Person with xml')
        .get("http://localhost:19000/webservice/person/1",
        {
            json: false,
            headers: {
                'Content-type': 'application/xml; charset=utf-8',
                'Accept': 'application/xml'
            }
        }).after(function(err,res,body) {
            var _person = JSON.parse( xml2json.toJson(body) );
            expect(_person.Person.firstname).toEqual("haha");
            expect(_person.Person.phone).toMatch(/\d+-\d+-\d+/);
        }).toss();
