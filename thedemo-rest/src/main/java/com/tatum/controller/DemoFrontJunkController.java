package com.tatum.controller;

import com.tatum.exception.DemoRuntimeException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;
import com.tatum.exception.DemoNotFoundObjectException;
import com.tatum.manager.DemoOperationCollection;
import com.tatum.model.BinaryObject;
import com.tatum.model.Person;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;

@RestController
@RequestMapping("/junk")
public class DemoFrontJunkController {

    @Autowired
    @Qualifier("DemoBackendOperation")
    DemoOperationCollection demoOperations;

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "\"Greeting\"";
    }


    @RequestMapping(value = "/webservice/json/person/{personid}", method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public Person getPersonJSON(@PathVariable(value = "personid") Long personid) {
        return getPerson(personid);
    }

    @RequestMapping(value = "/webservice/xml/person/{personid}", method = RequestMethod.GET
            , produces = MediaType.APPLICATION_XML_VALUE
    )
    @ResponseBody
    public Person getPersonXML(@PathVariable(value = "personid") Long personid) {
        return getPerson(personid);
    }

    private Person getPerson(Long id) {
        Person person = demoOperations.getId(id);
        if (person == null) {
            throw new DemoNotFoundObjectException("Not found person id=" + id);
        } else {
            return person;
        }
    }

    @RequestMapping(value = "/webservice/person/{personid}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getPersonModel(@PathVariable(value = "personid") Long personid
            , @RequestParam(value = "format", required = false) String format) {
        Person person = getPerson(personid);
        ModelAndView modelAndView;
        if("json".equals(format)) {
            modelAndView = new ModelAndView(new MappingJackson2XmlView());
        } else {
            modelAndView = new ModelAndView(new MappingJackson2JsonView());
        }
        modelAndView.addObject(person);
        return modelAndView;
    }

    @RequestMapping(value = "/webservice/json/person", method = RequestMethod.POST,
            consumes =  MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Person> createPersonJSON(@RequestBody Person person,HttpServletRequest request) {
       return createPerson(person, "json", request);
    }

    @RequestMapping(value = "/webservice/xml/person", method = RequestMethod.POST,
            consumes =  MediaType.APPLICATION_XML_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Person> createPersonXML(@RequestBody Person person, HttpServletRequest request) {
       return createPerson(person, "xml",request);
    }

    private ResponseEntity<Person> createPerson(Person person,String responseType, HttpServletRequest request) {
        demoOperations.insert(person);
        String path = ServletUriComponentsBuilder.fromServletMapping(request)
                .path("/webservice/json/person/{personid}")
                .build()
                .expand(person.getId())
                .toUriString();
        try {
            return ResponseEntity.created(new URI(path)).body(person);
        } catch (URISyntaxException e) {
            throw new DemoRuntimeException("Invalid syntex",e);
        }
    }

    @RequestMapping(value = "/webservice/json/person/{personid}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updatePersonJSON(@RequestParam(value = "personid") Long personid,@RequestBody Person person) {
        updatePerson(personid,person);
    }

    @RequestMapping(value = "/webservice/xml/person/{personid}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_XML_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updatePersonXML(@RequestParam(value = "personid") Long personid,@RequestBody Person person) {
        updatePerson(personid,person);
    }

    private void updatePerson(Long personid, Person person) {
        person.setId(personid);
        demoOperations.update(person);
    }

    @RequestMapping(value ="webservice/img/person/{personid}/binary", method = RequestMethod.POST)
    public ResponseEntity savePersonImageBinary(@PathVariable(value = "personid") Long personid,@RequestParam("file") MultipartFile file)
            throws IOException {
        savePersonImageBinary(personid.toString(),file.getName(), IOUtils.toByteArray(file.getInputStream()));
        return ResponseEntity.noContent().build();
    }

    private void savePersonImageBinary(String personid, String filename, byte[] content) {
        BinaryObject binaryObject = new BinaryObject();
        binaryObject.setBinary(content);
        binaryObject.setMime(URLConnection.guessContentTypeFromName(filename));
        demoOperations.put(personid,binaryObject);
    }

    @RequestMapping(value ="webservice/img/person/{personid}/binary", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPersonImageBinary(@PathVariable(value = "personid") Long personid)
            throws IOException {
        BinaryObject binaryObject = demoOperations.get(personid.toString());
        return ResponseEntity.accepted()
                .contentType(MediaType.parseMediaType(binaryObject.getMime()))
                .body(binaryObject.getBinary());
    }

}
