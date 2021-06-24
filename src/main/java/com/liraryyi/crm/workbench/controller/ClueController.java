package com.liraryyi.crm.workbench.controller;

import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.settings.service.UserService;
import com.liraryyi.crm.utils.DateTimeUtil;
import com.liraryyi.crm.utils.PrintJson;
import com.liraryyi.crm.utils.UUIDUtil;
import com.liraryyi.crm.workbench.domain.Activity;
import com.liraryyi.crm.workbench.domain.Clue;
import com.liraryyi.crm.workbench.service.ActivityService;
import com.liraryyi.crm.workbench.service.ClueService;
import com.liraryyi.crm.vo.PageListVo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/workbench")
@Controller
public class ClueController {

    @Resource @Getter @Setter
    private UserService userService;

    @Resource @Getter @Setter
    private ActivityService activityService;

    @Resource @Getter @Setter
    private ClueService clueService;

    @ResponseBody
    @RequestMapping(value = "/clue/getUserList.do")
    public List getUserList(HttpServletRequest request ,HttpServletResponse response){

        List<User> list =  userService.getUser();

        return list;
    }

    @RequestMapping(value = "/clue/saveClue.do")
    public void saveClueList(HttpServletRequest request,HttpServletResponse response){

        String id = UUIDUtil.getUUID();
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String fullname = request.getParameter("fullname");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue = new Clue();
        clue.setFullname(fullname);
        clue.setId(id);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        boolean success = clueService.saveClue(clue);

        PrintJson.printJsonFlag(response,success);

    }

    @RequestMapping(value = "/clue/getClueList.do")
    public void getClueList(HttpServletRequest request, HttpServletResponse response){

        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");
        String fullname = request.getParameter("fullname");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String source = request.getParameter("source");
        String owner  = request.getParameter("owner");
        String mphone = request.getParameter("mphone");
        String clueState = request.getParameter("clueState");

        int pageNoInt = Integer.valueOf(pageNo);
        int pageSizeInt = Integer.valueOf(pageSize);

        int skipCount = (pageNoInt-1)*pageSizeInt;

        Map<String, Object> map = new HashMap<>();
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("clueState",clueState);

        PageListVo<Clue> pageListVo =  clueService.getClueList(map);

        PrintJson.printJsonObj(response,pageListVo);
    }

    @RequestMapping(value = "/clue/detail.do")
    public ModelAndView getDetailClueList(String id){

        ModelAndView mv = new ModelAndView();
        Clue clue =  clueService.getClueListById(id);

        mv.addObject("clue",clue);
        mv.setViewName("detail.jsp");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/clue/getActivityList.do")
    public List<Activity> getActivityListByClueId(HttpServletRequest request){

        String id = request.getParameter("id");

        List<Activity> list = activityService.getActivityListByClueId(id);

        return list;
    }

    @RequestMapping(value = "/clue/unbund.do")
    public void unbundRelation(HttpServletRequest request,HttpServletResponse response){

        String id = request.getParameter("id");

        boolean success = clueService.deleteRelation(id);

        PrintJson.printJsonFlag(response,success);
    }
}
