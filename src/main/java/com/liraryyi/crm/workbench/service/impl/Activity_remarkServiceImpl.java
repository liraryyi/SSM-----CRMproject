package com.liraryyi.crm.workbench.service.impl;

import com.liraryyi.crm.workbench.domain.Activity_remark;
import com.liraryyi.crm.workbench.service.ActivityService;
import com.liraryyi.crm.workbench.dao.Activity_remarkDao;
import com.liraryyi.crm.workbench.service.Activity_remarkService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class Activity_remarkServiceImpl implements Activity_remarkService {

    @Resource @Setter @Getter
    private Activity_remarkDao activity_remarkDao;

    @Override
    public List<Activity_remark> getActivity_remarkListById(String id) {

        List<Activity_remark> list = activity_remarkDao.selectActivityListById(id);

        return list;
    }

    @Override
    public boolean deleteRemarkById(String id) {

        boolean flag = true;

        int count = activity_remarkDao.deleteByRemarkId(id);

        if (count != 1){ flag = false;}

        return flag;
    }

    public boolean saveRemarkActivityMap(Map<String, String> map){

        boolean flag = true;

        int count = activity_remarkDao.insertRemarkActivity(map);

        if (count != 1){flag = false;}

        return flag;
    }

    @Override
    public boolean updateRemark(Map<String, String> map) {

        boolean flag = true;

        int count = activity_remarkDao.updateRemarkById(map);

        if (count != 1){ flag=false; }

        return flag;
    }
}
