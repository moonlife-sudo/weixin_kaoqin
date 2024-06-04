package com.njwuqi.rollcall.service;


import com.njwuqi.rollcall.entity.Statisticsinfo;

public interface StatisticsinfoService {
    int insertStatisticsinfo(Statisticsinfo statisticsinfo);
    int delStatisticsinfo(long statisticsNumber);
    int updateStatisticsinfo(Statisticsinfo statisticsinfo);
    Statisticsinfo queryStatisticsinfoById(long statisticsNumber);

}
