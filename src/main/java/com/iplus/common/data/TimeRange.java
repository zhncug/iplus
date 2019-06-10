package com.iplus.common.data;

import lombok.Getter;

/**
 * Created by zhangrui on 17-2-10.
 */
public enum TimeRange {
    DAY_0_1(1, 0, 1),
    DAY_1_3(2, -3, 0),
    DAY_3_7(3, -7, -3),
    DAY_7_30(4, -30, -7),
    DAY_OVER_30(5, null, -30);

    @Getter
    private int value;
    @Getter
    private Integer startDay;
    @Getter
    private Integer endDay;

    TimeRange(int value, Integer startDay, Integer endDay) {
        this.value = value;
        this.startDay = startDay;
        this.endDay = endDay;
    }
}
