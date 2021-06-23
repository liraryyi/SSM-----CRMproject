package com.liraryyi.crm.workbench.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactsRemark {
	
	private String id;
	private String noteContent;
	private String createTime;
	private String createBy;
	private String editTime;
	private String editBy;
	private String editFlag;
	private String contactsId;
	
	
}
