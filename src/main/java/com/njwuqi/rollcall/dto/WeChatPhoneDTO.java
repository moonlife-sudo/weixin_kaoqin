package com.njwuqi.rollcall.dto;

public class WeChatPhoneDTO {
    // getPhoneNumber接口返回的code
    private String code;
    // 小程序的appid（一般是在程序中配置，不需要前端传参）
    private String appid;
    // 小程序的secretKey（一般是在程序中配置，不需要前端传参）
    private String secretKey;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}