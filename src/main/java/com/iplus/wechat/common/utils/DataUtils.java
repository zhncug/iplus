package com.iplus.wechat.common.utils;

import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zhangrui on 17-6-14.
 */
public class DataUtils {

    public static <T> List<T> getFieldList(Collection list, String fieldName, Class<T> fieldType) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return (List<T>) list.stream().map(obj -> {
            Field field = ReflectionUtils.findField(obj.getClass(), fieldName);
            if (field != null) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (!value.getClass().equals(fieldType)) {
                        throw new ConvertException("Can not convert " + value.getClass() + " to " + fieldType);
                    }
                    return value;
                } catch (IllegalAccessException e) {
                    throw new ConvertException("Get field from " + obj.getClass() + " error", e);
                }

            }
            return null;
        }).collect(Collectors.toList());
    }

    public static <K, V> Map<K, V> convertToMap(Collection<V> list, String keyField, Class<K> keyType) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Map map = Maps.newHashMap();
        for (Object obj : list) {
            Field field = ReflectionUtils.findField(obj.getClass(), keyField);
            if (field != null) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    map.put((K) value, obj);
                } catch (IllegalAccessException e) {
                    throw new ConvertException("Get field from " + obj.getClass() + " error", e);
                }

            }
        }
        return map;
    }

    private static class ConvertException extends RuntimeException {
        public ConvertException(String msg) {
            super(msg);
        }

        public ConvertException(String msg, Throwable e) {
            super(msg, e);
        }
    }
}
