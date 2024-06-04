package com.njwuqi.rollcall.StreamResultHandler;
import com.njwuqi.rollcall.entity.Userinfo;
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
public class TUserinfoResultHandler implements ResultHandler<Userinfo> {
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 每从流式查询中获得一行结果，就会调用一次这个方法
     * @param resultContext
     */
    @Override
    public void handleResult(ResultContext<? extends Userinfo> resultContext){
        // 这里获取流式查询每次返回的单条结果
        Userinfo resultObject = resultContext.getResultObject();
        Userinfo userinfo = resultObject;
        // 你可以看自己的项目需要分批进行处理或者单个处理，这里以分批处理为例
        redisUtil.set(RedisConst.USERINFO+userinfo.getPhone(), userinfo);
    }
}