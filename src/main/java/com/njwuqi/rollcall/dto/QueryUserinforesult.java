package com.njwuqi.rollcall.dto;

import com.njwuqi.rollcall.entity.Userinfo;

public class QueryUserinforesult {
    private int code;
    private String message;
    private Userinfo userinfo;

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

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }
}
