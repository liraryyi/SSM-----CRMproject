package com.liraryyi.crm.workbench.service.impl;

import com.liraryyi.crm.settings.domain.User;
import com.liraryyi.crm.workbench.dao.ActivityDao;
import com.liraryyi.crm.workbench.service.ActivityService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Resource
    @Setter @Getter
    private ActivityDao activityDao;


}
