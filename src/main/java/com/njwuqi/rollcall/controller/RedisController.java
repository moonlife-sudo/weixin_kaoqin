package com.njwuqi.rollcall.controller;

import com.njwuqi.rollcall.entity.Openid;
import com.njwuqi.rollcall.entity.OperationResult;
import com.njwuqi.rollcall.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RedisController {
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/redisSetOpenid")
    @ResponseBody
    public OperationResult redisSetOpenid(@RequestBody Openid openid){
        boolean result = redisUtil.set(openid.getOpenid(),openid);
        OperationResult operationResult = new OperationResult();
        if (result) {
            operationResult.setCode(0);
            operationResult.setMessage("redis新增openid成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("redis新增openid失败！");
        }
        return operationResult;
    }
    @PostMapping("/redisGetOpenid")
    @ResponseBody
    public Openid redisGetOpenid(@RequestBody Openid openid){
        Openid result = (Openid)redisUtil.get(openid.getOpenid());
        if(result == null){
            result = new Openid();
        }
        return result;
    }

    @PostMapping("/redisDelOpenid")
    @ResponseBody
    public OperationResult redisDelOpenid(@RequestBody Openid openid){
        redisUtil.del(openid.getOpenid());
        OperationResult operationResult = new OperationResult();
        operationResult.setCode(0);
        operationResult.setMessage("redis删除openid成功！");
        return operationResult;
    }
}
