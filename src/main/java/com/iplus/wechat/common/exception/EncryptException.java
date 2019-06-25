package com.iplus.wechat.common.exception;

public class EncryptException extends RuntimeException {
    public EncryptException(Exception e) {
        super(e);
    }

    public EncryptException(String message) {
        super(message);
    }

    public EncryptException(String message, Exception e){
        super(message, e);
    }
}
