package com.njwuqi.rollcall.StreamResultService;

import com.njwuqi.rollcall.StreamResultHandler.*;
import com.njwuqi.rollcall.mapper.RedisStreamMapper;
import com.njwuqi.rollcall.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisStreamMapper redisStreamMapper;
    @Autowired
    TOpenidResultHandler tOpenidResultHandler;
    @Autowired
    TUserinfoResultHandler tUserinfoResultHandler;
    @Autowired
    TCourseinfoResultHandler tCourseinfoResultHandler;
    @Autowired
    TCourseinfoResultHandler2 tCourseinfoResultHandler2;
    @Autowired
    TRecordinfoResultHandler tRecordinfoResultHandler;
    @Autowired
    TStatisticsinfoResultHandler tStatisticsinfoResultHandler;
    @Autowired
    TRuleinfoResultHandler tRuleinfoResultHandler;
    @Autowired
    TElectiveinfoResultHandler tElectiveinfoResultHandler;
    public void streamQuery(){
        // 删除全部key
        redisUtil.removeAllKey();
        // 调用流式查询
        // 一次性将mysql中所有openid加入到redis
        redisStreamMapper.queryStreamALLOpenid(tOpenidResultHandler);
        // 一次性将mysql中所有userinfo加入到redis
        redisStreamMapper.queryStreamALLUserinfo(tUserinfoResultHandler);
        // 一次性将mysql中所有courseinfo加入到redis  set
        // 			key：courseInfos
        //			value:所有班级列表
        // 和  老师手机号 对应的班级列表
        redisStreamMapper.queryStreamALLCourseinfo(tCourseinfoResultHandler);
        //   学生手机号 对应的班级列表
        redisStreamMapper.queryStreamALLCourseinfoStudent(tCourseinfoResultHandler2);
        // 统计    key：StateInfo：手机号：班级号
        //			value：考勤统计列表（以时间降序）
        redisStreamMapper.queryStreamALLRecordinfo(tRecordinfoResultHandler);
        // 课程号 考勤统计
        redisStreamMapper.queryStreamALLStatisticsinfo(tStatisticsinfoResultHandler);
        // 班级号 考勤规则
        redisStreamMapper.queryStreamALLRuleinfo(tRuleinfoResultHandler);
        // 班级对应所有学生信息
        redisStreamMapper.queryStreamALLElectiveinfo(tElectiveinfoResultHandler);
    }
}
