package chat_gui;
import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import common.Const;
import common.Message;
import common.LoginStates;;

public class LogWindowController {
	@FXML private Button butCancel;
	@FXML private Button butConnect;
	@FXML private TextField textClientIP;
	@FXML private TextField textUserLogin;
	@FXML private Pane tcpClientPort;
	@FXML private TextField textClientPort;
	private TcpClient connectionToServer = null;

	@FXML private void closeButtonAction(){
		if (connectionToServer != null) {
			connectionToServer.terminateListenningThread();
			connectionToServer.closeSocket();
			System.out.println("Connection to server closed");
		}

		Stage stage = (Stage) butCancel.getScene().getWindow();
	    stage.close();
	}

	@FXML private void OnKeyPressedEnter(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			connectButtonAction();
		}

	}

	@FXML private void connectButtonAction() {

		if (!this.PolaWypelnionePoprawnie()) {
			return;
		}

		butConnect.setDisable(true);

		/*creating logging thread */
		LoginThread threadToLogin = null;
		threadToLogin = new LoginThread(textClientIP.getText(),
				Integer.parseInt(textClientPort.getText()),
				this.textUserLogin.getText()
				);

		threadToLogin.start();

		threadToLogin.getLoginStateProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(final ObservableValue<? extends Number> observable,
		          final Number oldValue, final Number newValue) {

		          Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
	        			// get a handle to the stage
	        		    Stage loginStage = (Stage) butConnect.getScene().getWindow();
		            	int state = newValue.intValue();

		            	if (state == LoginStates.ACK_ACCEPT) {
		            		System.out.println("loginStateProperty changed to ACK_ACCEPT");

		        			//Starting chat window
		        			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatWindow.fxml"));
		        			AnchorPane chatWindow;

		        			try {

		        				chatWindow = (AnchorPane) loader.load();
		        				Stage stageChat = new Stage();
		        		        stageChat.setTitle("iKomunikator - zalogowano na serwerze jako: " + textUserLogin.getText());
		        		        Scene sceneChat = new Scene(chatWindow, 900, 600);
		        		        stageChat.setScene(sceneChat);

		        		        //setting TCP connection for ChatWindow
			        			ChatWindowController chatController = (ChatWindowController) loader.getController();
			        			chatController.setLoginScene(loginStage);
			        			chatController.setTcpConnectionToServer(connectionToServer);
			        			chatController.setLoggedUserName(textUserLogin.getText());
			        			chatController.startListenningThread2();

			        			connectionToServer.setChatController(chatController);
			        			connectionToServer.setUserLogin(textUserLogin.getText());
			        			//connectionToServer.startListennigThread();

		        		        stageChat.setOnCloseRequest(new EventHandler<WindowEvent>() {
		        		            public void handle(WindowEvent we) {
		        		                System.out.println("Stage is closing");

		        		                if (connectionToServer != null) {
		        		                	System.out.println("Sending logout message to Server");
		        		                	Message logoutMessage = new Message();
			        		    			logoutMessage.setType(Const.MSG_WYLOGOWANIE);
			        		    			logoutMessage.setReceiver(Const.USER_SERVER);
			        		    			logoutMessage.setSender(chatController.getLoggedUserName());
			        		    	        logoutMessage.addLineToMessageBody(Const.LOGOUT, chatController.getLoggedUserName());
			        		    	        connectionToServer.sendMessage(logoutMessage);
		        		        			chatController.terminateListenningThread();
		        		        			connectionToServer.closeSocket();
		        		        			System.out.println("Connection to server closed");
		        		        		}
		        		            }
		        		        });

		        		        stageChat.show();
		        				TextField textToSend = (TextField) sceneChat.lookup("#textToSend");
		        				textToSend.requestFocus();

		        			} catch (IOException e) {
		        				// TODO Auto-generated catch block
		        				System.out.println("Chat Window could not be created" );
		        				e.printStackTrace();
		        				return;
		        			}


		        			butConnect.setDisable(false);
		        		    // do what you have to do
		        			loginStage.hide();

		            	} else {
		            		System.out.println("loginStateProperty changed to different value then ACK_ACCEPT");

		            		String error_msg = "Nieznany b³¹d w trakcie logowania na serwer";

		            	 	if (state ==  LoginStates.NOT_CONNECTED) error_msg = "Nie uzyskano po³¹czenia z serwerem";
		            		if (state ==  LoginStates.CONNECTION_ERROR) error_msg = "Nie uzyskano po³¹czenia z serwerem";
		            		if (state ==  LoginStates.ACK_REJECT) error_msg = "U¿ytkownik o tej nazwie ju¿ istnieje lub przekroczono dozwolon¹ liczbê u¿ytkowników.";
		            		if (state ==  LoginStates.TIMEOUT) error_msg = "Timeout w oczekiwaniu na potwierdzenie logowania";

		            		Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle(Const.ERROR_HEADER);
							alert.setContentText(error_msg);
							alert.showAndWait();

		            		butConnect.setDisable(false);
		            	}
		            }
		          });
		      }
		    });

	}

	private boolean PolaWypelnionePoprawnie() {
		boolean result = true;
		String validIpMask = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

		if (result && this.textClientIP.getLength()==0) {
			result = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Const.ERROR_HEADER);
			alert.setContentText("Podaj adres serwera !");
			alert.showAndWait();
			this.textClientIP.requestFocus();
		}

		if (result && !this.textClientIP.getText().matches(validIpMask)) {
			result = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Const.ERROR_HEADER);
			alert.setContentText("Adres IP serwera jest niepoprawny !\nPoprawny adres to: [0-255].[0-255].[0-255].[0-255] !");
			alert.showAndWait();
			this.textClientIP.requestFocus();
		}

		if (result && this.textClientPort.getLength()==0) {
			result = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Const.ERROR_HEADER);
			alert.setContentText("Podaj numer portu serwera [0-65535] !");
			alert.showAndWait();
			this.textClientPort.requestFocus();
		}

		if (result && !this.textClientPort.getText().matches("[0-9]+")) {
			result = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Const.ERROR_HEADER);
			alert.setContentText("Niepoprawny numer portu !\nWpisz wartoœæ z przedzia³u 0-65535 !");
			alert.showAndWait();
			this.textClientPort.requestFocus();
		}

		if (result && this.textClientPort.getLength()>5) {
			result = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Const.ERROR_HEADER);
			alert.setContentText("Niepoprawny numer portu !\nWpisz wartoœæ z przedzia³u 0-65535 !");
			alert.showAndWait();
			this.textClientPort.requestFocus();
		}

		if (result && (Integer.parseInt(this.textClientPort.getText())<0 || Integer.parseInt(this.textClientPort.getText())>65535)) {
			result = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Const.ERROR_HEADER);
			alert.setContentText("Niepoprawny numer portu !\nWpisz wartoœæ z przedzia³u 0-65535 !");
			alert.showAndWait();
			this.textClientPort.requestFocus();
		}

		if (result && this.textUserLogin.getLength()==0) {
			result = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Const.ERROR_HEADER);
			alert.setContentText("Podaj nazwê u¿ytkownika !");
			alert.showAndWait();
			this.textUserLogin.requestFocus();
		}

		if (result && this.textUserLogin.getLength()>40) {
			result = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Const.ERROR_HEADER);
			alert.setContentText("Nazwa u¿ytkownika mo¿e zawieraæ maksymalnie 40 znaków !");
			alert.showAndWait();
			this.textUserLogin.requestFocus();
		}

		if (result && !this.textUserLogin.getText().matches("[_a-zA-Z0-9]+")) {
			result = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Const.ERROR_HEADER);
			alert.setContentText("Niepoprawna nazwa u¿ytkownika !\nDozwolone znaki to: litery, cyfry oraz znak podkreœlenia [_].");
			alert.showAndWait();
			this.textUserLogin.requestFocus();
		}

		return result;
	}

	public class LoginThread extends Thread {

		private IntegerProperty loginStateProperty;
		private String serverIP;
		private int serverPort;
		private String userLoginName;

		public LoginThread(String ip, int port, String user) {
		      loginStateProperty = new SimpleIntegerProperty(this, "int", LoginStates.NOT_CONNECTED);
		      serverIP = ip;
		      serverPort = port;
		      userLoginName = user;
		      setDaemon(true);
		}

		public int getLoginStateInt() {
		      return loginStateProperty.get();
		}

		public IntegerProperty getLoginStateProperty() {
		      return loginStateProperty;
		}

	 	@Override
	    public void run() {

	    	System.out.println(">>>>LoginThread: started");
	    	System.out.println(">>>>Uzytkownik: " + userLoginName);

	    	connectionToServer = new TcpClient();

	    	int rc = connectionToServer.connectToServer(serverIP, serverPort);

	    	if (rc != 0) {
	    		loginStateProperty.set(LoginStates.CONNECTION_ERROR);
	    		System.out.println("LoginThread: Connection to server NOT established");
	    		return;
	    	}

	    	//sending logging message
	    	Message loginMessage = new Message();
			loginMessage.setType(Const.MSG_LOGOWANIE);
			loginMessage.setReceiver(Const.USER_SERVER);
			loginMessage.setSender(textUserLogin.getText());
	        loginMessage.addLineToMessageBody(Const.LOGIN, textUserLogin.getText());

	        connectionToServer.sendMessage(loginMessage);

	        Message loginMessageAns = new Message();
	        int i;
	        for (i = 0; i < 500; i++){
	        	loginMessageAns = connectionToServer.getMsgFromInBuff();

	        	if (loginMessageAns != null) {
		        	if (loginMessageAns.getType().equals(Const.MSG_LOGOWANIE_OK)){
		        		System.out.println("LoginThread: Connection to server established");
		        		loginStateProperty.set(LoginStates.ACK_ACCEPT);
		        		break;
		        	}
		        	if (loginMessageAns.getType().equals(Const.MSG_LOGOWANIE_BLAD)){
		        		System.out.println("LoginThread: Rejected by server");
		        		loginStateProperty.set(LoginStates.ACK_REJECT);
		        		break;
		        	}
	        	}

	        	try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }

	        if (i >= 500) {
	        	System.out.println("LoginThread: Timeout during waiting for LOGIN_OK");
	        	loginStateProperty.set(LoginStates.TIMEOUT);
	        }

	    	System.out.println(">>>>LoginThread: closed");
	    }
	  }
}
