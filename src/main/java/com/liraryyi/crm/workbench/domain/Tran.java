package com.liraryyi.crm.workbench.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tran {
	
	private String id;
	private String owner;  //--------------------外键  关联tbl_user
	private String money;	//交易金额
	private String name;	//交易名称
	private String expectedDate;	//预计成交日期
	private String customerId;  //-------------------外键  关联tbl_customer
	private String stage;	//交易阶段
	private String type;	//交易类型
	private String source;	//交易来源
	private String activityId;  //-------------------外键  关联tbl_activity
	private String contactsId;  //-------------------外键  关联tbl_contacts
	private String createBy;
	private String createTime;
	private String editBy;
	private String editTime;
	private String description;
	private String contactSummary;	//联系纪要
	private String nextContactTime;	//下次联系时间

}
