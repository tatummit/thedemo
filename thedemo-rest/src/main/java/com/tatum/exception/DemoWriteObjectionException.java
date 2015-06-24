package com.tatum.exception;

public class DemoWriteObjectionException extends DemoRuntimeException {

    public DemoWriteObjectionException(String mesage) {
        super(mesage);
    }

    public DemoWriteObjectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DemoWriteObjectionException(Throwable cause) {
        super(cause);
    }
}
