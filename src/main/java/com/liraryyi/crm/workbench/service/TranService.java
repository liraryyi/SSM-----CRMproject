package com.liraryyi.crm.workbench.service;

import com.liraryyi.crm.workbench.domain.Tran;
import com.liraryyi.crm.vo.PageListVo;

import java.util.Map;

public interface TranService {

    PageListVo<Tran> getTranList(Map<String,Object> map);
}
