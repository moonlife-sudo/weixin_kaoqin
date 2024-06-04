package com.njwuqi.rollcall.configuration;

import com.njwuqi.rollcall.StreamResultService.StopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableAsync        // 2.开启多线程
@EnableScheduling   // 2.开启定时任务
public class ScheduleTask {
    private static Logger logger = LoggerFactory.getLogger(ScheduleTask.class);
    @Autowired
    StopService stopService;
    //添加定时任务 每天的23点执行
    // 每天23点执行一次：0 0 23 * * ?
    // 注意不能写成24 会报错 0到23
    // 0 0 1 * * ?
    @Async
    @Scheduled(cron = "0 0 23 * * ?")
    void requirementTasks(){
        logger.info("定时任务开始将redis中今天新增数据同步到mysql中去....");
        try {
            stopService.redisToMysql();
        } catch (Exception e){
            e.printStackTrace();
        }
        logger.info("定时任务结束将redis中今天新增数据同步到mysql中去....");

    }
}