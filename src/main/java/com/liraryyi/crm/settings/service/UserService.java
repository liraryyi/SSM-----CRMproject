package com.liraryyi.crm.settings.service;

import com.liraryyi.crm.exception.LoginException;
import com.liraryyi.crm.settings.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService{

    User login(String loginAck, String loginPwd, String ip) throws LoginException;

    List<User> getUser();


}
