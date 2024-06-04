package com.njwuqi.rollcall.service;

import com.njwuqi.rollcall.entity.Statisticsinfo;
import com.njwuqi.rollcall.mapper.StatisticsinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class StatisticsinfoServiceImpl implements StatisticsinfoService {
    @Autowired
   private StatisticsinfoMapper statisticsinfoMapper;

    @Override
    public int insertStatisticsinfo(Statisticsinfo statisticsinfo) {
        return statisticsinfoMapper.insertStatisticsinfo(statisticsinfo);
    }

    @Override
    public int delStatisticsinfo(long statisticsNumber) {
        return statisticsinfoMapper.delStatisticsinfo(statisticsNumber);
    }

    @Override
    public int updateStatisticsinfo(Statisticsinfo statisticsinfo) {
        return statisticsinfoMapper.updateStatisticsinfo(statisticsinfo);
    }

    @Override
    public Statisticsinfo queryStatisticsinfoById(long statisticsNumber) {
        return statisticsinfoMapper.queryStatisticsinfoById(statisticsNumber);
    }
}
