package com.liraryyi.crm.workbench.service.impl;

import com.liraryyi.crm.vo.PageListVo;
import com.liraryyi.crm.workbench.dao.ActivityDao;
import com.liraryyi.crm.workbench.dao.Activity_remarkDao;
import com.liraryyi.crm.workbench.dao.ClueActivityRelationDao;
import com.liraryyi.crm.workbench.domain.Activity;
import com.liraryyi.crm.workbench.service.ActivityService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    @Setter @Getter
    private ActivityDao activityDao;

    @Resource @Setter @Getter
    private ClueActivityRelationDao clueActivityRelationDao;

    @Resource
    @Setter @Getter
    private Activity_remarkDao activity_remarkDao;

    @Override
    public boolean saveActivity(Map<String, String> map) {

        int a = activityDao.insertActivity(map);
        //如果a大于0，表示创建成功
        if (a == 1){
            return true;
        }
        return false;
    }

    @Override
    public PageListVo selectActivityList(Map<String, Object> map) {

        int total = activityDao.selectActivityCount(map);

        List<Activity> list =  activityDao.selectActivityList(map);

        PageListVo<Activity> vo  =new PageListVo();
        vo.setTotal(total);
        vo.setList(list);
        return vo;
    }

    @Override
    public boolean deleteActivityList(String[] ids) {

        boolean flag = true;
        //在删除Activity这张表之前，应该先删除掉Activity_remark这张表
        //先做查询，根据Activity中的id找到remark中的id
        //查询remark表总条数
        System.out.println("service start");
        int count1 = activity_remarkDao.getCountByIds(ids);
        System.out.println("get count1");

        //实际删除并记录remark表的数目
        int count2 = activity_remarkDao.deleteByIds(ids);
        System.out.println("get count2");

        if (count1 != count2){
            flag = false;
        }

        //删除Activity表中的数据
        int count3 = activityDao.deleteActivityByIds(ids);
        System.out.println("get count3");

        if (count3 != ids.length){
            flag = false;
        }
        System.out.println("service end");
        return flag;
    }

    @Override
    public Activity selectAllActivity(String id) {

        Activity activity = activityDao.selectAllActivity(id);

        return activity;
    }

    public boolean updateActivity(Map<String, String> map){

        boolean flag = true;

        int count = activityDao.updateActivityById(map);

        if (count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Activity selectDetailActivityById(String id) {

        Activity activity = activityDao.selectDetailActivityById(id);

        return activity;
    }

    @Override
    public List<Activity> getActivityListByClueId(String id) {


        //根据含有市场活动id的数组ids，找到到相应的市场活动信息并保存到list中
        List<Activity> list = activityDao.selectActivityById(id);

        return list;
    }
}
