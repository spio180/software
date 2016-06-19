import java.io.BufferedReader;
//import java.io.Console;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 */

/**
 * @author admin
 *
 */
public class TCPServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting TCP Server ");

		int port = 10001;

		String clientSentence;
	    String capitalizedSentence;
	    int answer = 0;
	    //Console console = System.console();

	    ServerSocket welcomeSocket = null;

		try {
			welcomeSocket = new ServerSocket(port);
		} catch (IOException e1) {

			System.out.println("Server socket can not be created");
			e1.printStackTrace();
			System.exit(1);
		}

		InetAddress localaddr;
		try {
			localaddr = InetAddress.getLocalHost();
			System.out.println ("Local Address : " + localaddr );
			System.out.println("Local IP: " + localaddr.getHostAddress());
			System.out.println ("Local hostname   : " + localaddr.getHostName());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


	    while(answer != 1)
	    {
	        System.out.println("Waiting for connection from client ...");
	    	Socket connectionSocket;
			try {
				connectionSocket = welcomeSocket.accept();
				BufferedReader inFromClient =
			               new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

		        for (int i = 0; i < 2; i++)
		        {
		        	clientSentence = inFromClient.readLine();
		        	System.out.println("Received: " + clientSentence);
		        	capitalizedSentence = clientSentence + '\n';
		        	outToClient.writeBytes(capitalizedSentence);
		        }

			} catch (IOException e) {

				e.printStackTrace();
			}

	        System.out.println("Stop server? [1 - Yes/0 - No]");

	        //console = System.console();
	        answer = 0; //Integer.parseInt(console.readLine());


	     }

	    try {
			welcomeSocket.close();
		} catch (IOException e) {
			System.out.println("Server socket could not be closed");
			e.printStackTrace();
			System.exit(2);
		}

	}

}
