package com.iplus.wechat.common.request;

import com.iplus.wechat.common.utils.LogUtils;
import com.iplus.wechat.common.utils.RequestContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author zhangnan
 * @Date 19-6-10
 **/
@Component
public class RequestContextInterceptor extends HandlerInterceptorAdapter {

    private final static AtomicInteger index = new AtomicInteger(1);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        int requestId = index.getAndIncrement();
        RequestContext.getRequestContext().init(request, response);
        RequestContext.getRequestContext().setRequestId(requestId);
        response.setHeader(LogUtils.REQUEST_ID_PREFIX, String.valueOf(requestId));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {

        LogUtils.request();
        RequestContext.getRequestContext().remove();
    }
}