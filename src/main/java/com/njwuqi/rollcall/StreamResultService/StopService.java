package com.njwuqi.rollcall.StreamResultService;

import com.njwuqi.rollcall.StreamResultHandler.TRecordinfoResultHandler;
import com.njwuqi.rollcall.StreamResultHandler.TStatisticsinfoResultHandler;
import com.njwuqi.rollcall.businesscontroller.RollCallController;
import com.njwuqi.rollcall.entity.*;
import com.njwuqi.rollcall.mapper.RedisStreamMapper;
import com.njwuqi.rollcall.service.RecordinfoService;
import com.njwuqi.rollcall.service.StatisticsinfoService;
import com.njwuqi.rollcall.utils.RedisConst;
import com.njwuqi.rollcall.utils.RedisUtil;
import com.njwuqi.rollcall.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StopService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RecordinfoService recordinfoService;
    @Autowired
    private StatisticsinfoService statisticsinfoService;
    @Autowired
    private RollCallController rollCallController;
    @Autowired
    private RedisStreamMapper redisStreamMapper;
    @Autowired
    TRecordinfoResultHandler tRecordinfoResultHandler;
    @Autowired
    TStatisticsinfoResultHandler tStatisticsinfoResultHandler;
    public void redisToMysql() throws Exception {
        // 同步redis今天考勤记录到数据库
        Set<String> recordinfoKeys = redisUtil.qianzui(RedisConst.TODAY_RECORDINFO);
        if (recordinfoKeys != null) {
            Iterator iterator = recordinfoKeys.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Recordinfo recordinfo = (Recordinfo) redisUtil.get(key);
                if (recordinfo != null) {
                    recordinfoService.insertRecordinfo(recordinfo);
                }
            }
        }

        // 计算今天考勤统计，并同步到数据库
        // 1、从reddis查出今天的规则
        // 2、统计创建对象
        // 3、插入数据库
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Set<String> ruleinfoKeys = redisUtil.qianzui(RedisConst.RULEINFOS);
        if (ruleinfoKeys != null) {
            Iterator iterator = ruleinfoKeys.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Set<Object> ruleinfos = redisUtil.sGet(key);
                if(ruleinfos == null){
                    continue;
                }
                for (Object o : ruleinfos) {
                    Ruleinfo ruleinfo = (Ruleinfo) o;
                    Date start = simpleDateFormat.parse(ruleinfo.getStartTime());
                    Date end = simpleDateFormat.parse(ruleinfo.getFinalTime());
                    if (Utils.isNow(start) && Utils.isNow(end)) {
                        // 统计数据都为0或空，前面把RedisConst.RULEINFOS删除了 要修改
                        Statisticsinfo statisticsinfo = rollCallController.statistic(ruleinfo.getCourseNumber(), ruleinfo.getRuleNumber());
                        if(statisticsinfo != null){
                            statisticsinfoService.insertStatisticsinfo(statisticsinfo);
                        }

                    }
                }
            }
        }

        // 删除今天的redis考勤记录
        Set<String> todayrecordinfoKeys = redisUtil.qianzui(RedisConst.TODAY_RECORDINFO);
        if (todayrecordinfoKeys != null) {
            for (String key:todayrecordinfoKeys) {
                redisUtil.del(key);
            }
        }
        // 删除非今天的redis考勤记录
        Set<String> recordinfosKeys = redisUtil.qianzui(RedisConst.RECORDINFO);
        if (recordinfosKeys != null) {
            for (String recordinfosKey: recordinfosKeys) {
                redisUtil.del(recordinfosKey);
            }
        }
        // 删除redis考勤统计数据
        Set<String> statisticsinfoKeys = redisUtil.qianzui(RedisConst.STATISTICSINFO);
        if (statisticsinfoKeys != null) {
            for (String statisticsinfoKey: statisticsinfoKeys) {
                redisUtil.del(statisticsinfoKey);
            }
        }

        // 将表中考勤记录同步到redis
        redisStreamMapper.queryStreamALLRecordinfo(tRecordinfoResultHandler);
        // 将表中考勤统计同步到redis
        redisStreamMapper.queryStreamALLStatisticsinfo(tStatisticsinfoResultHandler);
    }
}