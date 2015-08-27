package com.tatum.manager;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.tatum.properties.GoogleProperties;
import com.tatum.service.GoogleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleManagerImpl implements GoogleManager {

    @Autowired
    private GoogleProperties googleProperties;

    private List scopes;

    @PostConstruct
    public void initBean() {
        scopes = new ArrayList<>();
        scopes.add("https://www.googleapis.com/auth/devstorage.full_control");
    }

    @Override
    public String doAutherized() {
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder( new NetHttpTransport(),
                new JacksonFactory(),
                googleProperties.getClientId(),
                googleProperties.getClientSecret(),
                scopes).build();
        return flow.newAuthorizationUrl().setRedirectUri(googleProperties.getCallbackURL()).toURI().toString();
    }


}
