package com.subrata.messaging.servicebus.client;

import com.subrata.messaging.servicebus.core.MessageBusException;
import com.subrata.messaging.servicebus.core.MessageProducer;
import com.subrata.messaging.servicebus.core.Payload;


public class TestProducer {
	
	public static void main(String[] args) {
		try {
			MessageProducer appleMessageProducer = new MessageProducer();
			Payload payload = new Payload();
			payload.setMessage("Hey Sub wats up! Test message......");
			payload.setToken("debdace8c43ef629143ae18ae8afe99afa6b5383927b20e5bdbcf05b45f8faf8");
			payload.setBadge(4);
			appleMessageProducer.sendMessage(payload);
		} catch (MessageBusException e) {
			System.out.println("***************** ERROR ::"+e.getMessage());
		}

	}

}
