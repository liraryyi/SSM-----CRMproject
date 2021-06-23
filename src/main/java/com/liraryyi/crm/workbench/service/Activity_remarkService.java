package com.liraryyi.crm.workbench.service;

import com.liraryyi.crm.workbench.domain.Activity_remark;
import java.util.List;
import java.util.Map;

public interface Activity_remarkService {

    public List<Activity_remark> getActivity_remarkListById(String id);

    public boolean deleteRemarkById(String id);

    public boolean saveRemarkActivityMap(Map<String,String> map);

    public boolean updateRemark(Map<String, String> map);
}
