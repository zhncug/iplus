package com.iplus.wechat.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CommonDateFormat {
    public static final String LONG_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String LONG_DATE = "yyyy-MM-dd";
    public static final DateFormat LONG_DATETIME_FORMAT = new SimpleDateFormat(LONG_DATETIME);
    public static final DateFormat LONG_DATE_FORMAT = new SimpleDateFormat(LONG_DATE);

}
