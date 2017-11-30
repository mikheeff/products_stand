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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
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
@TransactionManagement (TransactionManagementType.CONTAINER)
public class SmallGoodsServiceImpl implements SmallGoodsService{

    @EJB
    SmallGoodsDAO smallGoodsDAO;
    @Inject
    private javax.enterprise.inject.spi.BeanManager beanManager;

    private static Logger logger = LoggerFactory.getLogger(SmallGoodsService.class.getName());

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public List<SmallGoods> getAll() {
        return convertToDTO(smallGoodsDAO.getAll());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public List<SmallGoods> getBestSellers() {
        return convertToDTO(smallGoodsDAO.getBestSellers());
    }

    /**
     * converts Small goods to data access object
     * @param smallGoodsEntityList
     * @return
     */
    List<SmallGoods> convertToDTO(List <SmallGoodsEntity> smallGoodsEntityList){
        List<SmallGoods> smallGoodsList = new ArrayList<>();
        for (SmallGoodsEntity smallGoodsEntity : smallGoodsEntityList){
            SmallGoods smallGoods = new SmallGoods();
            smallGoods.setId(smallGoodsEntity.getId());
            smallGoods.setName(smallGoodsEntity.getName());
            smallGoods.setPrice(smallGoodsEntity.getPrice());
            smallGoods.setSalesCounter(smallGoodsEntity.getSalesCounter());
            smallGoods.setVisible(smallGoodsEntity.getVisible());
            smallGoodsList.add(smallGoods);
        }
        return smallGoodsList;
    }

    /**
     * add new goods
     * @param smallGoods
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void addSmallGoods(SmallGoods smallGoods) {
        SmallGoodsEntity smallGoodsEntity = new SmallGoodsEntity();
        smallGoodsEntity.setId(smallGoods.getId());
        smallGoodsEntity.setName(smallGoods.getName());
        smallGoodsEntity.setPrice(smallGoods.getPrice());
        smallGoodsEntity.setSalesCounter(smallGoods.getSalesCounter());
        smallGoodsEntity.setVisible(smallGoods.getVisible());
        smallGoodsDAO.addSmallGoods(smallGoodsEntity);
    }

    /**
     * creates new instance of JmsConsumer to receive messages
     * @throws JMSException
     */
    public void receiveMessage() throws JMSException {
        SmallGoodsService smallGoodsService = this;
        String url = ConfigurationClass.ACTIVE_MQ_URL; // broker connector url
        JmsConsumer consumer = new JmsConsumer(url, "connection.in",smallGoodsService);
        consumer.init();
    }

    /**
     * removes all goods from database
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void removeAll() {
        logger.info("removing all goods");
        smallGoodsDAO.removeAll();
    }

    /**
     * handles event from broker and identify its type, then socket push event to client
     * @param event
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void handleEvent(Event event) {
        if (event instanceof AddEvent){
            logger.info("new Add Event " + event.getProperty().toString());
            addSmallGoods((SmallGoods) event.getProperty());
        }
        if (event instanceof DeleteEvent){
            logger.info("new Delete Event " + event.getProperty().toString());
            smallGoodsDAO.deleteSmallGoodsById((Integer)event.getProperty());
        }
        if (event instanceof UpdateEvent){
            logger.info("new Update Event " + event.getProperty().toString());
            updateSmallGoods((SmallGoods)event.getProperty());
        }

        logger.info("new socket push event");
        beanManager.fireEvent(new PushEvent(event.getProperty().toString()));
    }

    /**
     * updates goods
     * @param smallGoods
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void updateSmallGoods(SmallGoods smallGoods) {
        SmallGoodsEntity smallGoodsEntity = smallGoodsDAO.getSmallGoodsById(smallGoods.getId());
        smallGoodsEntity.setName(smallGoods.getName());
        smallGoodsEntity.setPrice(smallGoods.getPrice());
        smallGoodsEntity.setSalesCounter(smallGoods.getSalesCounter());
        smallGoodsEntity.setVisible(smallGoods.getVisible());
        smallGoodsDAO.updateSmallGoods(smallGoodsEntity);
    }

    /**
     * loads all goods to database from server app
     */
    @Override
    public void loadAllGoodsToDB() {
        logger.info("loading goods to database...");
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
            logger.error("mapping error",e);
        }
    }
}
