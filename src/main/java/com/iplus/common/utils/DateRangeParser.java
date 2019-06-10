package com.iplus.common.utils;

import com.iplus.common.data.DateRange;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DateRangeParser {
    private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public interface ParserClient {
        void setOnlyStart(Date start);
        void setOnlyEnd(Date end);
        void setBetweenStartAndEnd(Date start, Date end);
    }


    public static void parse(List<String> dateRange, ParserClient client) {
        if (dateRange.size() != 2) {
            return;
        }

        Date startDate = null;
        Date endDate = null;

        String max = dateRange.get(0);
        String min = dateRange.get(1);

        if (StringUtils.isNotEmpty(min)) {
            try {
                startDate = fmt.parse(min);
            } catch (ParseException e) {
            }
        }

        if (StringUtils.isNotEmpty(max)) {
            try {
                endDate = fmt.parse(max);
            } catch (ParseException e) {
            }
        }

        callback(startDate, endDate, client);
    }

    public static void parse(Map<String, String> dateRange, ParserClient client) {
        if (CollectionUtils.sizeIsEmpty(dateRange)) {
            return;
        }

        Date startDate = null;
        Date endDate = null;

        String max = dateRange.get("max");
        String min = dateRange.get("min");

        if (StringUtils.isNotEmpty(min)) {
            try {
                startDate = fmt.parse(min);
            } catch (ParseException e) {
            }
        }

        if (StringUtils.isNotEmpty(max)) {
            try {
                endDate = fmt.parse(max);
            } catch (ParseException e) {
            }
        }

        callback(startDate, endDate, client);
    }

    public static void parse(DateRange dateRange, ParserClient client) {
        if (dateRange == null) {
            return;
        }

        Date startDate = null;
        Date endDate = null;

        String startDateText = dateRange.getStart();
        String endDateText = dateRange.getEnd();

        if (StringUtils.isNotEmpty(startDateText)) {
            try {
                startDate = fmt.parse(startDateText);
            } catch (ParseException e) {
            }
        }

        if (StringUtils.isNotEmpty(endDateText)) {
            try {
                endDate = fmt.parse(endDateText);
            } catch (ParseException e) {
            }
        }

        callback(startDate, endDate, client);
    }

    public static void parse(String dateRange, ParserClient client) {
        if (StringUtils.isEmpty(dateRange)) {
            return;
        }

        String[] parts = StringUtils.split(dateRange, '/');

        Date startDate = null;
        Date endDate = null;

        if (parts.length == 1) {
            try {
                startDate = fmt.parse(parts[0].trim());
            } catch (ParseException e) {
            }
        } else if (parts.length == 2) {
            try {
                startDate = fmt.parse(parts[0].trim());
            } catch (ParseException e) {
            }

            try {
                endDate = fmt.parse(parts[1].trim());
            } catch (ParseException e) {
            }
        }

        callback(startDate, endDate, client);
    }

    private static void callback(Date startDate, Date endDate, ParserClient client) {
        if (startDate != null && endDate == null) {
            client.setOnlyStart(startDate);
        } else if (startDate == null && endDate != null) {
            client.setOnlyEnd(endDate);
        } else if (startDate != null && endDate != null) {
            if (startDate.equals(endDate)) {
                endDate = new DateTime(endDate).plusDays(1).toDate();
            }

            client.setBetweenStartAndEnd(startDate, endDate);
        }
    }
}
