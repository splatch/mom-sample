package org.code_house.mom.auditor;

import java.io.IOException;

import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQMessage;
import org.apache.camel.Message;
import org.apache.camel.component.jms.JmsMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;

public class Auditor implements InitializingBean {

	private Session driver;
	private Database database;

	public void onMessage(Message message) throws Exception {
		audit(getActiveMQMessage(message));
	}
	
	private ActiveMQMessage getActiveMQMessage(Message message) {
		return (ActiveMQMessage) ((JmsMessage) message).getJmsMessage();
	}

	public void onDelivered(Message message) throws Exception {
		ActiveMQMessage msg = getActiveMQMessage(message);

		advisoryDelivered(msg);
	}

	public void onConsumed(Message message) throws Exception {
		ActiveMQMessage msg = getActiveMQMessage(message);

		advisoryConsumed(msg);
	}


	public Document getDocument(String jmsId) {
		Document doc = null;
		try {
			doc = database.getDocument(jmsId);
		} catch (IOException e) {
			
		}

		if (doc == null) {
			doc = new Document();
			doc.setId(jmsId);
		}
		return doc;
	}

	private void audit(ActiveMQMessage msg) throws Exception {
		if (msg.getJMSCorrelationID() == null) {
			System.err.println("Unknown message " + msg);
			return;
		}

		Document document = getDocument(msg.getJMSCorrelationID());
		document.put("sent", msg.getJMSTimestamp());
		document.put("body", ((TextMessage) msg).getText());
		database.saveDocument(document);
	}

	private void advisoryDelivered(ActiveMQMessage msg) throws Exception {
		Document document = getDocument(msg.getCorrelationId());
		
		if (!document.isEmpty()) {
			document.put("delivered", true);
			database.saveDocument(document);
		}
	}

	private void advisoryConsumed(ActiveMQMessage msg) throws Exception {
		Document document = getDocument(msg.getCorrelationId());

		if (!document.isEmpty()) {
			document.put("consumed", true);
			database.saveDocument(document);
			
		}
	}

	public void setDriver(Session driver) {
		this.driver = driver;
	}

	public void afterPropertiesSet() throws Exception {
		database = driver.getDatabase("audit");
	}
	
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("auditor.xml");
		context.start();

		System.console().readLine();

		context.stop();
	}
}
