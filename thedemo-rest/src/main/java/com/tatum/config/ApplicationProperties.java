package com.tatum.config;

import com.tatum.service.data.imp.BinaryLocalFileManager;

public class ApplicationProperties {

    public String getRootLocalBinaryPath() {
        return "/tmp/";
    }

    public String getBinaryManagerClass() {
        return BinaryLocalFileManager.class.getName();
    }
    
}
