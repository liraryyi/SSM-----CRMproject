package com.liraryyi.crm.workbench.dao;

import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int insertActivity(Map<String,String> map);

    List<Activity> selectActivityList(Map<String,Object> map);

    int selectActivityCount(Map<String, Object> map);
}
