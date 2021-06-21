package com.liraryyi.crm.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class PageListVo<T> {
    private int total;
    private List<T> list;



}
