package chat_gui;

import java.io.IOException;
import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import common.Message;
import common.Serialization;

public class LogWindowController {
	@FXML private Button butCancel;
	@FXML private Button butConnect;
	@FXML private TextField textClientIP;
	@FXML TextField userLogin;
	@FXML Pane tcpClientPort;
	@FXML TextField textClientPort;
	private TcpClient connectionToServer = null;

	@FXML private void closeButtonAction(){

		/* Closing connection */
		if (connectionToServer != null) {
			connectionToServer.terminateListenningThread();
			connectionToServer.closeSocket();
			System.out.println("Connection to server closed");
		}
		// get a handle to the stage
	    Stage stage = (Stage) butCancel.getScene().getWindow();

	    // do what you have to do
	    stage.close();
	}

	@FXML private void connectButtonAction() {

		connectionToServer = new TcpClient();
		butConnect.setDisable(true);


		/*connection establishing */
		int rc = connectionToServer.connectToServer(textClientIP.getText(),
					Integer.parseInt(textClientPort.getText()));


		if (rc != 0) {
			System.out.println("Cbutton");
			butConnect.setDisable(true);
			return;
		}


		/*sending logging message */
		HashMap<String, String> loginPayload = new HashMap<String, String>();
		loginPayload.put("LOGIN", userLogin.getText());
		Message loginMessage = new Message("LOGIN_REQ", userLogin.getText(),
				"##", loginPayload);
		System.out.println(Serialization.SerializeMessage(loginMessage));
		connectionToServer.sendMessage(Serialization.SerializeMessage(loginMessage));

		/*verify answear from server */

		if (true) { /*check nee to be implemented*/
			//Starting chat window
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatWindow.fxml"));
			BorderPane chatWindow;

			try {

				chatWindow = (BorderPane) loader.load();
				Stage stageChat = new Stage();
		        stageChat.setTitle("iKomunikator");
		        stageChat.setScene(new Scene(chatWindow, 480, 820));
		        stageChat.show();

		        stageChat.setOnCloseRequest(new EventHandler<WindowEvent>() {
		            public void handle(WindowEvent we) {
		                System.out.println("Stage is closing");
		                if (connectionToServer != null) {
		        			connectionToServer.terminateListenningThread();
		        			connectionToServer.closeSocket();
		        			System.out.println("Connection to server closed");
		        		}
		            }
		        });


			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Chat Window could not be created");
				e.printStackTrace();
				return;
			}

			//setting TCP connection for ChatWindow
			ChatWindowController chatController = (ChatWindowController) loader.getController();
			chatController.setTcpConnectionToServer(connectionToServer);

			// get a handle to the stage
		    Stage stage = (Stage) butConnect.getScene().getWindow();

		    butConnect.setDisable(true);
		    // do what you have to do
		    stage.hide();
		}

	}
}
