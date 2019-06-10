package com.iplus.common.exception;

/**
 * Created by zhangrui on 17-2-13.
 */
public class BusinessException extends ApiAccessException {


    public BusinessException(String message, Exception e){
        super(message, e);
    }

    public BusinessException(Exception e){
        super("business error happend ！", e);
    }

    public BusinessException(String message){
        super(message);
    }

}
