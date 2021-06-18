package com.liraryyi.crm.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 因为拦截器只拦截@Controller注解的类和方法，即和处理器相关，不拦截jsp文件
 *
 * 因此使用过滤器将未登录的用户对于workbench中内容的请求进行过滤
 *
 */
public class LoginFilter implements Filter {

    //初始化方法
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //过滤处理
        //servletRequest 是个接口，HttpServletRequest 是实现，但是有些方法是HttpServletRequest独有的，如：getSession
        //HttpServletRequest接口是继承servletRequest接口，增加了和http相关的方法

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // String requestURI=request.getRequestURI();
        // System.out.println("链接:"+requestURI+"进入过滤器");

        String path = request.getServletPath();

        //正常登录的请求，放行
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)) {

            filterChain.doFilter(request, response);

        }else{
            HttpSession session = request.getSession();

            if (session.getAttribute("user") == null) {

                //非法请求，直接跳转到登陆界面(重定向)
                String serverPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/login.jsp";
                response.sendRedirect(serverPath);

            } else {

                //正常登录，放行
                filterChain.doFilter(request, response);
            }
        }
    }
    @Override
    public void destroy() {
            //释放资源
    }
}
