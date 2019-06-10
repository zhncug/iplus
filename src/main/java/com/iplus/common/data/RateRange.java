package com.iplus.common.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据百分比统计
 * Created by zhangnan on 17-4-15.
 */
public enum RateRange {

    BETWEEN_0_TO_20(1, 0.0, 0.2),
    BETWEEN_20_TO_50(2, 0.2, 0.5),
    BETWEEN_50_TO_80(3, 0.5, 0.8),
    BETWEEN_80_TO_100(4, 0.8, 1.0);


    private static final List<RateRange> list = new ArrayList<>();

    static {
        list.add(BETWEEN_0_TO_20);
        list.add(BETWEEN_20_TO_50);
        list.add(BETWEEN_50_TO_80);
        list.add(BETWEEN_80_TO_100);
    }

    @Getter
    private Integer id;
    @Getter
    private Double startRate;
    @Getter
    private Double endRate;


    RateRange(Integer id, Double startRate, Double endRate) {
        this.id = id;
        this.startRate = startRate;
        this.endRate = endRate;
    }

    public static List<RateRange> getList() {

        return list;

    }

    /**
     * 根据比率获取分段的区间
     *
     * @param rate
     * @return
     */
    public static int getRateLevel(double rate) {
        if (rate < 0) {
            return BETWEEN_0_TO_20.getId();
        } else if (rate >= 100) {
            return BETWEEN_80_TO_100.getId();
        } else {
            for (RateRange rateRange : list) {
                if (rate >= rateRange.getStartRate() && rate < rateRange.getEndRate()) {
                    return rateRange.getId();
                }
            }
        }
        return 0;
    }
}
