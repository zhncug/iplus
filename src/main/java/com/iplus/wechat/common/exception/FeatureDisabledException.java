package com.iplus.wechat.common.exception;

public class FeatureDisabledException extends ApiAccessException {
    public FeatureDisabledException(Exception e) {
        super(e);
    }

    public FeatureDisabledException(String message) {
        super(message);
    }
}
