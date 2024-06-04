package com.njwuqi.rollcall.controller;

import com.njwuqi.rollcall.StreamResultService.StartService;
import com.njwuqi.rollcall.StreamResultService.StopService;
import com.njwuqi.rollcall.entity.OperationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StopController {
    @Autowired
    StopService stopService;

    @PostMapping("/stopServer")
    @ResponseBody
    public OperationResult initServerStart(){
        try {
            stopService.redisToMysql();
        } catch (Exception e){
            e.printStackTrace();
        }
        OperationResult operationResult = new OperationResult();
        operationResult.setCode(0);
        operationResult.setMessage("init服务关闭成功！");
        return operationResult;
    }
}
