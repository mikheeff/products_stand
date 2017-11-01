package com.Tsystems.product_stand.services.impl;

import com.Tsystems.product_stand.DAO.impl.SmallGoodsDAOImpl;
import com.Tsystems.product_stand.entities.SmallGoodsEntity;
import com.Tsystems.product_stand.models.SmallGoods;
import com.Tsystems.product_stand.services.api.SmallGoodsService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
//@Stateless
public class SmallGoodsServiceImpl implements SmallGoodsService{

//    @EJB
//    SmallGoodsDAOImpl smallGoodsDAO;

    @Override
    public List<SmallGoods> getAll() {
        List<SmallGoods> smallGoodsList = new ArrayList<>();
//        for (SmallGoodsEntity smallGoodsEntity : smallGoodsDAO.getAll()){
//            SmallGoods smallGoods = new SmallGoods();
//            smallGoods.setId(smallGoodsEntity.getId());
//            smallGoods.setName(smallGoodsEntity.getName());
//            smallGoods.setPrice(smallGoodsEntity.getPrice());
//            smallGoodsList.add(smallGoods);
//        }
        return smallGoodsList;
    }
}
