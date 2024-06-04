package com.njwuqi.rollcall.service;

import com.njwuqi.rollcall.entity.Openid;
import com.njwuqi.rollcall.entity.Userinfo;
import com.njwuqi.rollcall.mapper.OpenidMapper;
import com.njwuqi.rollcall.mapper.UserinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class OpenidServiceImpl implements OpenidService {
    @Autowired
   private OpenidMapper openidMapper;

    @Override
    public int insertOpenid(Openid openid) {
        return openidMapper.insertOpenid(openid);
    }

    @Override
    public int delOpenid(String openid) {
        return openidMapper.delOpenid(openid);
    }

    @Override
    public int updateOpenid(Openid openid) {
        return openidMapper.updateOpenid(openid);
    }

    @Override
    public Openid queryOpenidByOpenid(String openid) {
        return openidMapper.queryOpenidByOpenid(openid);
    }
}
