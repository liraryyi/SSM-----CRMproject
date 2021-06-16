package com.liraryyi.crm.settings.service;

import com.liraryyi.crm.exception.LoginException;
import com.liraryyi.crm.settings.domain.User;

public interface UserService {


    User login(String loginAck, String loginPwd, String ip) throws LoginException;
}
