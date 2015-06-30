package com.tatum.factory.imp;

import com.tatum.exception.DemoRuntimeException;
import com.tatum.factory.builder.BinaryManagerBuilder;
import com.tatum.factory.config.BinaryManagerConfig;
import com.tatum.service.data.BinaryManager;
import com.tatum.factory.BinaryManagerFactory;

public class BinaryManagerFactoryImp implements BinaryManagerFactory {

    private BinaryManagerConfig config;

    public BinaryManagerFactoryImp(BinaryManagerConfig config) {
        this.config = config;
    }



    @Override
    public BinaryManager createBinaryManager(Class<?> manager) {
        try {
            return build((Class <? extends BinaryManager>) manager);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new DemoRuntimeException("Cannot build BinaryManager",e);
        }
    }

    private <T extends BinaryManager> T build(Class<T> c) throws InstantiationException, IllegalAccessException {
        return new BinaryManagerBuilder<T>(c).setApplicationProperties(config).build();
    }
}
