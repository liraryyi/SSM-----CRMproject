package com.liraryyi.crm.workbench.service;

import com.liraryyi.crm.workbench.domain.Clue;
import com.liraryyi.crm.vo.PageListVo;
import com.liraryyi.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {

    boolean saveClue(Clue clue);

    PageListVo<Clue> getClueList(Map<String,Object> map);

    Clue getClueListById(String id);

    boolean deleteRelation(String id);

    boolean bindActivity(String cid,String[] aid);

    boolean convertClue(String clueId, Tran tran, String createBy);
}
