package com.liraryyi.crm.settings.service.impl;

import com.liraryyi.crm.exception.LoginException;
import com.liraryyi.crm.settings.dao.UserDao;
import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.settings.service.UserService;
import com.liraryyi.crm.utils.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {

    @Resource
    @Setter @Getter
    private UserDao userDao;


    @Override
    public User login(String loginAck, String loginPwd, String ip) throws LoginException {

        Map<String, String> map = new HashMap<String, String>();
        map.put("loginAck",loginAck);
        map.put("loginPwd", loginPwd);

        User user = userDao.loginDao((HashMap) map);

        //验证账号密码
        if (user == null){
            throw new LoginException("账号密码错误");
        }

        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)< 0){
            throw new LoginException("账号已失效");
        }

        //验证锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }

        //验证ip地址
        String ArrayIp = user.getAllowIps();
        if (ArrayIp != null && ArrayIp != "") {
            if (!ArrayIp.contains(ip)) {
                throw new LoginException("ip异常");
            }
        }
        return user;
    }
}
