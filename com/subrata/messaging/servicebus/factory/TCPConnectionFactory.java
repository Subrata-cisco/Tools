package com.subrata.messaging.servicebus.factory;

import javax.jms.ConnectionFactory;

import com.subrata.messaging.servicebus.contract.IMessageBusConnection;
import com.subrata.messaging.servicebus.core.MessageBusException;
import com.subrata.messaging.servicebus.core.ServiceLocator;

public class TCPConnectionFactory extends MessageBusConnectionFactory {

	private static ConnectionFactory cf = null;
	private static TCPConnectionFactory tcpFactory = null;
	
	static {
		try {
			tcpFactory = new TCPConnectionFactory();
		} catch (MessageBusException e) {
			System.out
					.println("[ERROR] : TCPConnectionFactory.enclosing_method()"+e.getMessage());
		}
	}
	
	
	private TCPConnectionFactory() throws MessageBusException {		
		populateExtranInfoAndCreateFactory();
	}
	
	public static TCPConnectionFactory getInstance() {
		return tcpFactory;
	}
	
	public ConnectionFactory getTCPConnectionFactory() throws MessageBusException {
		return cf;
	}
	
	
	public void createFactory() throws MessageBusException {
		try {
			cf = ServiceLocator.getInstance().lookup(
					IMessageBusConnection.JNDI_NAME_FOR_CONNECTION_FACTORY);
		} catch (Exception e) {
			e.printStackTrace();
			
		}	
	}
}
