package com.subrata.messaging.servicebus.core;

import javax.jms.MessageListener;
import javax.jms.Queue;

import com.subrata.messaging.servicebus.contract.IMessageBusConsumer;

public class MessageConsumer extends QueueConsumer {

	MessageListener listener;
	
	public MessageConsumer() throws MessageBusException {
		super();
	}
	
	public void startListening() throws MessageBusException{
		listener = new MessageQueueListener();
		listenToQueue(listener);
	}
	
	public MessageListener getListener(){
		return listener;
	}

	private void listenToQueue(MessageListener listener) throws MessageBusException {
		Queue queue = ServiceLocator.getInstance().getAppleMessageQueue();
		registerListenerForQueue(listener, queue);
	}
	
	public void restartListener(IMessageBusConsumer consumer) throws MessageBusException{
		listenToQueue(consumer.getListener()); 
	}
	
	public static void main(String[] args) throws MessageBusException   {
		new MessageConsumer().startListening();
	}
}
