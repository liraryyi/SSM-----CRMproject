package com.liraryyi.crm.workbench.service;

import com.liraryyi.crm.workbench.domain.Tran;
import com.liraryyi.crm.vo.PageListVo;
import com.liraryyi.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {

    PageListVo<Tran> getTranList(Map<String,Object> map);

    boolean saveTran(Tran tran,String customerName);

    Tran getTranById(String id);

    List<TranHistory> getTranHistoryByTranId(String tranId);

    boolean changeStage(Tran tran);
}
