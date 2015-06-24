package com.tatum.exception;

public class DemoIllegalParameterException extends DemoRuntimeException{

    public DemoIllegalParameterException(String mesage) {
        super(mesage);
    }

    public DemoIllegalParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public DemoIllegalParameterException(Throwable cause) {
        super(cause);
    }
}
