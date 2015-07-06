package com.tatum.client.config;

import org.springframework.http.MediaType;

public class DemoRestClientPropreties {

    public String getEndpoint() {
        return "http://localhost:19000";
    }

    public MediaType geMediaFormat() {
        return MediaType.APPLICATION_JSON;
    }

}
