package com.Tsystems.product_stand.DAO.api;

import com.Tsystems.product_stand.entities.SmallGoodsEntity;

import javax.ejb.Remote;
import java.util.List;
@Remote
public interface SmallGoodsDAO {
    List<SmallGoodsEntity> getAll();
    void addSmallGoods(SmallGoodsEntity smallGoodsEntity);
    void deleteSmallGoodsById(int id);
    SmallGoodsEntity getSmallGoodsById(int id);
    void updateSmallGoods(SmallGoodsEntity smallGoodsEntity);
    void removeAll();
}
