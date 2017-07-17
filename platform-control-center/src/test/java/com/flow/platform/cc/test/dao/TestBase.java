package com.flow.platform.cc.test.dao;

import com.flow.platform.cc.dao.AgentDao;
import com.flow.platform.cc.dao.CmdDao;
import com.flow.platform.cc.dao.CmdResultDao;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by gy@fir.im on 23/06/2017.
 * Copyright fir.im
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {HibernateConfig.class})
public abstract class TestBase {

    @Autowired
    protected AgentDao agentDao;

    @Autowired
    protected CmdDao cmdDao;

    @Autowired
    protected CmdResultDao cmdResultDao;
}