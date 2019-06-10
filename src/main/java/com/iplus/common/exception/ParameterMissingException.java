package com.iplus.common.exception;

import lombok.Getter;

public class ParameterMissingException extends ApiAccessException {
    @Getter
    private String paramName;

    public  ParameterMissingException(String paramName) {
        super(String.format("Parameter %s is missing", paramName));
        this.paramName = paramName;
    }
}
