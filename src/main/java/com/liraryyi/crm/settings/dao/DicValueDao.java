package com.liraryyi.crm.settings.dao;

import com.liraryyi.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {

    List<DicValue> getDicValueByType(String code);
}