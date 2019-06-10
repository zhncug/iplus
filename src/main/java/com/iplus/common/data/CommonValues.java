package com.iplus.common.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.iplus.common.model.CommonFields;

import java.util.Map;

public class CommonValues {

    public static final int NO_BIZ_GROUP = 1;
    public static final int WIN_NO_BIZ_GROUP_V3 = 2;
    public static final int LINUX_NO_BIZ_GROUP_V3 = 1;
    public static final String NO_BIZ_GROUP_NAME = "未分组主机";
    public static final String UNDER_LINE = "_";
    public static final int ZERO_PAGE = 0;
    public static final int LIMIT_ONE = 1;

    // os type
    public static final int OS_TYPE_ALL = 0;
    public static final int OS_TYPE_LINUX = 1;
    public static final int OS_TYPE_WIN = 2;

    //online
    public static final int OFFLINE = 0;
    public static final int ONLINE = 1;

    public static final int CLOSE = 0;
    public static final int OPEN = 1;

    public static final int TRUE_INT = 1;
    public static final int FALSE_INT = 0;

    // proto
    public static final String NET_PROTO_TCP = "tcp";
    public static final String NET_PROTO_UDP = "udp";

    public static final String WEB_PROTO_HTTP = "http";
    public static final String WEB_PROTO_HTTPS = "https";

    public static final String UNSPECIFIED_IP = "0.0.0.0";

    public static final String ORIGIN_TIME = "1970-01-01 00:00:00";
    public static final String ORIGIN_TIME_MAX = "2099-12-31 00:00:00";


    public static final String NAME_APACHE = "httpd";
    public static final String NAME_NGINX = "nginx";
    public static final String NAME_TOMCAT = "tomcat";
    public static final String NAME_WEBLOGIC = "weblogic";
    public static final String NAME_JBOSS = "jboss";
    public static final String NAME_WILDFLY = "wildfly";

    public static final String WEB_SITE_TYPE_NGINX = "nginx";
    public static final String WEB_SITE_TYPE_APACHE = "httpd";
    public static final String WEB_SITE_TYPE_JAVA = "java"; //  tomcat, weblogic, jboss, wildfly


    public static final String HIGH_LIGHT_START = "<qthighlight--";
    public static final String HIGH_LIGHT_END = "--qthighlight>";

    public static final int DEFAULT_PRIORITY = 0;

    private static final Map<String, String> map = Maps.newHashMap();

    static{
        map.put("ip", CommonFields.HOST_IP);
        map.put("hostname", CommonFields.HOST_HOST_NAME);
    }
    public static final Map<String, String> SORT_FIELD_MAP = ImmutableMap.copyOf(map);

    public static final String API_V3_FORMAT = "%s/v3/%s/%s";
    public static final String DEFAULT_COM_ID = "";

    public static final String TRACKING_POINT_APPNAME = "wisteria";

    public static Map<Integer, String> APPRULE_NAME_MAP = ImmutableMap.<Integer, String> builder()
            .put(1,"系统架构").put(2, "Web服务").put(3, "运维工具").put(4, "数据库").put(5, "安全应用")
            .put(6, "系统应用").put(7, "可疑应用").put(8, "其他").build();

    public static Map<Integer, String> INITLEVEL_NAME_MAP = ImmutableMap.<Integer, String> builder()
            .put(0, "停机(rc0)").put(1, "单用户模式(rc1)").put(2, "多用户无NFS模式(rc2)").put(3, "完全多用户模式(rc3)")
            .put(4, "预留模式(rc4)").put(5, "桌面模式(rc5)").put(6, "重新启动(rc6)").put(7, "单用户自启动(rcs)").build();
}
