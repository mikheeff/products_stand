package com.Tsystems.product_stand.controllers;

import com.Tsystems.product_stand.DAO.api.SmallGoodsDAO;
import com.Tsystems.product_stand.entities.SmallGoodsEntity;
import com.Tsystems.product_stand.models.SmallGoods;
import com.Tsystems.product_stand.services.api.SmallGoodsService;
import com.Tsystems.product_stand.services.impl.SmallGoodsServiceImpl;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

@SessionScoped
@ManagedBean
public class MainView implements Serializable {
    @EJB
    SmallGoodsDAO smallGoodsDAO;

    private String hello = "Hello";

    public List<SmallGoodsEntity> getAllGoods(){
        return smallGoodsDAO.getAll();
    }

    public void changeHello() {
        hello = hello + new Random().nextInt();
    }

    public String getHello() {
        return hello;
    } //

    public void setHello(String hello) {
        this.hello = hello;
    }
}