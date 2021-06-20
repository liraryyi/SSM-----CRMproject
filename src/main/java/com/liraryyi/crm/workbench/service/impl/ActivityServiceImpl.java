package com.liraryyi.crm.workbench.service.impl;

import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.vo.PageListVo;
import com.liraryyi.crm.workbench.dao.ActivityDao;
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

        System.out.println("pagelist service go");
        int total = activityDao.selectActivityCount(map);

        System.out.println("get total");
        List<Activity> list =  activityDao.selectActivityList(map);

        System.out.println("get list");
        PageListVo<Activity> vo  =new PageListVo();
        vo.setTotal(total);
        vo.setList(list);
        System.out.println("pagelist service end");
        return vo;
    }
}
