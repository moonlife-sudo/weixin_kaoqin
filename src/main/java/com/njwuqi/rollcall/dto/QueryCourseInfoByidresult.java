package com.njwuqi.rollcall.dto;

import com.njwuqi.rollcall.entity.Courseinfo;

public class QueryCourseInfoByidresult {
    private int code;
    private String message;
    private Courseinfo courseinfo;

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

    public Courseinfo getCourseinfo() {
        return courseinfo;
    }

    public void setCourseinfo(Courseinfo courseinfo) {
        this.courseinfo = courseinfo;
    }
}
