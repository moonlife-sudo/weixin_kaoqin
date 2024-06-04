package com.njwuqi.rollcall.controller;

import com.njwuqi.rollcall.entity.OperationResult;
import com.njwuqi.rollcall.entity.Statisticsinfo;
import com.njwuqi.rollcall.service.StatisticsinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StatisticsinfoController {
    @Autowired
    private StatisticsinfoService statisticsinfoService;

    @PostMapping("/insertStatisticsinfo")
    @ResponseBody
    public OperationResult insertStatisticsinfo(@RequestBody Statisticsinfo statisticsinfo){
        int result = statisticsinfoService.insertStatisticsinfo(statisticsinfo);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("新增考勤统计成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("新增考勤统计失败！");
        }
        return operationResult;
    }
    @PostMapping("/delStatisticsinfo")
    @ResponseBody
    public OperationResult delStatisticsinfo(@RequestBody Statisticsinfo statisticsinfo){
        int result = statisticsinfoService.delStatisticsinfo(statisticsinfo.getStatisticsNumber());
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("删除考勤统计成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("删除考勤统计失败！");
        }
        return operationResult;
    }

    @PostMapping("/updateStatisticsinfo")
    @ResponseBody
    public OperationResult updateStatisticsinfo(@RequestBody Statisticsinfo statisticsinfo){
        int result = statisticsinfoService.updateStatisticsinfo(statisticsinfo);
        OperationResult operationResult = new OperationResult();
        if (result == 1) {
            operationResult.setCode(0);
            operationResult.setMessage("更新考勤统计成功！");
        } else {
            operationResult.setCode(1);
            operationResult.setMessage("更新考勤统计失败！");
        }
        return operationResult;
    }

    @PostMapping("/queryStatisticsinfoById")
    @ResponseBody
    public Statisticsinfo queryStatisticsinfoById(@RequestBody Statisticsinfo statisticsinfo){
        Statisticsinfo result = statisticsinfoService.queryStatisticsinfoById(statisticsinfo.getStatisticsNumber());
        if(result == null){
            result = new Statisticsinfo();
        }
        return result;
    }
}
