package chat_gui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
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
		
		/*
		String sentence;
		String modifiedSentence;
		BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
		Socket clientSocket = new Socket("localhost", 6789);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		sentence = inFromUser.readLine();
		outToServer.writeBytes(sentence + '\n');
		modifiedSentence = inFromServer.readLine();
		System.out.println("FROM SERVER: " + modifiedSentence);
		clientSocket.close();
		*/
		return 0;
		
	}
	
}
