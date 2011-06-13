package org.code_house.mom.producer;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {

	public static void main(String[] args) throws JMSException {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:4000");

		ProducerRunnable producerRunnable = new ProducerRunnable(connectionFactory, "admin", "admin");
		
		Thread thread = new Thread(producerRunnable, "producer-thread");
		thread.start();


	}

}
