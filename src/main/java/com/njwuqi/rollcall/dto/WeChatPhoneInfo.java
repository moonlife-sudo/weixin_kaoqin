package com.njwuqi.rollcall.dto;

import lombok.Data;

public class WeChatPhoneInfo {
    // 用户绑定的手机号（国外手机号会有区号）
    private String phoneNumber = "";
    // 没有区号的手机号
    private String purePhoneNumber = "";
    // 区号
    private String countryCode = "";
    // 数据水印
    private String watermark = "";

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPurePhoneNumber() {
        return purePhoneNumber;
    }

    public void setPurePhoneNumber(String purePhoneNumber) {
        this.purePhoneNumber = purePhoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }
}
