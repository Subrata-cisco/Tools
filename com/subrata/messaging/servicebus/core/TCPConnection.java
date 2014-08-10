package com.subrata.messaging.servicebus.core;

import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageListener;

import com.subrata.messaging.servicebus.contract.IMessageBusConnection;
import com.subrata.messaging.servicebus.factory.TCPConnectionFactory;

public class TCPConnection  implements IMessageBusConnection, ExceptionListener {

	private static Connection jmsConn = null;
	private boolean active = false;
	private static TCPConnection tcpConn = null;
	private ArrayList<MessageListener> consumerList = new ArrayList<MessageListener>();

	private TCPConnection() throws JMSException {
		this.jmsConn = createConnection();
	}

	/**
	 * Method to return a Single instance of the class
	 * 
	 * @return instance
	 */
	public static synchronized TCPConnection getInstance() {
		if (tcpConn == null) {
			try {
				tcpConn = new TCPConnection();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		return tcpConn;
	}

	@Override
	public void closeConnection() throws JMSException {
		// TODO Auto-generated method stub

	}

	
	private Connection createConnection() throws JMSException {
		boolean done = false;
		// get a connection from the MBConnectionFactory, and create a
		// connection to the message bus
		while (!done) {
			try {
				ConnectionFactory factory = TCPConnectionFactory.getInstance().getFactory();
				System.out.println("***************** username  ::"+TCPConnectionFactory.getInstance().getUserName());
				System.out.println("***************** password  ::"+TCPConnectionFactory.getInstance().getPassword());
				jmsConn = factory.createConnection(TCPConnectionFactory.getInstance().getUserName(),
								TCPConnectionFactory.getInstance().getPassword());
				jmsConn.setExceptionListener(this);
				active = true;
				done = true;
			} catch (JMSException e) {
                System.out.println("ERROR ::"+e.getMessage());
                System.exit(-1);
			} catch(Exception ex){
				ex.printStackTrace();
				System.exit(-1);
			}
			//System.out.println("Created a new MessageBus Connection from the MessageBusConnectionFactory");
		}
		return jmsConn;
	}
	
	@Override
	public Connection getConnection() throws JMSException {
		return jmsConn;
	}


	public Connection getMessageBusConnection() throws MessageBusException {
		if (jmsConn == null) {
			try {
				jmsConn = createConnection();
			} catch (JMSException e) {
				e.printStackTrace();
				System.out
						.println("ERROR :: While creating MessageBus Connection !");
				throw new MessageBusException(e.getMessage());
			}
		}
		return jmsConn;
	}

	public MessageBusSession createSession(boolean transacted, int acknowledgeMode)
			throws MessageBusException {
		while (!active) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
				}
			}
		}
		MessageBusSession mb_session = new MessageBusSession(jmsConn, transacted,
				acknowledgeMode);
		return mb_session;
	}

	public MessageBusSession createSession() throws MessageBusException {
		while (!active) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
				}
			}
		}
		MessageBusSession mb_session = new MessageBusSession(jmsConn, false,
				javax.jms.Session.AUTO_ACKNOWLEDGE);

		return mb_session;
	}

	@Override
	public boolean isActive() throws JMSException {
		return active;
	}

	@Override
	public void startConnection() throws JMSException {
		while (!active) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
				}
			}
		}
		try {
			jmsConn.start();
		} catch (JMSException e) {
			if (handleJMSException(e)) {
				startConnection();
			} else {
				// exception not due to node failure
				System.out
						.println("Exception thrown while starting TCPConnection"
								+ e);
				throw e;
			}
		}

	}

	@Override
	public void stopConnection() throws JMSException {
		while (!active) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
				}
			}
		}
		try {
			jmsConn.stop();
		} catch (JMSException e) {
			if (handleJMSException(e)) {
				startConnection();
			} else {
				// exception not due to node failure
				System.out
						.println("Exception thrown while stopping TCPConnection"
								+ e);
				throw e;
			}
		}
	}

	@Override
	public void onException(JMSException arg0) {
		// TODO Auto-generated method stub
		new FailoverConnectionThread().start();
	}

	synchronized boolean handleJMSException(JMSException e) {
		boolean isFailoverError = false;

		if (active) {
			this.active = false;
			// failoverConnection();
			FailoverConnectionThread fct = new FailoverConnectionThread();
			fct.start();
		}
		while (active == false) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e1) {
				}
			}
		}

		return isFailoverError;
	}

	private class FailoverConnectionThread extends Thread {

		public void run() {
			boolean done = false;
			if (!active) {
				while (!done) {
					try {
						createConnection();
						active = true;
						synchronized (TCPConnection.this) {
							TCPConnection.this.notifyAll();
						}
						// startConsumers(); TODO
						System.out
								.println("SUCCESS - QueueConnection failover complete!!");
						done = true;
					} catch (JMSException e) {
						try {
							Thread.sleep(5 * 1000);
						} catch (InterruptedException e1) {
						}

					}
				}
			}
		}
	}
	
	public void registerConsumer(MessageListener listener) {
		if(listener != null){
			consumerList.add(listener);
		}	
	}
	
	private void startConsumers(){
		for(MessageListener listener : consumerList){
			
		}
	}


}
