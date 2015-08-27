package com.tatum.properties;

import java.util.Properties;

public class GoogleProperties {

    private Properties properties;

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getClientId() {
        return properties.getProperty("google.client.id");
    }

    public String getClientSecret() {
        return properties.getProperty("google.client.secret");
    }

    public String getCallbackURL() {
        return properties.getProperty("google.callback.url");
    }
}
