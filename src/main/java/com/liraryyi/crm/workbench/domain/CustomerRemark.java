package com.liraryyi.crm.workbench.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRemark {

	private String id;
	private String noteContent;
	private String createTime;
	private String createBy;
	private String editTime;
	private String editBy;
	private String editFlag;
	private String customerId;

	
	
}
