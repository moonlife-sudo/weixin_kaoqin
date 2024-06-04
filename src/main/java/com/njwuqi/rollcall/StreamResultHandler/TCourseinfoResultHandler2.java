package com.njwuqi.rollcall.StreamResultHandler;
import com.njwuqi.rollcall.entity.Courseinfo;
import com.njwuqi.rollcall.entity.Courseinfo2;
import com.njwuqi.rollcall.utils.RedisConst;
import com.njwuqi.rollcall.utils.RedisUtil;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  通过流式查询每获得一条数据的回调类
 */
@Component
public class TCourseinfoResultHandler2 implements ResultHandler<Courseinfo2> {
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 每从流式查询中获得一行结果，就会调用一次这个方法
     * @param resultContext
     */
    @Override
    public void handleResult(ResultContext<? extends Courseinfo2> resultContext){
        // 这里获取流式查询每次返回的单条结果
        Courseinfo2 resultObject = resultContext.getResultObject();
        Courseinfo2 courseinfo2 = resultObject;
        // 你可以看自己的项目需要分批进行处理或者单个处理，这里以分批处理为例
        Courseinfo courseinfo = new Courseinfo();
        courseinfo.setCourseNumber(courseinfo2.getCourseNumber());
        courseinfo.setCourseName(courseinfo2.getCourseName());
        courseinfo.setPhone(courseinfo2.getPhone());
        courseinfo.setState(courseinfo2.getState());
        courseinfo.setAddressName(courseinfo2.getAddressName());
        courseinfo.setCreateTime(courseinfo2.getCreateTime());
        // 存放学生的课程信息，手机号 班级set
        redisUtil.sSet(RedisConst.COURSEINFOS+":"+courseinfo2.getStudentphone(), courseinfo);

    }
}