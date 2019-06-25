package com.iplus.wechat.common.utils;

import java.lang.reflect.Field;

/**
 * Created by zhangrui on 17-1-24.
 */
public class EnumUtils {

    private static Logger logger = (Logger) LogUtils.getLogger(EnumUtils.class);

    /**
     * 获取field名字为fieldName的，值为value的枚举类实例
     * @param clazz 枚举类class
     * @param value field值
     * @param fieldName field名字
     * @return
     */
    public static <T> T getEnum(Class<T> clazz, Object value, String fieldName){
        if(value != null){
            T[] ts = clazz.getEnumConstants();
            try {
                Field vField = clazz.getDeclaredField(fieldName);
                vField.setAccessible(true);
                for (T t : ts) {
                    Object v = vField.get(t);
                    if(value.equals(v)){
                        return t;
                    }
                }
            } catch (Exception e) {
                logger.error("get enum error!", e);
            }
        }
        return null;
    }

    /**
     * 根据value获取对应的枚举类实例，默认获取field为“value”的枚举实例
     * @param clazz 枚举类class
     * @param value field值
     * @return
     */
    public static <T> T getEnum(Class<T> clazz, Object value){
        return getEnum(clazz, value, "value");
    }
}
