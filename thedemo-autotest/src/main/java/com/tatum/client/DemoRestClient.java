package com.tatum.client;

import com.tatum.client.config.DemoRestClientPropreties;
import com.tatum.client.constant.RestParam;
import com.tatum.client.constant.RestPathResource;
import com.tatum.model.BinaryObject;
import com.tatum.model.Person;
import com.tatum.service.DemoOperationCollection;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class DemoRestClient implements DemoOperationCollection{

    private String endpoint;

    private MediaType acceptFormat;

    private RestTemplate restTemplate;

    private URI personRestService;

    private URI personRestServiceWithId;

    private URI personRestServiceWithBinary;

    protected void init() {

        personRestService = URI.create(endpoint + RestPathResource.BASE_PATH + RestPathResource.PERSON_SERVICE);
        personRestServiceWithId = URI.create(endpoint + RestPathResource.BASE_PATH + RestPathResource.PERSON_SERVICE);
        personRestServiceWithBinary = URI.create(endpoint + RestPathResource.BASE_PATH + RestPathResource.PERSON_WITHBINARY_SERVICE);

    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setDemoRestClientProperies(DemoRestClientPropreties properies) {
        this.acceptFormat = properies.geMediaFormat();
        this.endpoint = properies.getEndpoint();
    }

    @Override
    public void put(String key, BinaryObject binary) {
        MultiValueMap from = new LinkedMultiValueMap<>();
        from.put(RestParam.FILE,new ByteArrayResource(binary.getBinary()) {
            @Override
            public String getFilename() {
                return "dummy.bin";
            }
        });

        URI resolveEndpoint = UriComponentsBuilder.fromUri(personRestServiceWithBinary).buildAndExpand(key).toUri();
        RequestEntity<MultiValueMap> requestEntity = RequestEntity.post(resolveEndpoint)
                .contentType(MediaType.MULTIPART_FORM_DATA).body(from);
        restTemplate.exchange(requestEntity,Void.class);
    }

    @Override
    public BinaryObject get(String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(acceptFormat);
        HttpEntity request = new HttpEntity(headers);
        restTemplate.exchange(personRestServiceWithBinary.toString(), HttpMethod.GET,request,byte[].class,key);
        return null;
    }

    @Override
    public Person getId(long id) {
        return null;
    }

    @Override
    public Person insert(Person person) {
        return null;
    }

    @Override
    public int update(Person person) {
        return 0;
    }
}
