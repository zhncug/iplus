package com.iplus.common.exception;

public class InvalidParameterException extends ApiAccessException {
    public InvalidParameterException(Exception e) {
        super(e);
    }

    public InvalidParameterException(String message) {
        super(message);
    }
}
