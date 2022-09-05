package com.wxy.reggie.filter;

import com.alibaba.fastjson2.JSON;
import com.wxy.reggie.common.BaseContext;
import com.wxy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wxy
 */

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取用户id并保存到ThreadLocal变量
        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);

        Long userId = (Long) request.getSession().getAttribute("user");
        BaseContext.setCurrentId(userId);

        String requestURI = request.getRequestURI();
        System.out.println(requestURI);
        /**不需要拦截的路径*/
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/*.js",
                "/user/sendMsg",
                "/user/login"
        };

        Boolean check = check(urls, requestURI);

        //如果匹配到不需要拦截的路径则放行
        if (check) {
            log.info("本次{}请求不需要处理", requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //检查用户是否登录，如登录则放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户{}已登录", request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }

        if (request.getSession().getAttribute("user") != null) {
            log.info("用户{}已登录", request.getSession().getAttribute("user"));
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }



    /**
     * 检查路径是否需要拦截
     * @param requestURI
     * @return
     */
    public Boolean check(String[] urls, String requestURI) {
        for (String url:urls
             ) {
            Boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
