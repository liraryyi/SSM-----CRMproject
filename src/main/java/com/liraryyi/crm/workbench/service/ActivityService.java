package com.liraryyi.crm.workbench.service;

import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.vo.PageListVo;
import com.liraryyi.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean saveActivity(Map<String, String> map);

    PageListVo selectActivityList(Map<String, Object> map);

    boolean deleteActivityList(String[] ids);

    Activity selectAllActivity(String id);

    boolean updateActivity(Map<String,String> map);

    Activity selectDetailActivityById(String id);
}
