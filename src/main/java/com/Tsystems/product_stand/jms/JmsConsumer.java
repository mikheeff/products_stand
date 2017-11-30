package com.Tsystems.product_stand.jms;


import com.Tsystems.product_stand.services.api.SmallGoodsService;
import com.tsystems.Event;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.Arrays;

public class JmsConsumer implements MessageListener, AutoCloseable {
    private SmallGoodsService smallGoodsService;
    private ActiveMQConnectionFactory _connectionFactory;
    private Connection _connection = null;
    private Session _session = null;
    private MessageConsumer _consumer;
    private String _queueName;

    private static Logger logger = LoggerFactory.getLogger(JmsConsumer.class.getName());

    /**
     * The constructor is used in the case when the broker does not require authorization.
     * Broker ActiveMQ out of the box is configured to work without authorization.
     */
    public JmsConsumer() {

    }

    public JmsConsumer(String url, String queue, SmallGoodsService smallGoodsService) {
        _connectionFactory = new ActiveMQConnectionFactory(url);
        _connectionFactory.setTrustedPackages(Arrays.asList("com.tsystems", "java.lang"));
        _queueName = queue;
        this.smallGoodsService = smallGoodsService;
    }


    /**
     * Initializing the consumer.
     * adding an instance of this class
     * as a subscriber to the event receiving messages.
     */
    public void init() throws JMSException {
        logger.info("JMS init");
        _connection = _connectionFactory.createConnection();
        _connection.start();
        _session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination dest = _session.createQueue(_queueName);
        _consumer = _session.createConsumer(dest);
        logger.info("subscribe on event");
        _consumer.setMessageListener(this);

        logger.info("Consumer successfully initialized");

    }

    /**
     * The event handler for the appearance of a message in the target object.
     * This method is part of the implementation of the MessageListener interface.
     */
    public void onMessage(Message msg) {
        if (msg instanceof ObjectMessage) {
            try {
                logger.info("received an object message");
                ObjectMessage objectMessage = (ObjectMessage) msg;
                Event event = (Event) objectMessage.getObject();
                smallGoodsService.handleEvent(event);
            } catch (JMSException e) {
                logger.error("onMessage error",e);
            }
        } else {
            logger.info("Received message: " + msg.getClass().getName());
        }

    }


    /**
     * The method closes the joins before the object is destroyed.
     * This method is an implementation of the Autoclosable interface,
     * Added in Java7 and used in the try-with-resources block.
     */
    public void close() throws Exception {
        try {
            if (_session != null)
                _session.close();
        } catch (JMSException jmsEx) {
            logger.error("closing error",jmsEx);
        }
        try {
            if (_connection != null)
                _connection.close();
        } catch (JMSException e) {
            logger.error("closing error",e);
        }
    }
}
