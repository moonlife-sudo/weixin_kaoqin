package com.njwuqi.rollcall.dto;

import com.njwuqi.rollcall.entity.Statisticsinfos;

public class TeacherRollALLCallInforesult {
    private int code;
    private String message;
    private Statisticsinfos statisticsinfos;

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

    public Statisticsinfos getStatisticsinfos() {
        return statisticsinfos;
    }

    public void setStatisticsinfos(Statisticsinfos statisticsinfos) {
        this.statisticsinfos = statisticsinfos;
    }
}
