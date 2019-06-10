package com.iplus.common.exception;

public class DataNotFoundException extends ApiAccessException {

    public DataNotFoundException(String category, Object target) {
        super(String.format("%s: %s not found", category, target.toString()));
    }

}
