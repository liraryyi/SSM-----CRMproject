package com.liraryyi.crm.workbench.dao;

import com.liraryyi.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    int insertClue(Clue clue);

    int selectClueCount(Map<String,Object> map);

    List<Clue> selectClueList(Map<String,Object> map);

    Clue  getClueById(String id);

    int deleteById(String clueId);

    Clue getAllClueById(String clueId);
}
