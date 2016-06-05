package chat_gui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class LogWindowController {
	@FXML private Button butCancel;
	@FXML private Button butConnect;
	@FXML private TextField textClientIP;
	@FXML TextField userLogin;
	@FXML Pane tcpClientPort;
	@FXML TextField textClientPort;
	private TcpClient connectionToServer;

	@FXML private void closeButtonAction(){

		// get a handle to the stage
	    Stage stage = (Stage) butCancel.getScene().getWindow();

	    // do what you have to do
	    stage.close();
	}

	@FXML private void connectButtonAction() {

		connectionToServer = new TcpClient();

		/*connection establishing */
		int rc = connectionToServer.connectToServer(textClientIP.getText(),
					Integer.parseInt(textClientPort.getText()));

		if (rc != 0) return;

		/*sending logging message */
		connectionToServer.sendMessage(userLogin.getText());

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



		    // do what you have to do
		    stage.close();
		}

	}
}
