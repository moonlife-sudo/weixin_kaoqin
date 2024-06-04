package com.njwuqi.rollcall.mapper;
import com.njwuqi.rollcall.entity.Ruleinfo;

public interface RuleinfoMapper {
    int insertRuleinfo(Ruleinfo ruleinfo);
    int delRuleinfo(long ruleNumber);
    int updateRuleinfo(Ruleinfo ruleinfo);
    Ruleinfo queryRuleinfoById(long ruleNumber);
}
