package com.liraryyi.crm.workbench.dao;

import com.liraryyi.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String name);

    int saveCustomer(Customer customer);
}
