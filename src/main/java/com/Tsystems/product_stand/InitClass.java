package com.Tsystems.product_stand;

import com.Tsystems.product_stand.services.api.SmallGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.JMSException;
import java.io.Serializable;
@Startup
@Singleton
public class InitClass implements Serializable {
    @EJB
    private SmallGoodsService smallGoodsService;

    private static Logger logger = LoggerFactory.getLogger(InitClass.class.getName());

    /**
     * clean database and then reload goods from server app,
     * then initializes JMS
     */
    @PostConstruct
    public void init() {
        logger.info("initialize application");
        smallGoodsService.removeAll();
        smallGoodsService.loadAllGoodsToDB();
        try {
            smallGoodsService.receiveMessage();
        } catch (JMSException e) {
            logger.error("init error",e);
        }
    }
}
