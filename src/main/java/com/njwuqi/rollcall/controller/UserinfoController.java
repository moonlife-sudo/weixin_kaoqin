package com.njwuqi.rollcall.controller;

import com.njwuqi.rollcall.entity.OperationResult;
import com.njwuqi.rollcall.entity.Userinfo;
import com.njwuqi.rollcall.service.UserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserinfoController {
    @Autowired
    private UserinfoService userinfoService;

    @PostMapping("/insertUserinfo")
    @ResponseBody
    public OperationResult insertUserinfo(@RequestBody Userinfo userinfo){
        int result = userinfoService.insertUserinfo(userinfo);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("新增用户成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("新增用户失败！");
        }
        return operationResult;
    }
    @PostMapping("/delUserinfo")
    @ResponseBody
    public OperationResult delUserinfo(@RequestBody Userinfo userinfo){
        int result = userinfoService.delUserinfo(userinfo.getPhone());
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("删除用户成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("删除用户失败！");
        }
        return operationResult;
    }

    @PostMapping("/updateUserinfo")
    @ResponseBody
    public OperationResult updateUserinfo(@RequestBody Userinfo userinfo){
        int result = userinfoService.updateUserinfo(userinfo);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("更新用户成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("更新用户失败！");
        }
        return operationResult;
    }

    @PostMapping("/queryUserinfoByPhone")
    @ResponseBody
    public Userinfo queryUserinfoByPhone(@RequestBody Userinfo userinfo){
        Userinfo result = userinfoService.queryUserinfoByPhone(userinfo.getPhone());
        if(result == null){
            result = new Userinfo();
        }
        return result;
    }
}
