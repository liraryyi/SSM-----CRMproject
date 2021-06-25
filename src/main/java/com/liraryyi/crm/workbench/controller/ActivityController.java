package com.liraryyi.crm.workbench.controller;

import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.settings.service.UserService;
import com.liraryyi.crm.utils.DateTimeUtil;
import com.liraryyi.crm.utils.PrintJson;
import com.liraryyi.crm.utils.UUIDUtil;
import com.liraryyi.crm.vo.PageListVo;
import com.liraryyi.crm.workbench.domain.Activity;
import com.liraryyi.crm.workbench.domain.Activity_remark;
import com.liraryyi.crm.workbench.service.ActivityService;
import com.liraryyi.crm.workbench.service.Activity_remarkService;
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
public class ActivityController {

    @Resource @Getter @Setter
    private UserService userService;

    @Resource @Getter @Setter
    private ActivityService activityService;

    @Resource @Getter @Setter
    private Activity_remarkService activity_remarkService;

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

        //传过来的数据为id=xxx&id=xxx，可以采用数组统一接收getParameterValues
        String[] ids = request.getParameterValues("id");

        System.out.println(ids);
        for (int i = 0; i < ids.length; i++) {
            System.out.println(ids[i]);
        }

        boolean flag = activityService.deleteActivityList(ids);

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

        String id = request.getParameter("edit-id");
        String owner = request.getParameter("edit-marketActivityOwner");
        String name = request.getParameter("edit-marketActivityName");
        String startDate = request.getParameter("edit-startTime");
        String endDate = request.getParameter("edit-endTime");
        String cost = request.getParameter("edit-cost");
        String describe = request.getParameter("edit-describe");
        //创建时间为当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //创建人为当前登录的用户
        String editBy =((User)request.getSession().getAttribute("user")).getName();

        Map<String, String> map = new HashMap<>();
        map.put("id",id);
        map.put("owner",owner);
        map.put("name",name);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("cost",cost);
        map.put("describe",describe);
        map.put("editTime",editTime);
        map.put("editBy",editBy);

        boolean success = activityService.updateActivity(map);


        PrintJson.printJsonFlag(response,success);
    }

    //点击市场活动，跳转到详细信息栏
    @RequestMapping(value = "/activity/detail.do")
    public ModelAndView getDetailActivity(String id){

        ModelAndView mv = new ModelAndView();

        Activity activity = activityService.selectDetailActivityById(id);

        mv.addObject("activity",activity);
        mv.setViewName("detail.jsp");

        return mv;
    }

    //得到备注活动信息列表
    @ResponseBody
    @RequestMapping(value = "/activity/getRemarkActivityList.do")
    public List<Activity_remark> getActivity_remarkList(HttpServletRequest request){

        String id = request.getParameter("activityId");

        List<Activity_remark> list = activity_remarkService.getActivity_remarkListById(id);

        return list;
    }

    //根据id删除备注活动信息列表
    @RequestMapping(value = "/activity/deleteRemark.do")
    public void  deleteRemark(HttpServletRequest request,HttpServletResponse response){

        String id = request.getParameter("id");

        boolean success = activity_remarkService.deleteRemarkById(id);

        PrintJson.printJsonFlag(response,success);
    }

    //保存备注活动信息
    @RequestMapping(value = "/activity/saveRemarkActivity.do")
    public void saveRemarkActivity(HttpServletRequest request,HttpServletResponse response){

        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContext");
        String activityId = request.getParameter("activityId");
        String editFlag = "0";
        //创建时间为当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人为当前登录的用户
        String createBy =((User)request.getSession().getAttribute("user")).getName();

        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("noteContent",noteContent);
        map.put("activityId",activityId);
        map.put("editFlag",editFlag);
        map.put("createTime",createTime);
        map.put("createBy",createBy);

        boolean success = activity_remarkService.saveRemarkActivityMap(map);

        PrintJson.printJsonFlag(response,success);
    }

    //更新备注活动信息
    @RequestMapping(value = "/activity/updateRemark.do")
    public void updateRemarkActivity(HttpServletRequest request,HttpServletResponse response){

        System.out.println("updateRemark controller start");
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        //创建时间为当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        //创建人为当前登录的用户
        String editBy =((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        Map<String , String > map = new HashMap<>();
        map.put("id",id);
        map.put("noteContent",noteContent);
        map.put("editTime",editTime);
        map.put("editBy",editBy);
        map.put("editFlag",editFlag);

        boolean success = activity_remarkService.updateRemark(map);

        System.out.println("updateRemark controller end");
        PrintJson.printJsonFlag(response,success);
    }

    //根据线索Id查询活动信息列表
    @ResponseBody
    @RequestMapping(value = "/clue/getActivityListByClueId.do")
    public List<Activity> getActivityListByName(HttpServletRequest request){

        //查询到的市场活动消息不应该是已经关联了的
        String clueId = request.getParameter("clueId");
        String name = request.getParameter("name");


        List<Activity> list = activityService.getActivityListByName(clueId,name);

        return list;
    }
}
