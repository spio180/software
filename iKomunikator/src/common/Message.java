package common;
import java.io.Serializable;
import java.util.HashMap;

public class Message implements Serializable {	
	private static final long serialVersionUID = 3053534860334056800L;
	private String type;
	private String sender;
	private String receiver;
	private HashMap<String,String> messageBody = new HashMap<String,String>();

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public HashMap<String,String> getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(HashMap<String,String> messageBody) {
		this.messageBody = messageBody;
	}
	
	public void addLineToMessageBody(String key, String value) {	
		this.messageBody.put(key,value);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Message() {
		this.messageBody = new HashMap<String,String>();
	}
	
	public Message(String type, String sender, String receiver, HashMap<String,String> messagebody){
		this.messageBody = new HashMap<String,String>();
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
		this.messageBody = messagebody;
	}
}
