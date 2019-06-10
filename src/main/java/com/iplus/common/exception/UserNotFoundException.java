package com.iplus.common.exception;

/**
 * Created by zhangrui on 17-2-13.
 */
public class UserNotFoundException extends ApiAccessException {

    public UserNotFoundException(){
        super("user not found!");
    }

}
