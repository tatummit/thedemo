package com.tatum.util;

import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import com.tatum.exception.DemoRuntimeException;

import java.io.File;
import java.io.IOException;

public class MimeUtils {
    public static String guessMimeType(File file) {
        try (TikaInputStream in = TikaInputStream.get(file)){
            return new Tika().detect(in);
        } catch (IOException ex) {
            throw new DemoRuntimeException("Cannot find content-type",ex);
        }
    }
}
