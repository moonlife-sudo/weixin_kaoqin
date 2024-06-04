package com.njwuqi.rollcall.service;


import com.njwuqi.rollcall.entity.Ruleinfo;

public interface RuleinfoService {
    int insertRuleinfo(Ruleinfo ruleinfo);
    int delRuleinfo(long ruleNumber);
    int updateRuleinfo(Ruleinfo ruleinfo);
    Ruleinfo queryRuleinfoById(long ruleNumber);
}
