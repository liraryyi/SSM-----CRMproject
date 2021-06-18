package com.liraryyi.crm.settings.service.impl;

import com.liraryyi.crm.exception.LoginException;
import com.liraryyi.crm.settings.dao.UserDao;
import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.settings.service.UserService;
import com.liraryyi.crm.utils.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    @Setter @Getter
    private UserDao userDao;

    //登录
    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        /**
         * 调用dao层时，如何将参数传进去(多个参数)
         * 1.使用@Param
         * 2.使用对象
         * 3.使用Map
         */

        Map<String, String> map = new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userDao.loginUser(map);

        //验证账号密码
        if (user == null){
            System.out.println("账号密码错误");
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
        if ( !"".equals(ArrayIp) && ArrayIp != null) {
            if (!ArrayIp.contains(ip)) {
                throw new LoginException("ip异常");
            }
        }

        return user;
    }

    //拿到tbluser表中的所有信息
    @Override
    public List<User> getUser() {
        List<User> list = userDao.selectUser();
        return list;
    }
}
