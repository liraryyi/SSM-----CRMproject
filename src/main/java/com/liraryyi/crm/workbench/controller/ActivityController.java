package com.liraryyi.crm.workbench.controller;

import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.settings.service.UserService;
import com.liraryyi.crm.utils.DateTimeUtil;
import com.liraryyi.crm.utils.PrintJson;
import com.liraryyi.crm.utils.UUIDUtil;
import com.liraryyi.crm.vo.PageListVo;
import com.liraryyi.crm.workbench.domain.Activity;
import com.liraryyi.crm.workbench.service.ActivityService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/workbench")
@Controller
public class ActivityController {

    @Resource @Getter @Setter
    private UserService userService;

    @Resource @Getter @Setter
    private ActivityService activityService;

    //这里的方法是要拿到tbluser表中的User数据，所以相对应的业务层应该调用UserService
    //返回对象，需要使用@ResponseBody 注解， 将转换后的JSON数据放入到响应体中
    @ResponseBody
    @RequestMapping(value = "/activity/getUserList.do")
    public List<User> GetUserList(){

        List<User> list = userService.getUser();

        return list;
    }

    //存储市场活动的信息
    @RequestMapping(value = "/activity/saveActivity.do")
    public void saveActivity(HttpServletRequest request, HttpServletResponse response){
        //这里使用的一个Map来存储与传递这些属性，其实直接用Activity对象更好
        String id = UUIDUtil.getUUID();
        String owner =request.getParameter("create-marketActivityOwner");
        String name =request.getParameter("create-marketActivityName");
        String startDate =request.getParameter("create-startTime");
        String endDate =request.getParameter("create-endTime");
        String cost =request.getParameter("create-cost");
        String description =request.getParameter("create-describe");
        //创建时间为当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人为当前登录的用户
        String createBy =((User)request.getSession().getAttribute("user")).getName();
        Map<String, String> map = new HashMap<String, String>();
        map.put("id",id);
        map.put("owner", owner);
        map.put("name", name);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("cost", cost);
        map.put("description", description);
        map.put("createTime",createTime);
        map.put("createBy",createBy);
        boolean success = activityService.saveActivity(map);
        Map<String, Boolean> map1 = new HashMap<>();
        map1.put("success",success);
        PrintJson.printJsonObj(response,map1);
    }

    //分页查询市场活动信息
    @RequestMapping(value = "/activity/pageList.do")
    public void getActivity(HttpServletRequest request,HttpServletResponse response){

        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        System.out.println(name + owner + startTime +endTime);

        int pageNoInt = Integer.valueOf(pageNo);
        int pageSizeInt = Integer.valueOf(pageSize);
        //计算出略过的记录数
        int skipCount = (pageNoInt-1)*pageSizeInt;

        Map<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startTime);
        map.put("endDate",endTime);
        map.put("skipCount",skipCount);
        map.put("pageSize", pageSizeInt);

        /**
         * 分析要传递给前端的数据
         * total ： 查询出来的总条数
         * activityList ： 市场活动信息列表
         *
         * 多个参数的传递
         * map：适用于一次
         * vo：适用于多次传递
         */
        PageListVo<Activity> pageListVo = activityService.selectActivityList(map);
        PrintJson.printJsonObj(response,pageListVo);
    }

    //删除复选框选中的数据，并且联系上另一个表
    @RequestMapping(value = "/activity/delete.do")
    public void deleteActivity(HttpServletRequest request,HttpServletResponse response){

        System.out.println("controller start");
        //传过来的数据为id=xxx&id=xxx，可以采用数组统一接收getParameterValues
        String[] ids = request.getParameterValues("id");

        System.out.println(ids);
        for (int i = 0; i < ids.length; i++) {
            System.out.println(ids[i]);
        }

        boolean flag = activityService.deleteActivityList(ids);

        System.out.println("controller end");
        PrintJson.printJsonFlag(response,flag);
    }

    //查询点击的复选框中的活动信息
    @ResponseBody
    @RequestMapping(value = "/activity/getActivityList.do")
    public Activity getAllActivity(HttpServletRequest request){

        String id = request.getParameter("id");

        Activity activity = activityService.selectAllActivity(id);

        return activity;
    }

    //保存更新修改后的市场活动信息
    @RequestMapping(value = "/activity/updateActivity.do")
    public void updateActivityList(HttpServletRequest request,HttpServletResponse response){

        System.out.println("update controller start");
        String id = request.getParameter("edit-id");
        String owner = request.getParameter("edit-marketActivityOwner");
        String name = request.getParameter("edit-marketActivityName");
        String startDate = request.getParameter("edit-startTime");
        String endDate = request.getParameter("edit-endTime");
        String cost = request.getParameter("edit-cost");
        String describe = request.getParameter("edit-describe");

        Map<String, String> map = new HashMap<>();
        map.put("id",id);
        map.put("owner",owner);
        map.put("name",name);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("cost",cost);
        map.put("describe",describe);

        boolean success = activityService.updateActivity(map);

        System.out.println("update controller end");

        PrintJson.printJsonFlag(response,success);
    }
}
