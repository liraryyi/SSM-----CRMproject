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

@Controller
@RequestMapping(value = "/settings")
public class UserController {

    @Resource
    @Setter @Getter
    private UserService userService;

    @RequestMapping(value = "/user/login.do",method = RequestMethod.POST)
    public void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {


        /**
         * 1.接受浏览器页面传过来的数据：账号，密码，ip地址
         * 2.调用UserService中的login方法
         */
        System.out.println("开始login");
        //接收传过来的数据(账号，密码)
        String loginAck = request.getParameter("loginAck");
        String loginPwd = request.getParameter("loginPwd");
        //密码从明文形式转换为MD5密文形式
        loginPwd = MD5Util.getMD5("loginPwd");
        //接收浏览器端的ip地址
        String ip = request.getRemoteAddr();

        try {
            User user = userService.login(loginAck,loginPwd, ip);

            request.getSession().setAttribute("user",user);
        } catch (LoginException e) {
            e.printStackTrace();
        }

        //使用jackson工具库,把true转为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(true);

        //使用HttpServletResponse输出数据到浏览器
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
        out.close();
    }
}
