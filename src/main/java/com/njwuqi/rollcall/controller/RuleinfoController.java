package com.njwuqi.rollcall.controller;

import com.njwuqi.rollcall.entity.OperationResult;
import com.njwuqi.rollcall.entity.Ruleinfo;
import com.njwuqi.rollcall.service.RuleinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RuleinfoController {
    @Autowired
    private RuleinfoService ruleinfoService;

    @PostMapping("/insertRuleinfo")
    @ResponseBody
    public OperationResult insertRuleinfo(@RequestBody Ruleinfo ruleinfo){
        int result = ruleinfoService.insertRuleinfo(ruleinfo);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("新增规则成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("新增规则失败！");
        }
        return operationResult;
    }
    @PostMapping("/delRuleinfo")
    @ResponseBody
    public OperationResult delRuleinfo(@RequestBody Ruleinfo ruleinfo){
        int result = ruleinfoService.delRuleinfo(ruleinfo.getRuleNumber());
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("删除规则成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("删除规则失败！");
        }
        return operationResult;
    }

    @PostMapping("/updateRuleinfo")
    @ResponseBody
    public OperationResult updateRuleinfo(@RequestBody Ruleinfo ruleinfo){
        int result = ruleinfoService.updateRuleinfo(ruleinfo);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("更新规则成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("更新规则失败！");
        }
        return operationResult;
    }

    @PostMapping("/queryRuleinfoById")
    @ResponseBody
    public Ruleinfo queryRuleinfoById(@RequestBody Ruleinfo ruleinfo){
        Ruleinfo result = ruleinfoService.queryRuleinfoById(ruleinfo.getRuleNumber());
        if(result == null){
            result = new Ruleinfo();
        }
        return result;
    }
}
