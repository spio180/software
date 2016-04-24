package chat_gui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClientConnection {
	String ClientIP;
	int ClientPort;
	Socket ClientSocket;
	
	TcpClientConnection() {
		ClientIP = "localhost";
		ClientPort = 10001;
		ClientSocket = null;
		
	}
	
	public static String getCurrentIPAddress() throws UnknownHostException{
		
		InetAddress localaddr = InetAddress.getLocalHost();
		
		System.out.println ("Local Address : " + localaddr );
		System.out.println("Local IP: " + localaddr.getHostAddress());
		System.out.println ("Local hostname   : " + localaddr.getHostName());
		
		return localaddr.getHostAddress();
		
	}
	
	public void createClientSocket(String IP, int Port) throws IOException{
		
		if (IP == null) {
			ClientIP = getCurrentIPAddress();
		}
		else ClientIP = IP;
		
		System.out.println("Client IP: " + ClientIP);
		
		if (Port != ClientPort && Port != 0){
			ClientPort = Port;
		}
		System.out.println("Client Port: " + ClientPort);
		
		ClientSocket = new Socket(ClientIP, ClientPort);
	}
	
	
	public void Connect(String clientLogin) {
		
		String message = "Hello Server - Client \n" + clientLogin;
		System.out.println(message);
		
		if (ClientSocket != null) {
		
			DataOutputStream outToServer;
			try {
				outToServer = new DataOutputStream(ClientSocket.getOutputStream());
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
				outToServer.writeBytes(message);
		
				String serverAnswear = inFromServer.readLine();
				System.out.println("FROM SERVER: " + serverAnswear);
				ClientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
