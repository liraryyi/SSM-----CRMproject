package com.liraryyi.crm.workbench.controller;

import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.settings.service.UserService;
import com.liraryyi.crm.utils.DateTimeUtil;
import com.liraryyi.crm.utils.PrintJson;
import com.liraryyi.crm.utils.UUIDUtil;
import com.liraryyi.crm.workbench.domain.Tran;
import com.liraryyi.crm.workbench.domain.TranHistory;
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

    @RequestMapping(value = "/transaction/saveTransaction.do")
    public ModelAndView saveTran(HttpServletRequest request){

        ModelAndView mv = new ModelAndView();

        String owner  = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName  = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContacTime");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran tran = new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);
        tran.setCreateTime(createTime);
        tran.setCreateBy(createBy);

        boolean flag = tranService.saveTran(tran,customerName);

        if (flag == true){
            mv.setViewName("forward:/workbench/transaction/index.jsp");
        }

        return mv;
    }

    @RequestMapping(value = "/transaction/detail.do")
    public ModelAndView getDetail(HttpServletRequest request){

        ModelAndView mv = new ModelAndView();

        String id = request.getParameter("id");

        Tran tran = tranService.getTranById(id);

        /*
        得到可能性：
         */
        String stage = tran.getStage();
        Map<String,String> map2 = (Map<String, String>) request.getServletContext().getAttribute("map2");
        String possibility = map2.get(stage);

        mv.addObject("possibility",possibility);
        mv.addObject("tran",tran);
        mv.setViewName("forward:/workbench/transaction/detail.jsp");

        return mv;
    }

    @RequestMapping(value = "/transaction/getTranHistoryList.do")
    public void getTranHistory(HttpServletRequest request, HttpServletResponse response){

        String tranId = request.getParameter("tranId");

        List<TranHistory> list = tranService.getTranHistoryByTranId(tranId);


        //拿到可能性
        Map<String,String> map2 = (Map<String, String>) request.getServletContext().getAttribute("map2");

        for (TranHistory tranHistory:list){

            String stage = tranHistory.getStage();
            String possibility = map2.get(stage);
            tranHistory.setPossibility(possibility);
        }

        PrintJson.printJsonObj(response,list);
    }

    @RequestMapping(value = "/transaction/changeStage.do")
    public void changeStage(HttpServletRequest request,HttpServletResponse response){

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        Tran tran =new Tran();
        tran.setId(id);
        tran.setStage(stage);
        tran.setMoney(money);
        tran.setExpectedDate(expectedDate);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);
        tran.setCreateBy(((User)request.getSession().getAttribute("user")).getName());

        boolean flag = tranService.changeStage(tran);

        Map<String,String> map2 = (Map<String, String>) request.getServletContext().getAttribute("map2");
        tran.setPossibility(map2.get(stage));

        Map<String, Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("tran",tran);

        PrintJson.printJsonObj(response,map);
    }
}






























