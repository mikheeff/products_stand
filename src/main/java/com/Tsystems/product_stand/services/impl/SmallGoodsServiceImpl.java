package com.Tsystems.product_stand.services.impl;

import com.Tsystems.product_stand.Configuration.ConfigurationClass;
import com.Tsystems.product_stand.DAO.api.SmallGoodsDAO;
import com.Tsystems.product_stand.PushEvent;
import com.Tsystems.product_stand.controllers.MainView;
import com.Tsystems.product_stand.entities.SmallGoodsEntity;
import com.Tsystems.product_stand.jms.JmsConsumer;
import com.Tsystems.product_stand.services.api.SmallGoodsService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tsystems.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.omnifaces.config.BeanManager;
import org.primefaces.context.RequestContext;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.net.URLConnection;

@Stateless
@ApplicationScoped
public class SmallGoodsServiceImpl implements SmallGoodsService{

    @EJB
    SmallGoodsDAO smallGoodsDAO;
    @Inject
    private javax.enterprise.inject.spi.BeanManager beanManager;

    @Override
    public List<SmallGoods> getAll() {
        return convertToDTO(smallGoodsDAO.getAll());
    }

    @Override
    public List<SmallGoods> getBestSellers() {
        return convertToDTO(smallGoodsDAO.getBestSellers());
    }

    List<SmallGoods> convertToDTO(List <SmallGoodsEntity> smallGoodsEntityList){
        List<SmallGoods> smallGoodsList = new ArrayList<>();
        for (SmallGoodsEntity smallGoodsEntity : smallGoodsEntityList){
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
        smallGoodsEntity.setSalesCounter(smallGoods.getSalesCounter());
        smallGoodsDAO.addSmallGoods(smallGoodsEntity);
    }

    public void receiveMessage() throws JMSException {
        SmallGoodsService smallGoodsService = this;
        String url = ConfigurationClass.ACTIVE_MQ_URL; // broker connector url
        JmsConsumer consumer = new JmsConsumer(url, "test.in",smallGoodsService);
        consumer.init();
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

        beanManager.fireEvent(new PushEvent(event.getProperty().toString()));
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
        List<SmallGoods> SmallGoodsList;
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
