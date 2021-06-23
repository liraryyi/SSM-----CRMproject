package com.liraryyi.crm.workbench.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClueRemark {
	
	private String id;
	private String noteContent;
	private String createBy;
	private String createTime;
	private String editBy;
	private String editTime;
	private String editFlag;
	private String clueId;

	
}
