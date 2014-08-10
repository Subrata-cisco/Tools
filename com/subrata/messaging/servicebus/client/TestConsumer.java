package com.subrata.messaging.servicebus.client;

import com.subrata.messaging.servicebus.core.MessageBusException;
import com.subrata.messaging.servicebus.core.MessageConsumer;

public class TestConsumer {
	public static void main(String[] args) {
		try {
			new MessageConsumer().startListening();
		} catch (MessageBusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("***************** ERROR Consumer ::"
					+ e.getMessage());
		}
	}
}
