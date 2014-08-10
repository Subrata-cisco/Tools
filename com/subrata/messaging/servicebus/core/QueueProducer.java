package com.subrata.messaging.servicebus.core;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;

import com.subrata.messaging.servicebus.contract.IMessageBusProducer;

public abstract class QueueProducer implements IMessageBusProducer {

	MessageBusSession session;
	Queue queue;
	
	public QueueProducer() throws MessageBusException {
		session =  TCPConnection.getInstance().createSession();
	}
	
	protected void deliverPayloadToQueue(Queue queue,Payload payload,int deliveryMode) 
				throws MessageBusException  {
		try {
			ObjectMessage message = session.getSession().createObjectMessage(payload);
			session.sendMessage(queue, message, deliveryMode);
			session.close();
		} catch (JMSException e) {
			throw new MessageBusException("[QueueProducer.deliverPayloadToQueue()]Error in Sending message , with description :: "+e.getMessage());
		}
	}
	
	
}
