package tatum.app.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import tatum.app.model.Person;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }


    @RequestMapping(value="/webservice/json/person/{personid}", method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public Person getPersonJSON(@PathVariable(value = "personid")Integer personid) {
        Person person = new Person();
        person.setFirstname("thedemo");
        person.setLastname("tatum");
        person.setAddress("Here is where i am from");
        person.setPhone("0-2XXX-XXXX");
        return person;
    }

    @RequestMapping(value="/webservice/xml/person/{personid}", method = RequestMethod.GET
            , produces = MediaType.APPLICATION_XML_VALUE
    )
    @ResponseBody
    public Person getPersonXML(@PathVariable(value = "personid")Integer personid) {
        Person person = new Person();
        person.setFirstname("thedemo");
        person.setLastname("tatum");
        person.setAddress("Here is where i am from");
        person.setPhone("0-2XXX-XXXX");
        return person;
    }

}
