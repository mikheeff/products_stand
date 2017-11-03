package com.Tsystems.product_stand.jms;


import com.Tsystems.product_stand.DAO.api.SmallGoodsDAO;
import com.Tsystems.product_stand.entities.SmallGoodsEntity;
import com.tsystems.SmallGoods;
import com.tsystems.TestModel;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.*;
import java.util.Arrays;

//@Stateless
//@EJB(beanInterface = JmsConsumer.class, name = "HotelService")
public class JmsConsumer implements MessageListener, AutoCloseable {
    @EJB
    SmallGoodsDAO smallGoodsDAO;
    private final ActiveMQConnectionFactory _connectionFactory;
    private Connection _connection = null;
    private Session _session = null;
    private MessageConsumer _consumer;
    private String _queueName;

    /**
     * Конструктор используется в случае, когда брокер не требует авторизации.
     * Здесь я не стал добавлять вариант с авторизацией. Он показан в producer-е.
     * Брокер ActiveMQ из коробки настроен на работу без авторизации.
     */

    public JmsConsumer(String url, String queue) {
        _connectionFactory = new ActiveMQConnectionFactory(url);
        _connectionFactory.setTrustedPackages(Arrays.asList("com.tsystems"));
        _queueName = queue;
    }

//    public ActiveMQConnectionFactory activeMQConnectionFactory() {
//        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("your broker URL");
//        factory.setTrustedPackages(Arrays.asList("com.my.package"));
//        return factory;
//    }

    /**
     * Инициализация consumer-а.
     * Обратите внимание на добавление экземпляра этого класса
     * в качестве подписчика на событие получения сообщений.
     */
    public void init() throws JMSException {
        System.out.println("Init consumer...");

        _connection = _connectionFactory.createConnection();
        _connection.start();
        _session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = _session.createQueue(_queueName);
        _consumer = _session.createConsumer(dest);
        _consumer.setMessageListener(this); // подписываемся на событие onMessage

        System.out.println("Consumer successfully initialized");

    }

    /**
     * Обработчик события появления сообщения в целевом объекте.
     * Этот метод является частью реализации интерфейса MessageListener.
     */
    public void onMessage(Message msg) {
        if (msg instanceof ObjectMessage) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) msg;
                Object object = objectMessage.getObject();
//                System.out.println("Received message: " + ((TextMessage) msg).getText());

                    SmallGoods smallGoods = (SmallGoods) objectMessage.getObject();

                SmallGoodsEntity smallGoodsEntity = new SmallGoodsEntity();
                smallGoodsEntity.setId(smallGoods.getId());
                smallGoodsEntity.setName(smallGoods.getName());
                smallGoodsEntity.setPrice(smallGoods.getPrice());
                smallGoodsDAO.addSmallGoods(smallGoodsEntity);
//                System.out.println(smallGoods.getName());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else System.out.println("Received message: " + msg.getClass().getName());
    }

    /**
     * Метод закрывает созединения перед разрушением объекта.
     * Этот метод является реализацией интерфейса Autoclosable,
     * добавленого в Java7 и используемого в блоке try-with-resources.
     */
    public void close() throws Exception {
        try {
            if (_session != null)
                _session.close();
        } catch (JMSException jmsEx) {
            jmsEx.printStackTrace();
        }
        try {
            if (_connection != null)
                _connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
