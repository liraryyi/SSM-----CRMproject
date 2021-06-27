package com.liraryyi.crm.workbench.dao;


import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {

    String[] selectActivityIdByClueId(String clueId);

    int deleteById(String id);

    int insertActivity(Map<String,String> map);

    int deleteByClueId(String id);
}
