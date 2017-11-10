package com.Tsystems.product_stand.jms;


import com.Tsystems.product_stand.DAO.api.SmallGoodsDAO;
import com.Tsystems.product_stand.DAO.impl.SmallGoodsDAOImpl;
import com.Tsystems.product_stand.controllers.MainView;
import com.Tsystems.product_stand.entities.SmallGoodsEntity;
import com.Tsystems.product_stand.services.api.SmallGoodsService;
import com.tsystems.Event;
import com.tsystems.SmallGoods;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.*;
import java.util.Arrays;
public class JmsConsumer implements MessageListener, AutoCloseable {
    private SmallGoodsService smallGoodsService;
    private ActiveMQConnectionFactory _connectionFactory;
    private Connection _connection = null;
    private Session _session = null;
    private MessageConsumer _consumer;
    private String _queueName;

    /**
     * Конструктор используется в случае, когда брокер не требует авторизации.
     * Здесь я не стал добавлять вариант с авторизацией. Он показан в producer-е.
     * Брокер ActiveMQ из коробки настроен на работу без авторизации.
     */
    public JmsConsumer(){

    }

    public JmsConsumer(String url, String queue, SmallGoodsService smallGoodsService) {
        _connectionFactory = new ActiveMQConnectionFactory(url);
        _connectionFactory.setTrustedPackages(Arrays.asList("com.tsystems","java.lang"));
        _queueName = queue;
        this.smallGoodsService = smallGoodsService;
    }


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
                Event event = (Event) objectMessage.getObject();
                smallGoodsService.handleEvent(event);
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
