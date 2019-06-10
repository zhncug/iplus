package com.iplus.common.utils;

import ch.qos.logback.classic.pattern.TargetLengthBasedClassNameAbbreviator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zhangrui on 16-12-27.
 */
public class LogUtils {

    private final static Logger request = LoggerFactory.getLogger("request");
    private final static Logger info = LoggerFactory.getLogger("info");
    private final static Logger error = LoggerFactory.getLogger("error");
    private final static Logger warn = LoggerFactory.getLogger("warn");
    private final static Logger debug = LoggerFactory.getLogger("debug");

    public final static String REQUEST_ID_PREFIX = "rid-";

    private final static String GET_METHOD = "get";
    private final static String CR = String.valueOf((char) HTTP.CR);
    private final static String LF = String.valueOf((char) HTTP.LF);
    private final static String BODY_SEPARATE = CR + LF + CR + LF;


    private final static String REQUEST_MSG_FORMAT = "%s %s %s %s %s %s";
    private final static String MSG_FORMAT = "%s %s - %s";

    private final static TargetLengthBasedClassNameAbbreviator classNameConventer =
            new TargetLengthBasedClassNameAbbreviator(36);

    public static com.iplus.common.utils.Logger getLogger(Class clazz) {
        return new com.iplus.common.utils.Logger(clazz);
    }


    public static void request() {
        RequestContext requestContext = RequestContext.getRequestContext();
        HttpServletResponse response = requestContext.getHttpServletResponse();
        HttpServletRequest requestServlet = requestContext.getHttpServletRequest();
        try {
            request.info(String.format(REQUEST_MSG_FORMAT,
                    REQUEST_ID_PREFIX + requestContext.getRequestId(),
                    requestServlet.getMethod(),
                    requestServlet.getRequestURI(),
                    response.getStatus(),
                    System.currentTimeMillis() - requestContext.getStartTimeStamp(),
                    queryParam(requestServlet)));

        } catch (Exception e) {
            error.error("log request info error", e);
        }
    }

    private static String queryParam(HttpServletRequest requestServlet) throws Exception {

        StringBuilder sb = new StringBuilder();
        Map<String, String[]> params = requestServlet.getParameterMap();

        if (!GET_METHOD.equals(requestServlet.getMethod().toLowerCase())
                && isApplicationJson(requestServlet)) {

            String body = getParamFromBody(requestServlet);
            sb.append("body:").append(body).append(" ");
        }

        String queryString = "";
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                queryString += key + "=" + value + "&";
            }
        }
        // 去掉最后一个空格
        if (queryString.length() > 1) {
            queryString = queryString.substring(0, queryString.length() - 1);
        }
        if (StringUtils.isEmpty(queryString) && requestServlet instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) requestServlet;
            queryString = new String(wrapper.getContentAsByteArray());
        }
        if (StringUtils.isNoneEmpty(queryString)) {
            queryString = queryString.replace(LF, StringUtils.EMPTY)
                    .replace(CR, StringUtils.EMPTY);
        }
        sb.append("param:").append(queryString);
        return sb.toString();
    }

    private static boolean isApplicationJson(HttpServletRequest requestServlet) {
        return StringUtils.equalsAny(requestServlet.getHeader(HTTP.CONTENT_TYPE),
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    public static String getParamFromBody(HttpServletRequest request) throws Exception {
        String param = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        //param一定是json格式的,处理没必要的空格以及回车换行
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode paramNode = objectMapper.readTree(param);
        return objectMapper.writeValueAsString(paramNode);
    }

    public static void info(Class clazz, String message) {
        info.info(injectRequestId(clazz, message));
    }

    public static void info(Class clazz, String message, Object... args) {
        info.info(injectRequestId(clazz, message), args);
    }

    public static void error(Class clazz, String message) {
        error.error(injectRequestId(clazz, message));
    }

    public static void error(Class clazz, String message, Object... args) {
        error.error(injectRequestId(clazz, message), args);
    }

    public static void error(Class clazz, String message, Throwable throwable) {
        error.error(injectRequestId(clazz, message), throwable);
    }

    public static boolean isDebugEnabled() {
        return debug.isDebugEnabled();
    }

    public static void debug(Class clazz, String message) {
        debug.debug(injectRequestId(clazz, message));
    }

    public static void debug(Class clazz, String message, Object... args) {
        debug.debug(injectRequestId(clazz, message), args);
    }

    public static void warn(Class clazz, String message) {
        warn.warn(injectRequestId(clazz, message));
    }

    public static void warn(Class clazz, String message, Object... args) {
        warn.warn(injectRequestId(clazz, message), args);
    }

    private static String injectRequestId(Class clazz, String msg) {
        return String.format(MSG_FORMAT,
                REQUEST_ID_PREFIX + RequestContext.getRequestContext().getRequestId(),
                //缩短className的长度，默认36个字符
                //整个全限定类名没有超过36个字符会原样输出，如果超过会从左往右做缩写，直到长度小于36个字符后不再缩写
                classNameConventer.abbreviate(clazz.getName())
                , msg);
    }
}
