package com.liraryyi.crm.workbench.dao;

import com.liraryyi.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int saveTran(Tran tran);

    int selectListCount(Map<String,Object> map);

    List<Tran> selectTranList(Map<String, Object> map);

    Tran getTranById( String id);

    int updateStage(Tran tran);
}
