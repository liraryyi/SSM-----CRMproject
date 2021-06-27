package com.liraryyi.crm.workbench.dao;

import com.liraryyi.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {
    List<ClueRemark> getClueRemarkByClueId(String clueId);

    int deleteClueRemarkById(String clueId);
}
