package com.njwuqi.rollcall.service;


import com.njwuqi.rollcall.entity.Userinfo;

public interface UserinfoService {
    int insertUserinfo(Userinfo userinfo);
    int delUserinfo(long phone);
    int updateUserinfo(Userinfo userinfo);
    Userinfo queryUserinfoByPhone(long phone);
}
