package com.Tsystems.product_stand.services.api;

import com.tsystems.Event;
import com.tsystems.SmallGoods;

import java.util.List;

public interface SmallGoodsService {
    List<SmallGoods> getAll();
    void addSmallGoods(SmallGoods smallGoods);
    void loadAllGoodsToDB();
    void handleEvent(Event event);
    void updateSmallGoods(SmallGoods smallGoods);
    void removeAll();
}
