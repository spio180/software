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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import common.Const;
import common.LoginStates;
import common.Message;
import common.Serialization;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TcpClient {
	private InetAddress connectedAddress;
	private int connectedPort = 0;
	private String userLogin = "";

	private Socket tcpSocket = null;
	private BufferedReader inBuff;
	private PrintWriter outPrint;
	private Thread listennigThread;
	private TcpListeningThread tcpListennigThread;
	private volatile boolean running = true;
	public HashMap<String, List<String>> listaListChatow = new HashMap<String, List<String>>();
	public List<String> listaUzytkownikow = new ArrayList<>();
	public ChatWindowController chatController = null;




	public TcpClient() {
		listaListChatow.put("Wszyscy", new ArrayList<>());
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}


	public static String getCurrentIPAddress() {
		InetAddress localaddr;
		try {
			localaddr = InetAddress.getLocalHost();
			System.out.println("Local Address : " + localaddr);
			System.out.println("Local IP: " + localaddr.getHostAddress());
			System.out.println("Local hostname   : " + localaddr.getHostName());
			return localaddr.getHostAddress();

		} catch (UnknownHostException e) {
			System.out.println("getCurrentIPAddress method - UnknownHostException occured");
			e.printStackTrace();
			return "";
		}
	}

	public int connectToServer(String host, int port) {
		try {
			connectedAddress = Inet4Address.getByName(host);
			connectedPort = port;
			this.tcpSocket = new Socket(connectedAddress, connectedPort);
			this.inBuff = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
			this.outPrint = new PrintWriter(tcpSocket.getOutputStream());


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

	public Message getMsgFromInBuff() {

		if (inBuff!=null)
			try {
				 if (inBuff.ready()) {
					String serializedMessage = inBuff.readLine();
					System.out.println("Received msg: " + serializedMessage);
					System.out.println("Received msg length: " + Integer.toString(serializedMessage.length()));
					if (serializedMessage.length() < 2)
						return null;
					else
						return Serialization.DeSerializeMessage(serializedMessage);
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Connection problem");
				return null;
			}
		return null;

	}

	public int startListennigThread() {

		tcpListennigThread = new TcpListeningThread();
		listennigThread = new Thread(tcpListennigThread);

		try {
			listennigThread.join();
		} catch (InterruptedException e) {
			System.out.println("Listenning thread could not be joined");
			e.printStackTrace();
			return -1;
		}
		listennigThread.start();
		running = true;
		return 0;
	}


	public void sendMessage(Message message) {
		synchronized (this) {
			if (!this.tcpSocket.isConnected()) {
				System.out.println("No connection to server");
				return;
			}

			String serializedMessage = Serialization.SerializeMessage(message);
			System.out.println(serializedMessage);
			this.outPrint.println(serializedMessage);
			this.outPrint.flush();
		}
	}

	public void terminateListenningThread() {
		running = false;
	}

	public void setChatController(ChatWindowController chat) {
		chatController = chat;

		if (chatController != null) {
			listaListChatow.get("Wszyscy").add("iKomunikator - WITAMY !");
			chatController.textChat.setItems(FXCollections.observableArrayList(listaListChatow.get("Wszyscy")));
		}
	}
	public void closeSocket() {
		if (tcpSocket != null)
			try {
				tcpSocket.close();
			} catch (IOException e) {
				System.out.println("TCP socket could not be closed");
				e.printStackTrace();
			}
	}

	private class TcpListeningThread implements Runnable {

		public TcpListeningThread() {

		}



		@Override
		public void run() {
			while (!running)
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					System.out.println("Listenning thread could not be started - running not set");
					e1.printStackTrace();
				}
			System.out.println("Listenning thread - Start signal received");

			while (running) {
				try {
					if (inBuff.ready()) {
						String serializedMessage = inBuff.readLine();
						System.out.println(serializedMessage);
						Message message = Serialization.DeSerializeMessage(serializedMessage);

						if (message.getType().equals(Const.MSG_LOGOWANIE_OK)) {
							this.LogowanieOK(message);
						}

						if (message.getType().equals(Const.MSG_DO_UZYTKOWNIKA)) {
							this.MessageDoUzytkownika(message);
						}


						if (message.getType().equals(Const.MSG_DO_WSZYSTKICH)) {
							this.MessageDoWszystkich(message);
						}

						if (message.getType().equals(Const.MSG_LOGOWANIE_BLAD)) {
							 Alert alert = new Alert(AlertType.ERROR);
							 alert.setTitle("B³¹d");
							 alert.setContentText("U¿ytkownik o tej nazwie ju¿ istnieje lub przekroczono dozwolon¹ liczbê u¿ytkowników.");
							 alert.showAndWait();
						}

						if (message.getType().equals(Const.MSG_LISTA_UZ)) {
							this.ListaUzytkownikow(message);
						}
					}

				} catch (IOException e) {
					System.err.println("Connection problem");
				}
			}

			System.out.println("Listenning thread stopped");
		}

		private void LogowanieOK(Message message) {
			this.ListaUzytkownikow(message);
			System.out.println(message.toString());
			//ChatWindowController.loggedUserName=message.getReceiver();
			String messageText = "Zalogowano na serwerze: "; //+ ChatWindowController.loggedUserName;
			listaListChatow.get("Wszyscy").add(messageText);

			if (chatController != null) {
				chatController.textChat.setItems(FXCollections.observableArrayList(listaListChatow.get("Wszyscy")));
			}
		}

		private void MessageDoUzytkownika(Message message) {
			if (message.getMessageBody().containsKey(Const.BODY)) {
				String messageText = message.getSender().concat(" - ").concat(message.getMessageBody().get(Const.BODY)).replace("\\n", "").replace("\n", "");

				System.out.println(messageText);
				listaListChatow.get(message.getSender()).add(messageText);

				if (chatController != null) {
					chatController.textChat.setItems(FXCollections.observableArrayList(listaListChatow.get(message.getSender())));
				}
			}
		}

		private void MessageDoWszystkich(Message message) {
			if (message.getMessageBody().containsKey(Const.BODY)) {
				String messageText = message.getSender().concat(" - ").concat(message.getMessageBody().get(Const.BODY)).replace("\\n", "").replace("\n", "");

				System.out.println(messageText);
				listaListChatow.get("Wszyscy").add(messageText);

				if (chatController != null) {
					chatController.textChat.setItems(FXCollections.observableArrayList(listaListChatow.get("Wszyscy")));
				}
			}
		}

		private void ListaUzytkownikow(Message message) {
			if (message.getMessageBody().size()>0) {
				if (chatController != null) {
					chatController.userList.getItems().clear();
					Map<String, String> sortedUsers = new TreeMap<String, String>(message.getMessageBody());

					for(String key : sortedUsers.keySet()){
						String userName = sortedUsers.get(key);
						chatController.userList.getItems().add(userName);
					}
				}
			}
		}
	}
}
