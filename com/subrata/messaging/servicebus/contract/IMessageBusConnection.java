package com.subrata.messaging.servicebus.contract;

import javax.jms.Connection;
import javax.jms.JMSException;
/**
 * core connection api.
 * @author subratas
 *
 */
public interface IMessageBusConnection {
	
	public static final String DEFAULT_CONNECTION_TYPE = "tcp";
	public static final String JNDI_NAME_FOR_CONNECTION_FACTORY = "ConnectionFactory";
	public static final String JNDI_NAME_FOR_APPLE_PUSH_QUEUE = "messagestore";
	public static final String KEY_FOR_APPLE_PUSH_QUEUE = "applequeue";
		
	public Connection getConnection() throws  JMSException;
	public void startConnection() throws JMSException;
	public void stopConnection() throws JMSException;
	public void closeConnection() throws JMSException;
	public boolean isActive() throws JMSException;
}

