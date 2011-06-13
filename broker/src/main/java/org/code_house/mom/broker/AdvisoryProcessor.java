package org.code_house.mom.broker;

import javax.jms.Message;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.jms.JmsMessage;

public class AdvisoryProcessor implements Processor {

	public void process(Exchange exchange) throws Exception {
		JmsMessage in = exchange.getIn(JmsMessage.class);
		Message m = in.getJmsMessage();
		JmsMessage out = in.newInstance();
		out.setJmsMessage(m);
		exchange.setOut(out);
	}

}
