package com.increff.pos;

import org.springframework.transaction.annotation.Transactional;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class)
@WebAppConfiguration
@Transactional
public abstract class AbstractUnitTest{

}
