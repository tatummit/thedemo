package com.tatum.manager.data.imp;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import com.tatum.exception.DemoNotFoundObjectException;
import com.tatum.exception.DemoRuntimeException;
import com.tatum.exception.DemoWriteObjectionException;
import com.tatum.manager.data.BinaryManager;
import com.tatum.model.BinaryObject;
import com.tatum.util.MimeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BinaryLocalFileManager implements BinaryManager {

    private String mediaRoot;

    public void setMediaRoot(String mediaRoot) {
        this.mediaRoot = mediaRoot;
    }

    public BinaryLocalFileManager() {

    }

    @Override
    public void put(String key, BinaryObject binary) {
        String path = buildPath(key);
        try {
            FileUtils.writeByteArrayToFile(new File(path), binary.getBinary(), false);
        } catch (IOException ex) {
            throw new DemoWriteObjectionException("Cannot write object to local machine",ex);
        }

    }

    @Override
    public BinaryObject get(String key) {
        String path = buildPath(key);
        BinaryObject bin = new BinaryObject();
        try (FileInputStream fileInputStream = FileUtils.openInputStream(new File(path))) {
            bin.setMime(MimeUtils.guessMimeType(new File(path)));
            bin.setBinary(IOUtils.toByteArray(fileInputStream));
        } catch (FileNotFoundException e) {
            throw new DemoNotFoundObjectException("Cannot found object for personid="+key);
        } catch (IOException e) {
            throw new DemoRuntimeException("Cannot get object from local machine",e);
        }
        return bin;
    }

    private String buildPath(String key) {
        return mediaRoot + "/" + key;
    }
}
