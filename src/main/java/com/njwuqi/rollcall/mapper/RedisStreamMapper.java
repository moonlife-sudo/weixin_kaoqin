package com.njwuqi.rollcall.mapper;

import com.njwuqi.rollcall.entity.*;
import org.apache.ibatis.session.ResultHandler;

public interface RedisStreamMapper {
    void queryStreamALLOpenid(ResultHandler<Openid> handler);
    void queryStreamALLUserinfo(ResultHandler<Userinfo> handler);
    void queryStreamALLCourseinfo(ResultHandler<Courseinfo> handler);
    void queryStreamALLRuleinfo(ResultHandler<Ruleinfo> handler);
    void queryStreamALLCourseinfoStudent(ResultHandler<Courseinfo2> handler);
    void queryStreamALLRecordinfo(ResultHandler<Recordinfo> handler);
    void queryStreamALLElectiveinfo(ResultHandler<Electiveinfo> handler);
    void queryStreamALLStatisticsinfo(ResultHandler<Statisticsinfo> handler);
}
