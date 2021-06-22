package com.liraryyi.crm.workbench.dao;

import com.liraryyi.crm.workbench.domain.Activity_remark;

import java.util.List;
import java.util.Map;

public interface Activity_remarkDao {

    //查询关联的总条数
    int getCountByIds(String[] ids);

    //受到删除影响的总条数
    int deleteByIds(String[] ids);

    //根据id查找到备注活动信息
    List<Activity_remark> selectActivityListById(String id);

    //根据id删除备注活动消息
    int deleteByRemarkId(String id);

    //插入新的备注活动信息
    int insertRemarkActivity(Map<String,String> map);

    //更新新的备注活动信息
    int updateRemarkById(Map<String, String> map);
}
