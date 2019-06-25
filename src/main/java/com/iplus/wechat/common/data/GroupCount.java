package com.iplus.wechat.common.data;

import com.iplus.wechat.common.model.CommonFields;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class GroupCount<T> {
    @Field(CommonFields.GROUP_ID)
    private final T groupId;

    @Field(CommonFields.COUNT)
    private final long count;

    public static GroupCount<Object> empty() {
        return new GroupCount<>(null, 0);
    }

    public static <T> GroupCount<T> from(Map<String, Object> map) {
        return new GroupCount<>((T) map.get(CommonFields.GROUP_ID), (long) map.get(CommonFields.COUNT));
    }
}
