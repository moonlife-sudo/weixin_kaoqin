package com.njwuqi.rollcall.controller;

import com.njwuqi.rollcall.entity.OperationResult;
import com.njwuqi.rollcall.entity.Recordinfo;
import com.njwuqi.rollcall.entity.Userinfo;
import com.njwuqi.rollcall.service.RecordinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RecordinfoController {
    @Autowired
    private RecordinfoService recordinfoService;

    @PostMapping("/insertRecordinfo")
    @ResponseBody
    public OperationResult insertRecordinfo(@RequestBody Recordinfo recordinfo){
        int result = recordinfoService.insertRecordinfo(recordinfo);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("新增考勤记录成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("新增考勤记录失败！");
        }
        return operationResult;
    }
    @PostMapping("/delRecordinfo")
    @ResponseBody
    public OperationResult delRecordinfo(@RequestBody Recordinfo recordinfo){
        int result = recordinfoService.delRecordinfo(recordinfo.getRecordNumber());
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("删除考勤记录成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("删除考勤记录失败！");
        }
        return operationResult;
    }

    @PostMapping("/queryRecordinfoById")
    @ResponseBody
    public Recordinfo queryRecordinfoById(@RequestBody Recordinfo recordinfo){
        Recordinfo result = recordinfoService.queryRecordinfoById(recordinfo.getRecordNumber());
        if(result == null){
            result = new Recordinfo();
        }
        return result;
    }
    @PostMapping("/updateRecordinfo")
    @ResponseBody
    public OperationResult updateRecordinfo(@RequestBody Recordinfo recordinfo){
        int result = recordinfoService.updateRecordinfo(recordinfo);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("更新考勤记录成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("更新考勤记录失败！");
        }
        return operationResult;
    }
}
