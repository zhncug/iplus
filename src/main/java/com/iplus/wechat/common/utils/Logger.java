package com.iplus.wechat.common.utils;

public class Logger {

    private Class clazz;
    public Logger(Class clazz){
        this.clazz = clazz;
    }

    public void info(String message){
        LogUtils.info(clazz,message);
    }

    public void info(String message,Object ... args){
        LogUtils.info(clazz,message,args);
    }

    public void error(String message){
        LogUtils.error(clazz,message);
    }

    public void error(String message,Object...args){
        LogUtils.error(clazz,message,args);
    }

    public void error(String message,Throwable throwable){
        LogUtils.error(clazz,message,throwable);
    }

    public boolean isDebugEnabled() {
        return LogUtils.isDebugEnabled();
    }

    public void debug(String message){
        LogUtils.debug(clazz,message);
    }

    public void debug(String message,Object ... args){
        LogUtils.debug(clazz,message,args);
    }

    public void warn(String message){
        LogUtils.warn(clazz,message);
    }

    public void warn(String message,Object... args){
        LogUtils.warn(clazz,message,args);
    }
}
