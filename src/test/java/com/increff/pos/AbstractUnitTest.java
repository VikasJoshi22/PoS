package com.increff.pos;

import javax.transaction.Transactional;

import com.increff.pos.spring.SpringConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class, loader = AnnotationConfigWebContextLoader.class)
@Transactional
public abstract class AbstractUnitTest{

}
