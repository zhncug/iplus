package com.iplus.wechat.common.exception;

/**
 * Created by zhangrui on 17-5-8.
 */
public class NoPermissionException extends ApiAccessException {

    public NoPermissionException(Exception e) {
        super(e);
    }

    public NoPermissionException(String url) {
        super(String.format("sorry, you have no permission for %s", url));
    }

    public NoPermissionException(String url, Exception e) {
        super(String.format("sorry, you have no permission for %s", url), e);
    }
}
