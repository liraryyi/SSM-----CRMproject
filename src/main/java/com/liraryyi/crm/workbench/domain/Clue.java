package com.liraryyi.crm.workbench.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Clue {
	
	private String id;	//主键
	private String fullname;	//全名（人的名字）
	private String appellation;	//称呼
	private String owner;	//所有者
	private String company;	//公司名称
	private String job;	//职业
	private String email;	//邮箱
	private String phone;	//公司电话
	private String website;	//公司网站
	private String mphone;	//手机
	private String state;	//状态
	private String source;	//来源
	private String createBy;	//创建人
	private String createTime;	//创建时间
	private String editBy;	//修改人
	private String editTime;	//修改时间
	private String description;	//描述
	private String contactSummary;	//联系纪要
	private String nextContactTime;	//下次联系时间
	private String address;	//地址

	

	
}
