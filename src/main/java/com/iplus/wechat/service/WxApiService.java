package com.iplus.wechat.service;

import com.iplus.wechat.common.WechatApiConstants;
import com.iplus.wechat.common.utils.DesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Author zhangnan
 * @Date 19-6-10
 **/
@Component
public class WxApiService {

    public static final String SECRET_KEY = "iplus123";

    private String appId;
    private String secert;
    private RestTemplate restTemplate;


    @Autowired
    public WxApiService(@Value("${appid}") String appId, @Value("${secert}") String secert,
                        RestTemplate restTemplate) {
        this.appId = DesUtils.decrypt(appId, SECRET_KEY);
        this.secert = DesUtils.decrypt(secert, SECRET_KEY);
        this.restTemplate = restTemplate;
    }

    public String getAccessToken() {

        String accessToken = String.format(WechatApiConstants.WX_ACCESS_TOKEN_URL, appId, secert);
        ResponseEntity<String> response = restTemplate.getForEntity(accessToken, String.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            String result = response.getBody();
            return result;
        }
        return response.toString();
    }
}
