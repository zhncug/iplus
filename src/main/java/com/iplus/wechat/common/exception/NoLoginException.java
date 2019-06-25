package com.iplus.wechat.common.exception;

/**
 * Created by zhangrui on 17-2-13.
 */
public class NoLoginException extends ApiAccessException {

    public NoLoginException(String message, Exception e){
        super(message, e);
    }

    public NoLoginException(String message){
        super(message);
    }

    public NoLoginException(){
        super("no login");
    }

}
