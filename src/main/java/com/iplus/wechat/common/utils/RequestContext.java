package com.iplus.wechat.common.utils;

import com.google.common.collect.Maps;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangrui on 16-12-27.
 */
public class RequestContext extends ThreadLocal<Map> {

    private static final String REQUEST_ID = "requestId";
    private static final String UUID = "uuid";
    private static final String AGENT_GROUP = "agentGroup";
    private static final String REQUEST = "request";
    private static final String RESPONSE = "response";
    private static final String START_TIME = "start";
    private static final RequestContext requestContext = new RequestContext();


    private RequestContext() {
    }

    public static RequestContext getRequestContext() {
        return requestContext;
    }

    private Object get(String field) {
        Map<String, Object> map = get();
        if (map == null || map.get(field) == null) {
            return null;
        }
        return map.get(field);
    }

    private void set(String field, Object value) {
        Map<String, Object> map = get();
        if (map == null) {
            map = Maps.newConcurrentMap();
        }
        map.put(field, value);
        set(map);
    }

    public List<Integer> getAgentGroup() {
        return (List) get(AGENT_GROUP);
    }

    public void setAgentGroup(List<Integer> agentGroup) {
        set(AGENT_GROUP, agentGroup);
    }

    public String getCurrentUuid() {
        return (String) get(UUID);
    }

    public void setCurrentUuid(String uuid) {
        set(UUID, uuid);
    }

    public int getRequestId() {
        Object requestId = get(REQUEST_ID);
        if (requestId == null) {
            return 0;
        }
        return (int) requestId;
    }

    public void setRequestId(int requestId) {
        set(REQUEST_ID, requestId);
    }

    public void remove() {
        super.remove();
    }

    public void init(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = Maps.newConcurrentMap();
        map.put(REQUEST, request);
        map.put(RESPONSE, response);
        map.put(START_TIME, System.currentTimeMillis());
        set(map);
    }

    public String getRequestHeader(String headerName) {
        return getHttpServletRequest().getHeader(headerName);
    }

    public void setResponseHeader(String headerName, String headerValue) {
        getHttpServletResponse().setHeader(headerName, headerValue);
    }

    public HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse)get(RESPONSE);
    }

    public HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest)get(REQUEST);
    }

    public long getStartTimeStamp() {
        return (long)get(START_TIME);
    }


}
