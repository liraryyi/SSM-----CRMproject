package com.liraryyi.crm.workbench.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {
	
	private String id;
	private String owner;
	private String name;
	private String website;
	private String phone;
	private String createBy;
	private String createTime;
	private String editBy;
	private String editTime;
	private String contactSummary;
	private String nextContactTime;
	private String description;
	private String address;

	
}
