package com.liraryyi.crm.workbench.dao;

public interface Activity_remarkDao {

    //查询关联的总条数
    int getCountByIds(String[] ids);

    //受到删除影响的总条数
    int deleteByIds(String[] ids);
}
