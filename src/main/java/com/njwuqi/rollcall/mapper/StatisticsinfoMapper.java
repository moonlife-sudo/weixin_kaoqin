package com.njwuqi.rollcall.mapper;
import com.njwuqi.rollcall.entity.Statisticsinfo;

public interface StatisticsinfoMapper {
    int insertStatisticsinfo(Statisticsinfo statisticsinfo);
    int delStatisticsinfo(long statisticsNumber);
    int updateStatisticsinfo(Statisticsinfo statisticsinfo);
    Statisticsinfo queryStatisticsinfoById(long statisticsNumber);
}
