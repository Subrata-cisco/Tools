package com.subrata.messaging.servicebus.contract;

import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

public interface IMessageBusQueueListener {	
	public void gotObjectMessage(ObjectMessage objMessage);
	public void gotTextMessage(TextMessage textMessage);
	public void gotMapMessage(MapMessage mapMessage);
}
