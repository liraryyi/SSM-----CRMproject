package com.liraryyi.crm.workbench.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranHistory {
	
	private String id;
	private String stage;
	private String money;
	private String expectedDate;
	private String createTime;
	private String createBy;
	private String tranId;

	
}
