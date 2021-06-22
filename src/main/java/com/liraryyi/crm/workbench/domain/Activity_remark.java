package com.liraryyi.crm.workbench.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Activity_remark {

    private String id;   //
    private String noteContent;   //
    private String createTime;   //
    private String createBy;   //
    private String editTime;   //
    private String editBy;   //
    private String editFlag;   //
    private String activityId;   //

    @Override
    public String toString() {
        return "Activity_remark{" +
                "id='" + id + '\'' +
                ", noteContent='" + noteContent + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createBy='" + createBy + '\'' +
                ", editTime='" + editTime + '\'' +
                ", editBy='" + editBy + '\'' +
                ", editFlag='" + editFlag + '\'' +
                ", activityId='" + activityId + '\'' +
                '}';
    }
}
