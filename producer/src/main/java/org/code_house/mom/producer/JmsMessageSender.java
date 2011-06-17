package org.code_house.mom.producer;

import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.InitializingBean;

/**
 * Very simple code. Sends JMS messages to queue.
 * 
 * @author ldywicki
 */
public class JmsMessageSender implements MessageSender, InitializingBean {

	private ConnectionFactory connectionFactory;
	private Session session;
	private final String destination;
	private MessageProducer producer;

	public JmsMessageSender(String destination) {
		this.destination = destination;
	}

	public void sendMessage(String message, Map<String, Object> headers)
		throws Exception {

		if (session == null || producer == null) {
			afterPropertiesSet();
		}

		TextMessage jmsMsg = session.createTextMessage(message);
		for (String header : headers.keySet()) {
			jmsMsg.setObjectProperty(header, headers.get(header));
		}
		producer.send(jmsMsg);
	}

	public void afterPropertiesSet() throws Exception {
		Connection connection = connectionFactory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(destination);
		producer = session.createProducer(queue);
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	
}
