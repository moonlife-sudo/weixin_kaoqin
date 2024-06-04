package com.njwuqi.rollcall.dto;

import java.util.List;

public class StudentRollCallInforesult {
    private int code;
    private String message;
    private List<RecordinfoDisplay> recordinfoDisplays;

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

    public List<RecordinfoDisplay> getRecordinfoDisplays() {
        return recordinfoDisplays;
    }

    public void setRecordinfoDisplays(List<RecordinfoDisplay> recordinfoDisplays) {
        this.recordinfoDisplays = recordinfoDisplays;
    }
}
