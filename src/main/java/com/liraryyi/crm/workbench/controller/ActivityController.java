package com.liraryyi.crm.workbench.controller;

import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.settings.service.UserService;
import com.liraryyi.crm.workbench.service.ActivityService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping(value = "/workbench")
@Controller
public class ActivityController {

    @Resource @Getter @Setter
    private UserService userService;

    //这里的方法是要拿到tbluser表中的User数据，所以相对应的业务层应该调用UserService
    //返回对象，需要使用@ResponseBody 注解， 将转换后的JSON数据放入到响应体中
    @ResponseBody
    @RequestMapping(value = "/activity/getUserList.do")
    public List<User> GetUserList(){

        List<User> list = userService.getUser();

        return list;
    }
}
