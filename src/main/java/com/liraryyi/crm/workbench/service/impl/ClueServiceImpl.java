package com.liraryyi.crm.workbench.service.impl;

import com.liraryyi.crm.utils.DateTimeUtil;
import com.liraryyi.crm.utils.UUIDUtil;
import com.liraryyi.crm.vo.PageListVo;
import com.liraryyi.crm.workbench.dao.*;
import com.liraryyi.crm.workbench.domain.*;
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

    @Resource @Getter @Setter
    private CustomerDao customerDao;

    @Resource @Getter @Setter
    private CustomerRemarkDao customerRemarkDao;

    @Resource @Getter @Setter
    private ContactsDao contactsDao;

    @Resource @Getter @Setter
    private ClueRemarkDao clueRemarkDao;

    @Resource @Getter @Setter
    private ContactsRemarkDao contactsRemarkDao;

    @Resource @Getter @Setter
    private ContactsActivityRelationDao contactsActivityRelationDao;

    @Resource @Getter @Setter
    private TranDao tranDao;

    @Resource @Getter @Setter
    private TranHistoryDao tranHistoryDao;

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

    @Override
    public boolean convertClue(String clueId, Tran tran, String createBy) {
        
        //执行线索转换
        boolean flag = true;

        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getClueById(clueId);

        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String name = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(name);

        if (customer == null){

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateBy(createBy);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setAddress(clue.getAddress());
            customer.setDescription(clue.getDescription());
            customer.setContactSummary(clue.getContactSummary());
            customer.setName(clue.getFullname());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setOwner(clue.getOwner());
            customer.setPhone(clue.getPhone());
            customer.setWebsite(clue.getWebsite());

            int count1 = customerDao.saveCustomer(customer);
            if (count1 !=1){
                flag = false;
            }
        }

        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAddress(clue.getAddress());
        contacts.setDescription(clue.getDescription());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        int count2 = contactsDao.saveContacts(contacts);
        if (count2 != 1){
            flag = false;
        }

        //(4) 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> list = clueRemarkDao.getClueRemarkByClueId(clueId);

        for (ClueRemark clueRemark:list){
            //遍历出的clueRemark对象，一个一个加到客户备注以及联系人备注中

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            contactsRemark.setContactsId(contacts.getId());
            int count3 = contactsRemarkDao.saveContactsRemark(contactsRemark);
            if (count3 != 1){
                flag = false;
            }

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(DateTimeUtil.getSysTime());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(clueRemark.getNoteContent());
            customerRemark.setCustomerId(customer.getId());
            int count4 = customerRemarkDao.saveCustomerRemark(customerRemark);
            if (count4 != 1){
                flag = false;
            }

           // int count5 = clueRemarkDao.deleteClueRemarkById(clueId);
        }
        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系

        String[] activityId = clueActivityRelationDao.selectActivityIdByClueId(clueId);

        for (int i = 0; i < activityId.length; i++) {

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId[i]);
            contactsActivityRelation.setContactsId(contacts.getId());

            int count5 = contactsActivityRelationDao.saveContactsActivityRelation(contactsActivityRelation);

            if (count5 != 1){
                flag = false;
            }
        }

        //(6) 如果有创建交易需求，创建一条交易
        if (tran !=null){

            //将交易的信息进一步完善
            tran.setSource(clue.getSource());
            tran.setOwner(clue.getOwner());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setCustomerId(customer.getId());
            tran.setContactSummary(clue.getContactSummary());
            tran.setContactsId(contacts.getId());

            int count6 = tranDao.saveTran(tran);
            if (count6 != 1){
                flag = false;
            }

            //(7)如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setTranId(tran.getId());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setStage(tran.getStage());

            int count7 = tranHistoryDao.saveTranHistory(tranHistory);
            if (count7 != 1){
                flag = false;
            }
        }
        //			(8) 删除线索备注
        //			(9) 删除线索和市场活动的关系
        //			(10) 删除线索

        int count8 = clueRemarkDao.deleteClueRemarkById(clueId);
        int count9 = clueActivityRelationDao.deleteByClueId(clueId);
        int count10 = clueDao.deleteById(clueId);

        if (count8 != 1|| count9 != 1 ||count10 !=1){
            flag = false;
        }

        return flag;
    }
}
