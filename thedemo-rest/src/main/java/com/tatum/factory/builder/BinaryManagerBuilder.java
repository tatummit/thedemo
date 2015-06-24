package com.tatum.factory.builder;

import org.apache.commons.lang.StringUtils;
import com.tatum.exception.DemoIllegalParameterException;
import com.tatum.factory.config.BinaryManagerConfig;
import com.tatum.manager.data.BinaryManager;
import com.tatum.manager.data.imp.BinaryLocalFileManager;

public class BinaryManagerBuilder<T extends BinaryManager> {

    private BinaryManagerConfig config;

    private Class<? extends BinaryManager> target;

    public BinaryManagerBuilder(Class<? extends BinaryManager> target) {
        this.target = target;
    }

    public BinaryManagerBuilder<T> setApplicationProperties(BinaryManagerConfig config) {
        this.config = config;
        return this;
    }

    public T build() throws IllegalAccessException, InstantiationException {
        if (config == null) {
            throw new DemoIllegalParameterException("Missing media config");
        }

        BinaryManager obj = target.newInstance();
        initalManager(obj);
        return (T) obj;

    }

    private void initalManager(BinaryManager obj) {
        if (obj instanceof BinaryLocalFileManager) {
            initalBinaryLocalFileManager((BinaryLocalFileManager) obj);
        }
    }

    private void initalBinaryLocalFileManager(BinaryLocalFileManager obj) {
        if (obj instanceof BinaryLocalFileManager) {

            String mediapath;
            if (StringUtils.isBlank(config.getEndpoint())) {
                throw new DemoIllegalParameterException("Missing config.getEndPoint");
            }

            if (config.getEndpoint().endsWith("/")){
                mediapath = config.getEndpoint() + "mybinary";
            } else {
                mediapath = config.getEndpoint() + "/" + "mybinary";
            }

            //create path
            obj.setMediaRoot(mediapath);
        }
    }


}
