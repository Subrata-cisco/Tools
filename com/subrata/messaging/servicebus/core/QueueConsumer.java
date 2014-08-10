package com.subrata.messaging.servicebus.core;

import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;

import com.subrata.messaging.servicebus.contract.IMessageBusConsumer;

public class QueueConsumer implements IMessageBusConsumer {	

	MessageBusSession session;
	Queue queue;
	
	public QueueConsumer() throws MessageBusException {
		session =  TCPConnection.getInstance().createSession();
	}

	@Override
	public void registerListenerForQueue(MessageListener listener,Queue queue) throws MessageBusException { 
		try {
			TCPConnection.getInstance().startConnection();
			MessageConsumer messageConsumer = session.getSession().createConsumer(queue);
			messageConsumer.setMessageListener(listener);
		} catch (Exception e) {
			throw new MessageBusException("ERROR :: in registering Queue listener with description :"+e.getMessage());
		}
	}

	@Override
	public MessageListener getListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restartListener(IMessageBusConsumer consumer) throws MessageBusException{
		// TODO Auto-generated method stub
	}

}
