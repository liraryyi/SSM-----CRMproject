package com.liraryyi.crm.workbench.dao;

import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    int insertActivity(Map<String,String> map);

    List<Activity> selectActivityList(Map<String,Object> map);

    int selectActivityCount(Map<String, Object> map);

    int deleteActivityByIds(String[] ids);

    Activity selectAllActivity(String id);

    int updateActivityById(Map<String,String> map);

    Activity selectDetailActivityById(String id);

    List<Activity> selectActivityById(String id);

    List<Activity> getActivityListByName(@Param("clueId") String clueId,
                                         @Param("name") String name);

    List<Activity> selectActivityListByName (String name);
}
