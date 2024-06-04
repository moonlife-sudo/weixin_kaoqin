package com.njwuqi.rollcall.configuration;

import com.njwuqi.rollcall.StreamResultService.StartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationReadyEventListener.class);
    @Autowired
    StartService startServiceRedisService;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent contextReadyEvent) {
        logger.info("开始将mysql中数据加载到redis中去....");
        startServiceRedisService.streamQuery();
        logger.info("结束将mysql中数据加载到redis中去....");
    }
}