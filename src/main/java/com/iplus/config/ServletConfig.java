package com.iplus.config;

import com.iplus.common.MultiReadHttpServletRequest;
import com.iplus.wechat.api.MvcConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class ServletConfig {
    @Bean
    @Qualifier("wisteria")
    public ServletRegistrationBean frontendServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet(){
            @Override
            protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
                MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest(request);
                super.doDispatch(multiReadRequest, response);
            }
        };
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(MvcConfig.class);
        dispatcherServlet.setApplicationContext(context);

        ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(dispatcherServlet, "/*");
        servletRegistrationBean.setName("wx");
        servletRegistrationBean.setLoadOnStartup(1);

        return servletRegistrationBean;
    }
}
