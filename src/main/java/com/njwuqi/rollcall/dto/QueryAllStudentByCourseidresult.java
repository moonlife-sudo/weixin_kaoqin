package com.njwuqi.rollcall.dto;

import com.njwuqi.rollcall.entity.Userinfo;

import java.util.List;

public class QueryAllStudentByCourseidresult {
    private int code;
    private String message;
    private List<Userinfo> userinfos;

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

    public List<Userinfo> getUserinfos() {
        return userinfos;
    }

    public void setUserinfos(List<Userinfo> userinfos) {
        this.userinfos = userinfos;
    }
}
