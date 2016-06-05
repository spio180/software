package chat_gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TcpClient {

    private InetAddress connectedAddress;
    private int connectedPort;
    private Socket tcpSocket;
    private BufferedReader inBuff;
    private PrintWriter outPrint;

    private Thread listennigThread;
    private volatile boolean running = true;

    public TcpClient(){

    }

    public static String getCurrentIPAddress() {

		InetAddress localaddr;
		try {
			localaddr = InetAddress.getLocalHost();
			System.out.println ("Local Address : " + localaddr );
			System.out.println("Local IP: " + localaddr.getHostAddress());
			System.out.println ("Local hostname   : " + localaddr.getHostName());

			return localaddr.getHostAddress();

		} catch (UnknownHostException e) {
			System.out.println("getCurrentIPAddress method - UnknownHostException occured");
			e.printStackTrace();
			return "";
		}

	}

    public int connectToServer(String host, int port){
        try {
            connectedAddress = Inet4Address.getByName(host);
            connectedPort = port;
            this.tcpSocket = new Socket(connectedAddress, connectedPort);
            this.inBuff = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            this.outPrint = new PrintWriter(tcpSocket.getOutputStream());

            /* Starting listenning thread */
            listennigThread = new Thread(new TcpListeningThread());
            try {
				listennigThread.join();
			} catch (InterruptedException e) {
				System.out.println("Listenning thread could not be joined");
				e.printStackTrace();
			}
            listennigThread.start();
            running = true;

        } catch (SocketException e) {
            System.out.println("Socket Exception occured");
            e.printStackTrace();
            return 1;
        } catch (UnknownHostException e) {
            System.out.println("UnknownHostException occured");
        	e.printStackTrace();
        	return 2;
        } catch (IOException e) {
            System.out.println("IOException occured");
        	e.printStackTrace();
        	return 3;
        }

        return 0;
    }

    public void sendMessage(String message){
        synchronized(this) {
            if(!this.tcpSocket.isConnected()) {
                System.out.println("No connection to server");
            	return;
            }
            this.outPrint.println(message);
            this.outPrint.flush();
        }
    }

    public void terminateListenningThread(){
    	running = false;

    }

    private class TcpListeningThread implements Runnable{

        public TcpListeningThread() {
            // Nothing to do...
        }

        @Override
        public void run() {
            /*waiting for thread start signal */
        	while (!running)
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					System.out.println("Listenning thread could not be started - running not set");
					e1.printStackTrace();
				}
        	System.out.println("Start signal received");

        	try {
                while(running){
                    if (inBuff.ready()) {
                    	String newLine = inBuff.readLine();
                    	System.out.println(newLine); // Replace this with any processing you want !
                    }
                }
            } catch (IOException e) {
                System.err.println("Connection problem");
            }

        	System.out.println("Listenning thread stopped");
        }
    }
}