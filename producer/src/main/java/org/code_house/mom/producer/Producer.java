package org.code_house.mom.producer;

import javax.jms.JMSException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Entry point to application.
 * 
 * @author ldywicki
 */
public class Producer {

	public static void main(String[] args) throws JMSException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("producer.xml");

		context.start();
	}

}
