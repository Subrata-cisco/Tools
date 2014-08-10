package com.subrata.messaging.servicebus.core;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.subrata.messaging.servicebus.factory.TCPConnectionFactory;

public class MessageQueueListener extends QueueListener {
	
	ApnsService service = null;
	
	public MessageQueueListener() {
		service = createAppleNotificationService();	
	}
	
	private ApnsService createAppleNotificationService() {
		return APNS.newService()
	     .withCert(TCPConnectionFactory.getInstance().getCertPath(), TCPConnectionFactory.getInstance().getCertPassword())
	     .withProductionDestination()
	     .build();
	}
	
	private void pushNotification(Payload payload) {
		while(service == null){
			service = createAppleNotificationService();
		}
		 		 
		 String dataPayload = APNS.newPayload().badge(payload.getBadge()).alertBody(payload.getMessage()).actionKey("Play").build();
		 service.push(payload.getToken(), dataPayload);		
	}

	
	public void gotObjectMessage(ObjectMessage objMessage) {
		if(objMessage == null){
			printError("Message not found to send to Apple server.");
		}
		
		try {
			Payload payload = (Payload)objMessage.getObject();
			if (payload != null) {
				System.out.println("Message Received :: "
						+ payload.getMessage());
				
				pushNotification(payload);								
			}else{
				printError("Message not found to send to Apple server.");
			}
		} catch (JMSException e) {			
			printError("Message not found to send to Apple server."+e.getMessage());
		}
	}

	
	public void gotTextMessage(TextMessage textMessage) {
		// implement in inherited method.	
	}
	
	private void printError(String errorText) {
		System.out.println("ERROR :: "+errorText);
	}


}
