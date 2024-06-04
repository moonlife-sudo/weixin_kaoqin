package com.njwuqi.rollcall.dto;

import java.util.List;

public class TeacherRollCallInfoByRuleNumberresult {
    private int code;
    private String message;
    private List<StatisticsinfoNowDisplay> statisticsinfoNowDisplays;
    private List<StatisticsinfoDisplay> statisticsinfoDisplays;

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

    public List<StatisticsinfoNowDisplay> getStatisticsinfoNowDisplays() {
        return statisticsinfoNowDisplays;
    }

    public void setStatisticsinfoNowDisplays(List<StatisticsinfoNowDisplay> statisticsinfoNowDisplays) {
        this.statisticsinfoNowDisplays = statisticsinfoNowDisplays;
    }

    public List<StatisticsinfoDisplay> getStatisticsinfoDisplays() {
        return statisticsinfoDisplays;
    }

    public void setStatisticsinfoDisplays(List<StatisticsinfoDisplay> statisticsinfoDisplays) {
        this.statisticsinfoDisplays = statisticsinfoDisplays;
    }
}
