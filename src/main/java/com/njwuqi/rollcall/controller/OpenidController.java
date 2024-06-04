package com.njwuqi.rollcall.controller;

import com.njwuqi.rollcall.entity.Openid;
import com.njwuqi.rollcall.entity.OperationResult;
import com.njwuqi.rollcall.service.OpenidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OpenidController {
    @Autowired
    private OpenidService openidService;

    @PostMapping("/insertOpenid")
    @ResponseBody
    public OperationResult insertOpenid(@RequestBody Openid openid){
        int result = openidService.insertOpenid(openid);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("新增openid成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("新增openid失败！");
        }
        return operationResult;
    }
    @PostMapping("/delOpenid")
    @ResponseBody
    public OperationResult delOpenid(@RequestBody Openid openid){
        int result = openidService.delOpenid(openid.getOpenid());
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("删除openid成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("删除openid失败！");
        }
        return operationResult;
    }

    @PostMapping("/updateOpenid")
    @ResponseBody
    public OperationResult updateOpenid(@RequestBody Openid openid){
        int result = openidService.updateOpenid(openid);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("更新openid成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("更新openid失败！");
        }
        return operationResult;
    }

    @PostMapping("/queryOpenidByOpenid")
    @ResponseBody
    public Openid queryOpenidByOpenid(@RequestBody Openid openid){
        Openid result = openidService.queryOpenidByOpenid(openid.getOpenid());
        if(result == null){
            result = new Openid();
        }
        return result;
    }
}
