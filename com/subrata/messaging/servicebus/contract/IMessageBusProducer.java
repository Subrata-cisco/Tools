package com.subrata.messaging.servicebus.contract;

import com.subrata.messaging.servicebus.core.MessageBusException;
import com.subrata.messaging.servicebus.core.Payload;

public interface IMessageBusProducer {
	public void sendMessage (Payload payload) throws MessageBusException;
}
