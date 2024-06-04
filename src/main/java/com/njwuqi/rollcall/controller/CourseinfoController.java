package com.njwuqi.rollcall.controller;

import com.njwuqi.rollcall.entity.Courseinfo;
import com.njwuqi.rollcall.entity.OperationResult;
import com.njwuqi.rollcall.service.CourseinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CourseinfoController {
    @Autowired
    private CourseinfoService courseinfoService;

    @PostMapping("/insertCourseinfo")
    @ResponseBody
    public OperationResult insertCourseinfo(@RequestBody Courseinfo courseinfo){
        int result = courseinfoService.insertCourseinfo(courseinfo);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("新增班级成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("新增班级失败！");
        }
        return operationResult;
    }
    @PostMapping("/delCourseinfo")
    @ResponseBody
    public OperationResult delCourseinfo(@RequestBody Courseinfo courseinfo){
        int result = courseinfoService.delCourseinfo(courseinfo.getCourseNumber());
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("删除班级成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("删除班级失败！");
        }
        return operationResult;
    }

    @PostMapping("/updateCourseinfo")
    @ResponseBody
    public OperationResult updateCourseinfo(@RequestBody Courseinfo courseinfo){
        int result = courseinfoService.updateCourseinfo(courseinfo);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("更新班级成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("更新班级失败！");
        }
        return operationResult;
    }

    @PostMapping("/queryCourseinfoById")
    @ResponseBody
    public Courseinfo queryCourseinfoById(@RequestBody Courseinfo courseinfo){
        Courseinfo result = courseinfoService.queryCourseinfoById(courseinfo.getCourseNumber());
        if(result == null){
            result = new Courseinfo();
        }
        return result;
    }
}
