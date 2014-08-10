package com.subrata.messaging.servicebus.contract;

import javax.jms.MessageListener;
import javax.jms.Queue;

import com.subrata.messaging.servicebus.core.MessageBusException;

/**
 * 
 * @author subratas
 *
 */
public interface IMessageBusConsumer {
	
	public void registerListenerForQueue(MessageListener listener,Queue queue)throws MessageBusException;
	public void restartListener(IMessageBusConsumer consumer)throws MessageBusException;
	public MessageListener getListener();
	
}
