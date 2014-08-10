package com.subrata.messaging.servicebus.core;

import javax.jms.DeliveryMode;
import javax.jms.Queue;

public class MessageProducer extends QueueProducer {

	public MessageProducer() throws MessageBusException {
		super();
	}

	@Override
	public void sendMessage(Payload payload) throws MessageBusException {
		Queue queue = ServiceLocator.getInstance().getAppleMessageQueue();
		deliverPayloadToQueue(queue,payload,DeliveryMode.NON_PERSISTENT);
	}
	
	//public void sendTestMessage() throws MessageBusException {
	public static void main(String[] args) throws MessageBusException   {
		MessageProducer appleMessageProducer = new MessageProducer();
		Payload payload = new Payload();
		payload.setMessage("Hey Sub wats up! Test message......");
		payload.setToken("debdace8c43ef629143ae18ae8afe99afa6b5383927b20e5bdbcf05b45f8faf8");
		payload.setBadge(4);
		appleMessageProducer.sendMessage(payload);
	}

}
