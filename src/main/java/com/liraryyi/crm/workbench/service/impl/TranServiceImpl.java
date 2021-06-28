package com.liraryyi.crm.workbench.service.impl;


import com.liraryyi.crm.vo.PageListVo;
import com.liraryyi.crm.workbench.dao.TranDao;
import com.liraryyi.crm.workbench.domain.Tran;
import com.liraryyi.crm.workbench.service.TranService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Resource @Getter @Setter
    private TranDao tranDao;

    @Override
    public PageListVo<Tran> getTranList(Map<String, Object> map) {

        PageListVo<Tran> pageListVo = new PageListVo<>();

        int total = tranDao.selectListCount(map);

        List<Tran> list = tranDao.selectTranList(map);

        pageListVo.setTotal(total);
        pageListVo.setList(list);
        return pageListVo;
    }
}
