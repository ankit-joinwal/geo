package com.geogenie.geo.service.exception;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.NONE)
public class MessageDTO implements Serializable{

	public MessageDTO() {
		super();
	}
	private static final long serialVersionUID = 1L;
	@XmlElement
	private String message;
	@XmlElement
	private MessageType messageType;
	@XmlElement(name="errorDetails")
	private ServiceException serviceException;
	
	public MessageDTO(String message, MessageType messageType){
		this.message = message;
		this.messageType = messageType;
	}
	public MessageDTO(String message, MessageType messageType,ServiceException serviceException){
		this.message = message;
		this.messageType = messageType;
		this.serviceException = serviceException;
	}
	
	public enum MessageType{
		INFO, SUCCESS, WARNING, ERROR
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	public ServiceException getServiceException() {
		return serviceException;
	}
	public void setServiceException(ServiceException serviceException) {
		this.serviceException = serviceException;
	}
	
	
}
