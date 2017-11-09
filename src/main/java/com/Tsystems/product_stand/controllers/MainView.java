package com.Tsystems.product_stand.controllers;

import com.Tsystems.product_stand.Configuration.ConfigurationClass;
import com.Tsystems.product_stand.DAO.api.SmallGoodsDAO;
import com.Tsystems.product_stand.entities.SmallGoodsEntity;
import com.Tsystems.product_stand.jms.JmsConsumer;
import com.Tsystems.product_stand.services.api.SmallGoodsService;
import com.tsystems.SmallGoods;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.jms.JMSException;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

@ApplicationScoped
@ManagedBean
public class MainView implements Serializable {
    @EJB
    SmallGoodsService smallGoodsService;

//    private List<SmallGoods> smallGoodsList;

    private String hello = "Hello";

    @PostConstruct
    public void init(){
        smallGoodsService.removeAll();
        smallGoodsService.loadAllGoodsToDB();
//        smallGoodsList = smallGoodsService.getBestSellers();
        try {
            receiveMessage();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

//    public List<SmallGoods> getSmallGoodsList() {
//        return smallGoodsList;
//    }

    public List<SmallGoods> getBestSellers() {
        return smallGoodsService.getBestSellers();
    }


    public List<SmallGoods> getAllGoods() {
        return smallGoodsService.getAll();
    }


    public void changeHello() throws JMSException {
        hello = hello + new Random().nextInt();

    }

    public void receiveMessage() throws JMSException{
        String url = ConfigurationClass.ACTIVE_MQ_URL; // broker connector url
        JmsConsumer consumer = new JmsConsumer(url, "test.in",smallGoodsService);
        consumer.init();
    }


    public String getHello() {
        return hello;
    } //

    public void setHello(String hello) {
        this.hello = hello;
    }
}
