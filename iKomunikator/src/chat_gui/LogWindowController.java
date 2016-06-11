package chat_gui;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import common.Const;
import common.Message;

public class LogWindowController {
	@FXML private Button butCancel;
	@FXML private Button butConnect;
	@FXML private TextField textClientIP;
	@FXML TextField userLogin;
	@FXML Pane tcpClientPort;
	@FXML TextField textClientPort;
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

	@FXML private void connectButtonAction() {
		connectionToServer = new TcpClient();
		butConnect.setDisable(true);

		/*connection establishing */
		int rc = connectionToServer.connectToServer(textClientIP.getText(),Integer.parseInt(textClientPort.getText()));

		if (rc != 0) {
			System.out.println("Cbutton");
			butConnect.setDisable(true);
			return;
		}
		
		Message loginMessage = new Message();
		loginMessage.setType(Const.MSG_LOGOWANIE);
		loginMessage.setReceiver(Const.USER_SERVER);
		loginMessage.setSender(userLogin.getText());
        loginMessage.addLineToMessageBody(Const.LOGIN, userLogin.getText());		
        connectionToServer.sendMessage(loginMessage);
		
		/*verify answear from server */

		if (true) { 
			//Starting chat window
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatWindow.fxml"));
			AnchorPane chatWindow;

			try {
				chatWindow = (AnchorPane) loader.load();
				Stage stageChat = new Stage();
		        stageChat.setTitle("iKomunikator");
		        stageChat.setScene(new Scene(chatWindow, 900, 600));
		        //TextField textToSend = (TextField) stageChat.lookup("textToSend");
		        //textToSend.requestFocus();
		        //Button butWyslij = (Button) chatWindow.lookup("butWyslij");
		        //butWyslij.setDefaultButton(true);
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
				System.out.println("Chat Window could not be created");
				e.printStackTrace();
				return;
			}

			//setting TCP connection for ChatWindow
			ChatWindowController chatController = (ChatWindowController) loader.getController();
			chatController.setTcpConnectionToServer(connectionToServer);
			connectionToServer.setChatController(chatController);

			// get a handle to the stage
		    Stage stage = (Stage) butConnect.getScene().getWindow();

		    butConnect.setDisable(true);
		    // do what you have to do
		    stage.hide();
		}

	}
}
