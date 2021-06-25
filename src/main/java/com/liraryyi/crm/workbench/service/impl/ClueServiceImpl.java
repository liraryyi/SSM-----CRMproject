package com.liraryyi.crm.workbench.service.impl;

import com.liraryyi.crm.utils.UUIDUtil;
import com.liraryyi.crm.vo.PageListVo;
import com.liraryyi.crm.workbench.dao.ClueActivityRelationDao;
import com.liraryyi.crm.workbench.dao.ClueDao;
import com.liraryyi.crm.workbench.domain.Clue;
import com.liraryyi.crm.workbench.service.ClueService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {

    @Resource @Getter @Setter
    private ClueDao clueDao;

    @Resource @Getter @Setter
    private ClueActivityRelationDao clueActivityRelationDao;

    @Override
    public boolean saveClue(Clue clue) {

        boolean flag =true;

        int count = clueDao.insertClue(clue);

        if (count !=1){flag = false;}

        return flag;
    }

    @Override
    public PageListVo<Clue> getClueList(Map<String, Object> map) {


        PageListVo pageListVo = new PageListVo<Clue>();

        int total = clueDao.selectClueCount(map);

        List<Clue> list = clueDao.selectClueList(map);

        pageListVo.setTotal(total);
        pageListVo.setList(list);

        return pageListVo;
    }

    @Override
    public Clue getClueListById(String id) {

        Clue clue = clueDao.getClueById(id);
        return clue;
    }

    @Override
    public boolean deleteRelation(String id) {

        boolean flag = true;

        int count = clueActivityRelationDao.deleteById(id);

        if (count !=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean bindActivity(String cid, String[] aid) {

        boolean flag = true;
        int count = 0;
        for (int i = 0; i < aid.length; i++) {
            String id = UUIDUtil.getUUID();
            Map<String,String> map = new HashMap<>();
            map.put("id",id);
            map.put("cid",cid);
            map.put("aid",aid[i]);
            int count1 = clueActivityRelationDao.insertActivity(map);
            count = count + count1;
        }

        if (count != aid.length){
            flag = false;
        }
        return flag;
    }
}
