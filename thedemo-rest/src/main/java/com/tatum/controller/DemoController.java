package com.tatum.controller;

import com.tatum.manager.GoogleManagerImpl;
import com.tatum.service.DemoOperationCollection;
import com.tatum.model.BinaryObject;
import com.tatum.model.Person;
import com.tatum.service.GoogleManager;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;

@RestController
@RequestMapping("/webservice")
public class DemoController {

    @Autowired
    @Qualifier("DemoOperation")
    DemoOperationCollection demoOperations;

    @Autowired
    GoogleManagerImpl googleManager;

    @RequestMapping(value = "/person/{personid}", method = RequestMethod.GET
            , produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    @ResponseBody
    public Person getPersonJSON(@PathVariable(value = "personid") Long personid) {

        return demoOperations.getId(personid);
    }

    @RequestMapping(value = "/person/{personid}", method = RequestMethod.PUT,
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity updatePersonXML(@PathVariable(value = "personid") Long personid,@RequestBody Person person) {
        person.setId(personid);
        demoOperations.update(person);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/person", method = RequestMethod.POST,
            consumes =  { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Person> createPersonXML(@RequestBody Person person, HttpServletRequest request)
            throws URISyntaxException {
        demoOperations.insert(person);
        String path = ServletUriComponentsBuilder.fromServletMapping(request)
                .path("/webservice/json/person/{personid}")
                .build()
                .expand(person.getId())
                .toUriString();
        return ResponseEntity.created(new URI(path)).body(person);
    }

    @RequestMapping(value ="/person/{personid}/binary", method = RequestMethod.POST)
    public ResponseEntity savePersonImageBinary(@PathVariable(value = "personid") Long personid,@RequestParam("file")
    MultipartFile file)
            throws IOException {
        BinaryObject binaryObject = new BinaryObject();
        binaryObject.setBinary(IOUtils.toByteArray(file.getInputStream()));
        binaryObject.setMime(URLConnection.guessContentTypeFromName(file.getName()));
        demoOperations.put(personid.toString(), binaryObject);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value ="/person/{personid}/binary", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPersonImageBinary(@PathVariable(value = "personid") Long personid)
            throws IOException {
        BinaryObject binaryObject = demoOperations.get(personid.toString());
        return ResponseEntity.accepted()
                .contentType(MediaType.parseMediaType(binaryObject.getMime()))
                .body(binaryObject.getBinary());
    }


    @RequestMapping(value ="/google", method = RequestMethod.GET)
    public void invorkGoogleSheet(HttpServletResponse response) throws IOException {
        String url = googleManager.doAutherized();
        response.sendRedirect(url);
    }

}
