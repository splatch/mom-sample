package org.code_house.mom.producer;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.code_house.mom.domain.Client;
import org.code_house.mom.domain.Money;
import org.code_house.mom.domain.Money.Currency;
import org.code_house.mom.domain.Transfer;
import org.codehaus.jackson.map.ObjectMapper;

public class ProducerRunnable implements Runnable {

	private static String[] clients = new String[] {
		"Client 0",
		"Client 1",
		"Client 2",
		"Client 3"
	};

	private static int pointer = 0;

	private Connection connection;

	public ProducerRunnable(ConnectionFactory factory, String user, String pass) throws JMSException {
		connection = factory.createConnection(user, pass);
	}

	public void run() {
		try {
			while (!Thread.interrupted()) {
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

				Transfer transfer = createTransfer();
				ObjectMapper mapper = new ObjectMapper();
				String value = mapper.writeValueAsString(transfer);

				TextMessage message = session.createTextMessage(value);
				message.setLongProperty("destination", transfer.getDestination().getId());

				Queue destination = session.createQueue("MOM.Incoming");
				MessageProducer producer = session.createProducer(destination);
				producer.send(message);
				System.out.println("Send message " + message.getJMSMessageID() + " to " + transfer.getDestination().getName());

				Thread.sleep(TimeUnit.SECONDS.toMillis(10));
			}
		} catch (Exception e) {
			System.err.println("Unexpected error, closing producer.");
			e.printStackTrace(System.err);
		}
	}

	private Transfer createTransfer() {
		Transfer transfer = new Transfer();
		transfer.setUuid(UUID.randomUUID().toString());

		Money money = new Money();
		money.setCurrency(Currency.PLN);
		money.setAmount(new Random().nextDouble());

		Client client = new Client();
		client.setId(new Long(pointer));
		client.setName(clients[pointer++]);

		if (pointer + 1 > clients.length) {
			pointer = 0;
		}

		transfer.setDestination(client);
		transfer.setMoney(money);

		return transfer;
	}

}
