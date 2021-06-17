package com.liraryyi.crm.settings.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User {
    /**
     * 关于登录
     *   验证账号和密码
     *   User user = sql语句（select * form tbl_user where loginAct = ? and loginPwd = ?）
     *   user对象为null，说明账号密码错误
     *   如果user对象不为null，说明账号密码正确
     *   继续验证：
     *   1.expireTime 失效时间
     *   2.lockState 锁定状态
     *   3.allowIps 验证浏览器端的ip地址是否有效
     */
    private String id;     // 编号 主键
    private String loginAct;     // 登陆账号
    private String name;     // 用户的真实姓名
    private String loginPwd;     // 登陆密码
    private String email;     // 邮箱
    private String expireTime;     // 失效时间
    private String lockState;     // 锁定状态 0：锁定 1：启用
    private String deptno;     // 部门编号
    private String allowIps;     // 允许访问的ip地址
    private String createTime;     // 创建时间
    private String createBy;     // 创建人
    private String editTime;     // 修改时间
    private String editBy;     // 修改人

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", loginAct='" + loginAct + '\'' +
                ", name='" + name + '\'' +
                ", loginPwd='" + loginPwd + '\'' +
                ", email='" + email + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ", lockState='" + lockState + '\'' +
                ", deptno='" + deptno + '\'' +
                ", allowIps='" + allowIps + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createBy='" + createBy + '\'' +
                ", editTime='" + editTime + '\'' +
                ", editBy='" + editBy + '\'' +
                '}';
    }
}
