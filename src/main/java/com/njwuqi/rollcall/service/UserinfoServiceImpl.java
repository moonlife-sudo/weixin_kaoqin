package com.njwuqi.rollcall.service;

import com.njwuqi.rollcall.entity.Userinfo;
import com.njwuqi.rollcall.mapper.UserinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserinfoServiceImpl implements UserinfoService {
    @Autowired
   private UserinfoMapper userinfoMapper;

    @Override
    public int insertUserinfo(Userinfo userinfo) {
        return userinfoMapper.insertUserinfo(userinfo);
    }

    @Override
    public int delUserinfo(long phone) {
        return userinfoMapper.delUserinfo(phone);
    }

    @Override
    public int updateUserinfo(Userinfo userinfo) {
        return userinfoMapper.updateUserinfo(userinfo);
    }

    @Override
    public Userinfo queryUserinfoByPhone(long phone) {
        return userinfoMapper.queryUserinfoByPhone(phone);
    }
}
