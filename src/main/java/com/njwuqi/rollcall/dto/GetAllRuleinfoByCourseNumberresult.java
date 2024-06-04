package com.njwuqi.rollcall.dto;

import com.njwuqi.rollcall.entity.Ruleinfo;
import com.njwuqi.rollcall.entity.Userinfo;

import java.util.List;

public class GetAllRuleinfoByCourseNumberresult {
    private int code;
    private String message;
    private List<Ruleinfo> ruleinfos;

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

    public List<Ruleinfo> getRuleinfos() {
        return ruleinfos;
    }

    public void setRuleinfos(List<Ruleinfo> ruleinfos) {
        this.ruleinfos = ruleinfos;
    }
}
