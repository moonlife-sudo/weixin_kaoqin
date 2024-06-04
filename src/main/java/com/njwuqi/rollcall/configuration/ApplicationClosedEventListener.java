package com.njwuqi.rollcall.configuration;

import com.njwuqi.rollcall.StreamResultService.StopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    private static Logger logger = LoggerFactory.getLogger(ApplicationClosedEventListener.class);
    @Autowired
    StopService stopService;
    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent){
        logger.info("开始将redis中今天新增数据同步到mysql中去....");
        try {
            stopService.redisToMysql();
        } catch (Exception e){
            e.printStackTrace();
        }
        logger.info("结束将redis中今天新增数据同步到mysql中去....");
    }
}
