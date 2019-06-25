package com.iplus.wechat.api;


import com.iplus.wechat.service.WxApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/")
public class WechatController {

    private WxApiService wxApiService;

    @Autowired
    public WechatController(WxApiService wxApiService) {

        this.wxApiService = wxApiService;
    }

    @GetMapping("test")
    public String test() {
        return "This is iplus";
    }

    @GetMapping("access_token")
    public String accessToken() {
        return wxApiService.getAccessToken();
    }
}