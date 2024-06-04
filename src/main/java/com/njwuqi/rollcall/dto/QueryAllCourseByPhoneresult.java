package com.njwuqi.rollcall.dto;

import com.njwuqi.rollcall.entity.Courseinfo;

import java.util.List;

public class QueryAllCourseByPhoneresult {
    private int code;
    private String message;
    private List<Courseinfo> courseinfos;

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

    public List<Courseinfo> getCourseinfos() {
        return courseinfos;
    }

    public void setCourseinfos(List<Courseinfo> courseinfos) {
        this.courseinfos = courseinfos;
    }
}
