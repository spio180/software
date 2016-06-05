package common;
import java.util.ArrayList;

public class Message {	
	private String type;
	private String sender;
	private String receiver;
	private ArrayList<String> messageBody = new ArrayList<String>();
	
	public String GetType() {
		return this.type;
	}
	
	public void SetType(String type) {
		this.type = type;
	}
	
	public String GetSender() {
		return this.sender;
	}
	
	public void SetSender(String sender) {
		this.sender = sender;
	}
	
	public String GetReceiver() {
		return this.receiver;
	}
	
	public void SetReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	public ArrayList<String> GetMessageBody() {
		return this.messageBody;
	}
	
	public void GetMessageBody(ArrayList<String> messagebody) {
		this.messageBody = messagebody;
	}
	
	public Message(String type, String sender, String receiver, ArrayList<String> messagebody){
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
		this.messageBody = messagebody;
	}
}
