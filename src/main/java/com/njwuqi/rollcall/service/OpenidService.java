package com.njwuqi.rollcall.service;
import com.njwuqi.rollcall.entity.Openid;

public interface OpenidService {
    int insertOpenid(Openid openid);
    int delOpenid(String openid);
    int updateOpenid(Openid openid);
    Openid queryOpenidByOpenid(String openid);
}
