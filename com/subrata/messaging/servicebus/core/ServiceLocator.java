package com.subrata.messaging.servicebus.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.subrata.messaging.servicebus.contract.IMessageBusConnection;

public class ServiceLocator {

	private static ServiceLocator locator;
	private Context ic;
	private Map<String, Object> cache;

	static {
		try {
			locator = new ServiceLocator();
		} catch (Exception se) {
			System.err.println(se);
			se.printStackTrace(System.err);
		}
	}

	private ServiceLocator() throws Exception {
		super();
		try {
			ic = getInitialContext();
			cache = Collections.synchronizedMap(new HashMap<String, Object>());
		} catch (NamingException ne) {
			throw new Exception(ne);
		}
	}

	static public ServiceLocator getInstance() {
		return locator;
	}

	public InitialContext getInitialContext() throws Exception {
		InitialContext ctx = null;
		try {			
			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY,MessageBusConstants.AQ_CONNECTION_FACTORY);
			props.setProperty(Context.PROVIDER_URL,MessageBusConstants.SERVER_PORT);			
			ctx = new InitialContext(props);

		} catch (Exception ex) {
			System.out
					.println("[ERROR] :: ServiceLocator.getInitialContext()");
		}
		return ctx;
	}

	public ConnectionFactory lookup(String connType)
			throws Exception {
		ConnectionFactory conn = null;
		try {
			if (cache.containsKey(connType)) {
				conn = (ConnectionFactory) cache.get(connType);
			} else {
				Object objref = ic.lookup(connType);
				conn = (ConnectionFactory) objref;
				cache.put(connType, conn);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return conn;
	}

	public Queue getAppleMessageQueue() throws MessageBusException {
		Queue queue = null;		
		try {
			if (cache.containsKey(IMessageBusConnection.KEY_FOR_APPLE_PUSH_QUEUE)) {
				queue = (Queue) cache.get(IMessageBusConnection.KEY_FOR_APPLE_PUSH_QUEUE);
			} else {
				Object objref = ic.lookup(IMessageBusConnection.JNDI_NAME_FOR_APPLE_PUSH_QUEUE);  
				queue = (Queue) objref;
				cache.put(IMessageBusConnection.KEY_FOR_APPLE_PUSH_QUEUE,
						queue);
			}
		} catch (Exception e) {
			throw new MessageBusException("Look up failed for Queue messagestore in ActiveMQ server."+e.getMessage());
		}
		return queue;
	}

}
