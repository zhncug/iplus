package com.iplus.common.exception;

public class DecryptException extends RuntimeException {
    public DecryptException(Exception e) {
        super(e);
    }

    public DecryptException(String message) {
        super(message);
    }

    public DecryptException(String message, Exception e){
        super(message, e);
    }
}
