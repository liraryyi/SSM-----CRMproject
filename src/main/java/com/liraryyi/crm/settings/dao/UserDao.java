package com.liraryyi.crm.settings.dao;


import com.liraryyi.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    User loginUser(Map<String,String> map);

}
