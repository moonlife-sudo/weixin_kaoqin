package com.njwuqi.rollcall.businesscontroller;

import com.alibaba.fastjson.JSONObject;
import com.njwuqi.rollcall.entity.*;
import com.njwuqi.rollcall.service.OpenidService;
import com.njwuqi.rollcall.utils.RedisConst;
import com.njwuqi.rollcall.utils.RedisUtil;
import com.njwuqi.rollcall.utils.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OpenidService openidService;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    /**
     * 第一次登录用（本地没有缓存openid的场合）
     * @return
     */
    @PostMapping("/wx/login")
    @ResponseBody
//    public OperationResultWXLogin user_login(@RequestParam(value = "code", required = false) String code,
//                                             @RequestParam(value = "rawData", required = false) String rawData,
//                                             @RequestParam(value = "signature", required = false) String signature) {
    public OperationResultWXLogin user_login(@RequestBody LoginData loginData) {
        logger.info("code:"+loginData.getCode());
//        logger.info("rawData:"+rawData);
//        logger.info("signature:"+signature);
//        // 用户非敏感信息：rawData
//        // 签名：signature
//        JSONObject rawDataJson = JSON.parseObject(rawData);
        // 1.接收小程序发送的code
        // 2.开发者服务器 登录凭证校验接口 appi + appsecret + code
        JSONObject SessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(loginData.getCode());
        // 3.接收微信接口服务 获取返回的参数
        String openid = SessionKeyOpenId.getString("openid");
        String sessionKey = SessionKeyOpenId.getString("session_key");
        logger.info("openid:"+openid);
        logger.info("sessionKey:"+sessionKey);
//        // 4.校验签名 小程序发送的签名signature与服务器端生成的签名signature2 = sha1(rawData + sessionKey)
//        String signature2 = DigestUtils.sha1Hex(rawData + sessionKey);
//        if (!signature.equals(signature2)) {
//            OperationResultWXLogin operationResult = new OperationResultWXLogin();
//            operationResult.setCode(3);
//            operationResult.setMessage("签名校验失败");
//            return operationResult;
//        }
        if(openid == null || "".equals(openid)){
            // openid表中存在该openid,但是手机号未绑定
            OperationResultWXLogin operationResult = new OperationResultWXLogin();
            operationResult.setCode(2);
            operationResult.setSessionid(httpSession.getId());
            operationResult.setMessage("code过期或无效");
            return operationResult;
        }
        // 根据openid查询redis缓存
        Openid openidTable = (Openid)redisUtil.get(RedisConst.OPENID+openid);
        if(openidTable == null){
            // 缓存中添加一条记录
            Openid openidObject = new Openid();
            openidObject.setOpenid(openid);
            openidObject.setPhone(0);
            redisUtil.set(RedisConst.OPENID+openid,openidObject);
            // 插入数据库
            openidService.insertOpenid(openidObject);
            // openid表中不存在该openid
            OperationResultWXLogin operationResult = new OperationResultWXLogin();
            operationResult.setCode(1);
            operationResult.setOpenid(openid);
            httpSession.setAttribute(openid,openid);
            operationResult.setSessionid(httpSession.getId());
            operationResult.setMessage("登录成功，手机号未绑定");
            return operationResult;
        }
        if(openidTable.getPhone() == 0){
            // openid表中存在该openid,但是手机号未绑定
            OperationResultWXLogin operationResult = new OperationResultWXLogin();
            operationResult.setCode(1);
            operationResult.setOpenid(openid);
            httpSession.setAttribute(openid,openid);
            operationResult.setSessionid(httpSession.getId());
            operationResult.setMessage("登录成功，手机号未绑定");
            return operationResult;
        }
        // openid表中存在该openid 同时手机号存在
        OperationResultWXLogin operationResult = new OperationResultWXLogin();
        operationResult.setCode(0);
        operationResult.setOpenid(openid);
        httpSession.setAttribute(openid,openid);
        operationResult.setSessionid(httpSession.getId());
        operationResult.setMessage("登录成功");
        return operationResult;
    }


    /**
     * 不是第一次登录用（使用缓存openid的场合）
     * @return
     */
    @PostMapping("/wx/login2")
    @ResponseBody
    public OperationResultWXLogin user_login2(@RequestBody LoginData loginData) {
        logger.info("loginData openid:"+loginData.getOpenid());
        // 根据openid查询redis缓存
        Openid openidTable = (Openid)redisUtil.get(RedisConst.OPENID+loginData.getOpenid());
        if(openidTable == null){
            // openid表中不存在该openid（可能该openid是非法的）
            OperationResultWXLogin operationResult = new OperationResultWXLogin();
            operationResult.setCode(2);
            operationResult.setSessionid(httpSession.getId());
            operationResult.setMessage("openid非法，请清空本地缓存再操作");
            logger.info(operationResult.toString());
            return operationResult;
        }
        if(openidTable.getPhone() == 0){
            // openid表中存在该openid,但是手机号未绑定
            OperationResultWXLogin operationResult = new OperationResultWXLogin();
            operationResult.setCode(1);
            httpSession.setAttribute(loginData.getOpenid(),loginData.getOpenid());
            operationResult.setSessionid(httpSession.getId());
            operationResult.setMessage("登录成功，手机号未绑定");
            logger.info(operationResult.toString());
            return operationResult;
        }
        // openid表中存在该openid 同时手机号存在
        OperationResultWXLogin operationResult = new OperationResultWXLogin();
        operationResult.setCode(0);
        httpSession.setAttribute(loginData.getOpenid(),loginData.getOpenid());
        operationResult.setSessionid(httpSession.getId());
        operationResult.setMessage("登录成功");
        logger.info(operationResult.toString());
        return operationResult;
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("/wx/logout")
    @ResponseBody
    public OperationResult logout(@RequestBody LogoutData logoutData) {
        httpSession.removeAttribute(logoutData.getOpenid());
        OperationResult operationResult = new OperationResult();
        operationResult.setCode(0);
        operationResult.setMessage("退出成功");
        return operationResult;
    }
}