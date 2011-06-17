package org.code_house.mom.producer;

import java.util.Map;

public interface MessageSender {

	void sendMessage(String message, Map<String, Object> headers) throws Exception;

}
