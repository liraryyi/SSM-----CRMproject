package com.liraryyi.crm.workbench.controller;

import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.settings.service.UserService;
import com.liraryyi.crm.utils.PrintJson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping(value = "/workbench")
@Controller
public class ClueController {

    @Resource @Getter @Setter
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/clue/getUserList.do")
    public List getUserList(HttpServletRequest request ,HttpServletResponse response){

        List<User> list =  userService.getUser();

        return list;
    }
}
