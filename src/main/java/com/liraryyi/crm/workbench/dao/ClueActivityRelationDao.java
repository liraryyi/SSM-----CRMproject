package com.liraryyi.crm.workbench.dao;


import java.util.List;

public interface ClueActivityRelationDao {

    String[] selectActivityIdByClueId(String clueId);

    int deleteById(String id);
}
