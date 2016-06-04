package chat_gui;

import java.io.IOException;
import java.net.UnknownHostException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
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
	private TcpClientConnection ConnectionToServer;

	@FXML private void closeButtonAction(){
	    /////////TEST ///////////
		BorderPane chatWindow;
		try {
			chatWindow = (BorderPane)FXMLLoader.load(getClass().getResource("ChatWindow.fxml"));
			Stage stageChat = new Stage();
	        stageChat.setTitle("iKomunikator");
	        stageChat.setScene(new Scene(chatWindow, 480, 550));
	        stageChat.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Chat Window could not be created");
			e.printStackTrace();
		}



		///////////////////////


		// get a handle to the stage
	    Stage stage = (Stage) butCancel.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	    //stage.hide();
	}

	@FXML private void connectButtonAction() {
		ConnectionToServer = new TcpClientConnection();

		try {
			ConnectionToServer.createClientSocket(textClientIP.getText(),
					Integer.parseInt(textClientPort.getText()));
			ConnectionToServer.Connect(userLogin.getText());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in sending message to Server");
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();

			System.out.println("Error during creating of Socket");
		}


	}
}
