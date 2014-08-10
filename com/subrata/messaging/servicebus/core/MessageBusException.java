package com.subrata.messaging.servicebus.core;

public class MessageBusException extends Exception {

    private static final long serialVersionUID = -1986427301055082473L;
	private String message;
	private String errorCode;
	

	public MessageBusException(String msg) {
		this.message = msg;
	}

	public MessageBusException(String errorCode,String msg) {
		this.errorCode = errorCode;
		this.message = msg;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String toString() {
		return message;
	}

}
