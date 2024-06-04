package com.njwuqi.rollcall.service;

import com.njwuqi.rollcall.entity.Ruleinfo;
import com.njwuqi.rollcall.mapper.RuleinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class RuleinfoServiceImpl implements RuleinfoService {
    @Autowired
   private RuleinfoMapper ruleinfoMapper;

    @Override
    public int insertRuleinfo(Ruleinfo ruleinfo) {
        return ruleinfoMapper.insertRuleinfo(ruleinfo);
    }

    @Override
    public int delRuleinfo(long ruleNumber) {
        return ruleinfoMapper.delRuleinfo(ruleNumber);
    }

    @Override
    public int updateRuleinfo(Ruleinfo ruleinfo) {
        return ruleinfoMapper.updateRuleinfo(ruleinfo);
    }

    @Override
    public Ruleinfo queryRuleinfoById(long ruleNumber) {
        return ruleinfoMapper.queryRuleinfoById(ruleNumber);
    }
}
