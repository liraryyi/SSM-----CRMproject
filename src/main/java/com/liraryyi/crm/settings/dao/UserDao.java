package com.liraryyi.crm.settings.dao;


import com.liraryyi.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    //登录时从tbluser找到与传进来的账号密码相对应的数据
    User loginUser(Map<String,String> map);

    //拿到tbluser表中的所有信息
    List selectUser();
}
