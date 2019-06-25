package com.iplus.wechat.common.data;

import com.iplus.wechat.common.utils.DateTimeUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class DateRange {
    private final String start;
    private final String end;

    public Long getStartLong(){
        if(StringUtils.isNotEmpty(this.start)){
            return DateTimeUtils.timestamp(this.start);
        }
        return null;
    }

    public Long getEndLong(){
        if(StringUtils.isNotEmpty(this.end)){
            return DateTimeUtils.timestamp(this.end);
        }
        return null;
    }

    public static DateRange from(Map<String, String> dateRangeMap) {
        if (CollectionUtils.sizeIsEmpty(dateRangeMap)) {
            return null;
        }

        return new DateRange(dateRangeMap.get("min"),dateRangeMap.get("max"));
    }

    public static DateRange fromDays(Integer days) {
        if (days == null) {
            return null;
        }

        Period period = Period.days(days);

        DateTime today = new DateTime().dayOfMonth().roundCeilingCopy();
        DateTime start = today.minus(period);

        return new DateRange(start.toString(CommonDateFormat.LONG_DATETIME), today.toString(CommonDateFormat.LONG_DATETIME));
    }
}
