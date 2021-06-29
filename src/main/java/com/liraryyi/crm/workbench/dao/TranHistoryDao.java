package com.liraryyi.crm.workbench.dao;

import com.liraryyi.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {
    int saveTranHistory(TranHistory tranHistory);

    List<TranHistory> getListByTranId(String tranId);
}
