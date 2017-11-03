package com.Tsystems.product_stand.services.impl;

import com.Tsystems.product_stand.services.api.SmallGoodsService;
import com.tsystems.SmallGoods;

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
