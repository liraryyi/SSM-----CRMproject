package com.liraryyi.crm.settings.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liraryyi.crm.exception.LoginException;
import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.settings.service.UserService;
import com.liraryyi.crm.utils.MD5Util;
import com.liraryyi.crm.utils.PrintJson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/settings")
public class UserController {

    @Resource @Setter @Getter
    private UserService userService;

    //ajax请求 方法的返回值为void
    @RequestMapping(value = "/user/login.do")
    public void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

        /**
         * 1.接受浏览器页面传过来的数据：账号，密码，ip地址
         * 2.调用UserService中的login方法
         * 3.返回结果
         */
        //接收传过来的数据(账号，密码)
        String loginAck = request.getParameter("loginAck");
        String loginPwd = request.getParameter("loginPwd");

        //密码从明文形式转换为MD5密文形式
        loginPwd = MD5Util.getMD5(loginPwd);

        //接收浏览器端的ip地址
        String ip = request.getRemoteAddr();

        try {
            //调用业务层的login方法，从数据库中查找相应的用户信息
            User user = userService.login(loginAck,loginPwd, ip);

            //将用户加入到会话作用域对象(HttpSession),便于用户对其它页面进行访问
            request.getSession().setAttribute("user",user);

            //以json字符串的形式返回结果
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success",true);
            PrintJson.printJsonObj(response,map);

        } catch (LoginException e) {

            //如果没有找到相对应的用户数据，抛出异常并处理
            String msg = e.getMessage();

            /**
             * controller为ajax提供多项信息
             * 1.success:false
             * 2.异常消息：msg
             *
             * 2个方法
             * 如果展现的信息只有在这个类才使用：
             * 1.将多项信息打包成map，再将map解析成json串
             * 如果展现的信息还会大量使用：
             * 2.创建一个VO
             */
            Map<String,Object> map = new HashMap<String,Object>();

            map.put("success",false);
            map.put("msg",msg);

            PrintJson.printJsonObj(response,map);
        }

    }
}
