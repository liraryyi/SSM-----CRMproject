package com.liraryyi.crm.workbench.service.impl;

import com.liraryyi.crm.utils.DateTimeUtil;
import com.liraryyi.crm.utils.UUIDUtil;
import com.liraryyi.crm.vo.PageListVo;
import com.liraryyi.crm.workbench.dao.CustomerDao;
import com.liraryyi.crm.workbench.dao.TranDao;
import com.liraryyi.crm.workbench.dao.TranHistoryDao;
import com.liraryyi.crm.workbench.domain.Customer;
import com.liraryyi.crm.workbench.domain.Tran;
import com.liraryyi.crm.workbench.domain.TranHistory;
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

    @Resource @Getter @Setter
    private CustomerDao customerDao;

    @Resource @Getter @Setter
    private TranHistoryDao tranHistoryDao;

    @Override
    public PageListVo<Tran> getTranList(Map<String, Object> map) {

        PageListVo<Tran> pageListVo = new PageListVo<>();

        int total = tranDao.selectListCount(map);

        List<Tran> list = tranDao.selectTranList(map);

        pageListVo.setTotal(total);
        pageListVo.setList(list);
        return pageListVo;
    }

    @Override
    public boolean saveTran(Tran tran, String customerName) {

        /*
        交易添加业务:
           1.判断customerName,根据客户名称在客户表进行精确查询
              有就取出id添加
              没有就先创建一个，再取id添加
           2.添加交易
           3.创建一条交易历史
         */
        boolean flag = true;

        Customer customer = customerDao.getCustomerByName(customerName);

        if (customer == null){

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setCreateBy(tran.getCreateBy());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            int count3 = customerDao.saveCustomer(customer);
            if (count3 != 1){
                flag =false;
            }
        }
        tran.setCustomerId(customer.getId());
        int count1 = tranDao.saveTran(tran);
        if (count1 != 1){
            flag = false;
        }

        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        int count2 = tranHistoryDao.saveTranHistory(tranHistory);
        if (count2 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran getTranById(String id) {

        Tran tran = tranDao.getTranById(id);

        return tran;
    }

    @Override
    public List<TranHistory> getTranHistoryByTranId(String tranId) {

        List<TranHistory> list = tranHistoryDao.getListByTranId(tranId);

        return list;
    }

    @Override
    public boolean changeStage(Tran tran) {

        boolean flag = true;

        int count1 = tranDao.updateStage(tran);

        if (count1 != 1){
            flag = false;
        }

        //再生成一条交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        int count2 = tranHistoryDao.saveTranHistory(tranHistory);
        if (count2 != 1){
            flag = false;
        }

        return flag;
    }
}






























