package com.tatum.exception;

public class DemoRuntimeException extends RuntimeException {

    public DemoRuntimeException(String mesage) {
        super(mesage);
    }

    public DemoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DemoRuntimeException(Throwable cause) {
        super(cause);
    }

}
