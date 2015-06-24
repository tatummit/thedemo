package com.tatum.factory;

import com.tatum.manager.data.BinaryManager;

public interface BinaryManagerFactory {
    public BinaryManager createBinaryManager( Class<?> manager );
}
