package com.subrata.messaging.servicebus.core;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import com.subrata.messaging.servicebus.contract.IMessageBusQueueListener;

public class QueueListener implements MessageListener,IMessageBusQueueListener {
		
	public QueueListener() {
		super();
	}

	@Override
	public void onMessage(Message msg) {		
		if (msg instanceof ObjectMessage) {
			ObjectMessage mesg = (ObjectMessage) msg;
			gotObjectMessage(mesg);
		} else if (msg instanceof TextMessage) {
			TextMessage mesg = (TextMessage) msg;
			gotTextMessage(mesg);
		} else if (msg instanceof MapMessage){
			MapMessage mesg = (MapMessage) msg;
			gotMapMessage(mesg);
		}
	}

	@Override
	public void gotObjectMessage(ObjectMessage objMessage) {
		// implement in inherited method.
	}

	@Override
	public void gotTextMessage(TextMessage textMessage) {
		// implement in inherited method.	
	}

	@Override
	public void gotMapMessage(MapMessage mapMessage) {
		// TODO Auto-generated method stub
		
	}
	
	

}
