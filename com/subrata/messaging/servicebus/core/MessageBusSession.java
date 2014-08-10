package com.subrata.messaging.servicebus.core;

import java.util.Vector;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class MessageBusSession {

	private Connection conn;
	private Session jmsSess;

	private boolean transacted;
	private int acknowledgeMode;
	Vector consumerList = new Vector(10, 10);

	public static final int TEXT = 0;
	public static final int OBJECT = 1;
	public static final int MAP = 2;
	public static final int BYTES = 3;
	public static final int STREAM = 4;

	MessageBusSession(Connection conn, boolean transacted, int acknowledgeMode)
			throws MessageBusException {
		boolean done = false;
		this.conn = conn;

		while (!done) {
			try {
				// create a session from the Connection
				jmsSess = (Session) conn.createSession(transacted,
						acknowledgeMode);
				this.transacted = transacted;
				this.acknowledgeMode = acknowledgeMode;
				done = true;
			} catch (JMSException e) {
				throw new MessageBusException("ERROR :: in creation sesssion " +
						"object with description :"+e.getMessage());
			}
		}
	}
	
	public void close() throws MessageBusException {
		boolean done = false;
		while (!done) {
			try {
				if(jmsSess != null)
					jmsSess.close();
				this.conn = null;
				done = true;
			} catch (JMSException e) {
				throw new MessageBusException("ERROR :: in closing sesssion " +
						"object with description :"+e.getMessage());
			}
		}
	}


	MessageBusSession(Connection conn) throws MessageBusException {
		boolean done = false;
		this.conn = conn;
		while (!done) {
			try {
				// create a session from the Connection
				jmsSess = (Session) new MessageBusSession(conn, false,
						javax.jms.Session.AUTO_ACKNOWLEDGE);
				done = true;
			} catch (MessageBusException e) {
				throw new MessageBusException("ERROR :: in creation sesssion " +
						"object with description :"+e.getMessage());
			}
		}
	}

	public Session getSession() {
		return jmsSess;
	}

	public void sendMessage(javax.jms.Destination destination,
			javax.jms.Message message, int deliveryMode)
			throws javax.jms.JMSException {
		
		try {
			MessageProducer producer = jmsSess.createProducer(destination);
			System.out.println("***************** Sending message...");
			producer.send(message, deliveryMode, 9, 0);
			producer.close();
		} catch (JMSException e) {
			// handleexception 
		}
	}

}
