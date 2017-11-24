package com.Tsystems.product_stand;

import com.Tsystems.product_stand.services.api.SmallGoodsService;

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
    @PostConstruct
    public void init() {
        smallGoodsService.removeAll();
        smallGoodsService.loadAllGoodsToDB();
        try {
            smallGoodsService.receiveMessage();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
