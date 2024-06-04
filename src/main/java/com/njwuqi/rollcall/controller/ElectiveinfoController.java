package com.njwuqi.rollcall.controller;

import com.njwuqi.rollcall.entity.Electiveinfo;
import com.njwuqi.rollcall.entity.OperationResult;
import com.njwuqi.rollcall.service.ElectiveinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ElectiveinfoController {
    @Autowired
    private ElectiveinfoService electiveinfoService;

    @PostMapping("/insertElectiveinfo")
    @ResponseBody
    public OperationResult insertElectiveinfo(@RequestBody Electiveinfo electiveinfo){
        int result = electiveinfoService.insertElectiveinfo(electiveinfo);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("新增班级学生成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("新增班级学生失败！");
        }
        return operationResult;
    }
    @PostMapping("/delElectiveinfo")
    @ResponseBody
    public OperationResult delElectiveinfo(@RequestBody Electiveinfo electiveinfo){
        int result = electiveinfoService.delElectiveinfo(electiveinfo.getId());
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("删除班级学生成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("删除班级学生失败！");
        }
        return operationResult;
    }

    @PostMapping("/updateElectiveinfo")
    @ResponseBody
    public OperationResult updateUserinfo(@RequestBody Electiveinfo electiveinfo){
        int result = electiveinfoService.updateElectiveinfo(electiveinfo);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("更新班级学生成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("更新班级学生失败！");
        }
        return operationResult;
    }

    @PostMapping("/queryElectiveinfoById")
    @ResponseBody
    public Electiveinfo queryElectiveinfoById(@RequestBody Electiveinfo electiveinfo){
        Electiveinfo result = electiveinfoService.queryElectiveinfoById(electiveinfo.getId());
        if(result == null){
            result = new Electiveinfo();
        }
        return result;
    }
}
