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

	//扩展可能性
	private String possibility;

	@Override
	public String toString() {
		return "TranHistory{" +
				"id='" + id + '\'' +
				", stage='" + stage + '\'' +
				", money='" + money + '\'' +
				", expectedDate='" + expectedDate + '\'' +
				", createTime='" + createTime + '\'' +
				", createBy='" + createBy + '\'' +
				", tranId='" + tranId + '\'' +
				", possibility='" + possibility + '\'' +
				'}';
	}
}
