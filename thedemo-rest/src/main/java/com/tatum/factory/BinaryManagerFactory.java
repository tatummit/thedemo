package com.tatum.factory;

import com.tatum.service.data.BinaryManager;

public interface BinaryManagerFactory {
    public BinaryManager createBinaryManager( Class<?> manager );
}
