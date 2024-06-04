package com.njwuqi.rollcall.controller;

import com.njwuqi.rollcall.StreamResultService.StartService;
import com.njwuqi.rollcall.entity.OperationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InitController {
    @Autowired
    StartService startServiceRedisService;

    @PostMapping("/initServerStart")
    @ResponseBody
    public OperationResult initServerStart(){
        startServiceRedisService.streamQuery();
        OperationResult operationResult = new OperationResult();
        operationResult.setCode(0);
        operationResult.setMessage("init服务启动成功！");
        return operationResult;
    }
}
