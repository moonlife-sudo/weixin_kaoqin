package com.njwuqi.rollcall.mapper;
import com.njwuqi.rollcall.entity.Openid;

public interface OpenidMapper {
    int insertOpenid(Openid openid);
    int delOpenid(String openid);
    int updateOpenid(Openid openid);
    Openid queryOpenidByOpenid(String openid);
}
