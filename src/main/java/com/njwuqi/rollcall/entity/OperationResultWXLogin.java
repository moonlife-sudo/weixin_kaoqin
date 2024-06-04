package com.njwuqi.rollcall.entity;

public class OperationResultWXLogin {
    private int code;
    private String message;
    private String openid;
    private String sessionid;

    @Override
    public String toString() {
        return "OperationResultWXLogin{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", openid='" + openid + '\'' +
                ", sessionid='" + sessionid + '\'' +
                '}';
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
