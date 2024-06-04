package com.njwuqi.rollcall.mapper;
import com.njwuqi.rollcall.entity.Userinfo;

public interface UserinfoMapper {
    int insertUserinfo(Userinfo userinfo);
    int delUserinfo(long phone);
    int updateUserinfo(Userinfo userinfo);
    Userinfo queryUserinfoByPhone(long phone);
}
