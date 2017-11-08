package com.Tsystems.product_stand.services.impl;

import com.Tsystems.product_stand.Configuration.ConfigurationClass;
import com.Tsystems.product_stand.DAO.api.SmallGoodsDAO;
import com.Tsystems.product_stand.entities.SmallGoodsEntity;
import com.Tsystems.product_stand.services.api.SmallGoodsService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tsystems.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class SmallGoodsServiceImpl implements SmallGoodsService{

    @EJB
    SmallGoodsDAO smallGoodsDAO;

    @Override
    public List<SmallGoods> getAll() {
        List<SmallGoods> smallGoodsList = new ArrayList<>();
        for (SmallGoodsEntity smallGoodsEntity : smallGoodsDAO.getAll()){
            SmallGoods smallGoods = new SmallGoods();
            smallGoods.setId(smallGoodsEntity.getId());
            smallGoods.setName(smallGoodsEntity.getName());
            smallGoods.setPrice(smallGoodsEntity.getPrice());
            smallGoods.setImg(smallGoodsEntity.getImg());
            smallGoods.setSalesCounter(smallGoodsEntity.getSalesCounter());
            smallGoodsList.add(smallGoods);
        }
        return smallGoodsList;
    }

    @Override
    public void addSmallGoods(SmallGoods smallGoods) {
        SmallGoodsEntity smallGoodsEntity = new SmallGoodsEntity();
        smallGoodsEntity.setId(smallGoods.getId());
        smallGoodsEntity.setName(smallGoods.getName());
        smallGoodsEntity.setPrice(smallGoods.getPrice());
        smallGoodsEntity.setImg(smallGoods.getImg());
        smallGoodsDAO.addSmallGoods(smallGoodsEntity);
    }

    @Override
    public void removeAll() {
        smallGoodsDAO.removeAll();
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof AddEvent){
            addSmallGoods((SmallGoods) event.getProperty());
        }
        if (event instanceof DeleteEvent){
            smallGoodsDAO.deleteSmallGoodsById((Integer)event.getProperty());
        }
        if (event instanceof UpdateEvent){
            updateSmallGoods((SmallGoods)event.getProperty());
        }
    }

    @Override
    public void updateSmallGoods(SmallGoods smallGoods) {
        SmallGoodsEntity smallGoodsEntity = smallGoodsDAO.getSmallGoodsById(smallGoods.getId());
        smallGoodsEntity.setName(smallGoods.getName());
        smallGoodsEntity.setPrice(smallGoods.getPrice());
        smallGoodsEntity.setImg(smallGoods.getImg());
        smallGoodsDAO.updateSmallGoods(smallGoodsEntity);
    }

    @Override
    public void loadAllGoodsToDB() {
        List<SmallGoods> SmallGoodsList = new ArrayList<>();
        Client client = Client.create();

        WebResource webResource = client.resource(ConfigurationClass.SERVER_URL+"/catalog/goodsAll");
        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            return;
        }

        String json = response.getEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            SmallGoodsList = mapper.readValue(json, new TypeReference<List<SmallGoods>>(){});
            for (SmallGoods goods : SmallGoodsList){
                this.addSmallGoods(goods);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
