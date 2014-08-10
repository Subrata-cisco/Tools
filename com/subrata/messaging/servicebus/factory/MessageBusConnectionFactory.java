package com.subrata.messaging.servicebus.factory;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import com.subrata.messaging.servicebus.contract.IMessageBusConnection;
import com.subrata.messaging.servicebus.core.MessageBusException;

public abstract class MessageBusConnectionFactory {
	
	protected String connectionType = null;
	protected String userName = null;
	protected String password = null;
	protected String certPath = null;
	protected String certPassword = null;
	protected ConnectionFactory factory = null;


	/**
	 * This method try to find the property file from the Class path
	 * 
	 * @param fileName
	 * @return filepath
	 */
	private String locateFile(String fileName) {
		String file = null;
		URL fileURL = ClassLoader.getSystemResource(fileName);
		if (fileURL != null) {
			file = fileURL.getFile();
		} else
			System.err.println("Unable to locate file " + fileName);
		return file;
	}

	/**
	 * This method populate the key/value from the property file
	 * 
	 * @param file
	 * @return property object
	 */
	private Properties loadPropertiesFile(String file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			if (fis != null) {
				Properties props = new Properties();
				props.load(fis);
				return props;
			}
			System.err.println("Cannot load " + file
					+ " , file input stream object is null");
		} catch (java.io.IOException ioe) {
			System.err.println("Cannot load " + file + ", error reading file");
		}
		return null;
	}
	
	/**
	 * This method returns the factory of different type
	 * default is http
	 * @return
	 * @throws JMSException
	 */
	protected void populateExtranInfoAndCreateFactory() throws MessageBusException {
		String filePath = null;

		filePath = locateFile("MessageBus.properties");
		if (filePath != null) {
			File propertyFile = new File(filePath);
			if (propertyFile.exists()) {
				Properties mbProperty = loadPropertiesFile(filePath);
				connectionType = mbProperty.getProperty("connectionType");
				// If not found in the property file then assume Queue
				if (null == connectionType) {
					connectionType = IMessageBusConnection.DEFAULT_CONNECTION_TYPE;
				}
				
				userName = mbProperty.getProperty("userName");
				password = mbProperty.getProperty("password");
				certPath = mbProperty.getProperty("certPath");
				certPassword = mbProperty.getProperty("certPassword");

				if(certPath == null || certPassword == null){
					throw new MessageBusException("ERROR :: certPath or certPassword is missing from MessageBus.properties");
				}
				
			} else {
				throw new MessageBusException("ERROR :: MessageBus.properties is missing from Classapth");
			}
		} else {
			throw new MessageBusException("ERROR :: MessageBus.properties is missing from Classapth");
		}
		createFactory();
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getPassword() {
		return password;
	}

	public String getCertPath() {
		return certPath;
	}

	public void setCertPath(String certPath) {
		this.certPath = certPath;
	}

	public String getCertPassword() {
		return certPassword;
	}

	public void setCertPassword(String certPassword) {
		this.certPassword = certPassword;
	}

	public ConnectionFactory getFactory() throws MessageBusException {
		if (IMessageBusConnection.DEFAULT_CONNECTION_TYPE == connectionType) {
			factory = TCPConnectionFactory.getInstance().getTCPConnectionFactory();
		}
		return factory;
	}
	
	public abstract void createFactory() throws MessageBusException;

}
