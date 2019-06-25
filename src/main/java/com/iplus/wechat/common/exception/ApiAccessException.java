package com.iplus.wechat.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiAccessException extends RuntimeException {

    private int errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public ApiAccessException(Exception e) {
        super(e);
    }

    public ApiAccessException(String message) {
        super(message);
    }

    public ApiAccessException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApiAccessException(String message, Exception e){
        super(message, e);
    }
}
