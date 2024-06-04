package com.njwuqi.rollcall.utils;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class WechatUtil {
    private static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);
    // TODO
    public static final String APPID= "wx401a7e42c3586a24";
    // TODO
    public static final String SECRET= "0dfc401891a715157092419624911ad8";
    public static JSONObject getSessionKeyOrOpenId(String code) {
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> requestUrlParam = new HashMap<>();
        // https://mp.weixin.qq.com/wxopen/devprofile?action=get_profile&token=164113089&lang=zh_CN
        //小程序appId
        requestUrlParam.put("appid", APPID);
//        requestUrlParam.put("appid", appid);
        //小程序secret
        requestUrlParam.put("secret", SECRET);
//        requestUrlParam.put("secret", secret);
        //小程序端返回的code
        requestUrlParam.put("js_code", code);
        //默认参数
        requestUrlParam.put("grant_type", "authorization_code");
        //发送post请求读取调用微信接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject(HttpClientUtil.doPost(requestUrl, requestUrlParam));
        return jsonObject;
    }
    /**
     * 请求微信接口服务，获取小程序全局唯一后台接口调用凭据（access_token）
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/access-token/auth.getAccessToken.html
     *
     * @param appid
     * @param secretKey
     * @return
     */
    public static JSONObject getAccessToken(String appid, String secretKey) {
        String result = null;
        try {
            String baseUrl = "https://api.weixin.qq.com/cgi-bin/token";
            Map<String, String> requestParam = new HashMap<>();
            // 小程序 appId
            requestParam.put("grant_type", "client_credential");
            // 小程序唯一凭证id appid:(换成自己的)
            requestParam.put("appid", appid);
            // 小程序 appSecret（小程序的唯一凭证密钥,换成自己的）
            requestParam.put("secret", secretKey);
            // 发送GET请求读取调用微信接口获取openid用户唯一标识
            result = HttpClientUtil.doGet(baseUrl, requestParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.parseObject(result);
    }

    /**
     * 请求微信接口服务，用code换取用户手机号（每个code只能使用一次，code的有效期为5min）
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/phonenumber/phonenumber.getPhoneNumber.html
     *
     * @param code
     * @param accessToken
     * @return
     */
    public static JSONObject getPhoneNumber(String code, String accessToken) {
        String result = null;
        try {
            // 接口调用凭证：accessToken
            String baseUrl = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken;
            Map<String, String> requestParam = new HashMap<>();
            // 手机号调用凭证
            requestParam.put("code", code);
            // 发送post请求读取调用微信接口获取openid用户唯一标识
            String jsonStr = JSONUtil.toJsonStr(requestParam);
            HttpResponse response = HttpRequest.post(baseUrl)
                    .header(Header.CONTENT_ENCODING, "UTF-8")
                    // 发送json数据需要设置contentType
                    .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .body(jsonStr)
                    .execute();
            if (response.getStatus() == HttpStatus.HTTP_OK) {
                result = response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.parseObject(result);
    }
}