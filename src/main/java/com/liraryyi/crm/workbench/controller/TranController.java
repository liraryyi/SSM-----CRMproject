package com.liraryyi.crm.workbench.controller;

import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.settings.service.UserService;
import com.liraryyi.crm.utils.PrintJson;
import com.liraryyi.crm.workbench.domain.Tran;
import com.liraryyi.crm.workbench.service.TranService;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.liraryyi.crm.vo.PageListVo;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/workbench")
@Controller
public class TranController {

    @Resource @Setter @Getter
    private TranService tranService;

    @Resource @Setter @Getter
    private UserService userService;

    @RequestMapping(value = "/transaction/getTransactionList.do")
    public void getTranList(HttpServletRequest request, HttpServletResponse response){

        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String contactsName = request.getParameter("contactsName");

        int pageNoInt = Integer.valueOf(pageNo);
        int pageSizeInt = Integer.valueOf(pageSize);
        //这里skipCount是指略过的信息数,比如第一页就不略过，第二页略过1*pageSizeInt条
        int skipCount = (pageNoInt-1)*pageSizeInt;

        Map<String ,Object> map = new HashMap<>();
        map.put("owner",owner);
        map.put("name",name);
        map.put("customerName",customerName);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("contactsName",contactsName);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSizeInt);

        PageListVo<Tran> pageListVo = tranService.getTranList(map);

        PrintJson.printJsonObj(response,pageListVo);
    }

    @RequestMapping(value = "/transaction/getUserList.do")
    public ModelAndView getUserList(){

        ModelAndView mv = new ModelAndView();

        List<User> list = userService.getUser();

        mv.addObject("list",list);

        mv.setViewName("forward:/workbench/transaction/save.jsp");

        return mv;
    }
}
