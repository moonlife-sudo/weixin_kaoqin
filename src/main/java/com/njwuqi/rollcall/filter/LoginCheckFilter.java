package com.njwuqi.rollcall.filter;
import com.alibaba.fastjson.JSON;
import com.njwuqi.rollcall.entity.OperationResult;
//import jdk.nashorn.internal.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @Auther guandz
 */
@Component
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    @Autowired
    private HttpSession httpSession;
    // 日志
    private static final Logger logger = LoggerFactory.getLogger(LoginCheckFilter.class);
    //路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        //不拦截的路径
        String[] urls = new String[]{
                "/wx/login",
                "/wx/login2",
                "/wx/logout"
        };
        //1.获取本次请求的url
        String requestURI = request.getRequestURI();
        logger.info("登录过滤器开始{}",requestURI);
        //2.判断该url是否需要拦截,若不拦截则直接放行
        for (String i:urls){
            boolean match = PATH_MATCHER.match(i, requestURI);
            if (match==true){
                logger.info("该请求{},无需拦截",requestURI);
                filterChain.doFilter(request,response);
                return;
            }
        }
        //4.判断用户是否登录,登录直接放行
        String openid = request.getHeader("openid");
        if(httpSession.getAttribute(openid)!=null){
            logger.info("用户已登录,openid为{}",request.getSession().getAttribute(openid));
            filterChain.doFilter(request,response);
            return;
        }
        logger.info("用户未登录");
        OperationResult operationResult = new OperationResult();
        operationResult.setCode(99);
        operationResult.setMessage("用户未登录");
        //5.未登录则跳转登录页面
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(operationResult));
        logger.info("登录过滤器结束{}",requestURI);
        return;
    }
}