package chat_gui;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TcpClientConnection {
	TcpClientConnection() {
		
	}
	
	public String getCurrentIPAddress() throws UnknownHostException{
		
		InetAddress localaddr = InetAddress.getLocalHost();
		
		System.out.println ("Local Address : " + localaddr );
		System.out.println("Local IP: " + localaddr.getHostAddress());
		System.out.println ("Local hostname   : " + localaddr.getHostName());
		
		return localaddr.getHostAddress();
		
	}
	
	public int Connect() throws UnknownHostException{
		return 0;
		
	}
	
}
