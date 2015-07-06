package com.tatum.service.data;

import com.tatum.model.BinaryObject;

public interface BinaryManager {

    public void put(String key, BinaryObject binary);

    public BinaryObject get(String key);

}
