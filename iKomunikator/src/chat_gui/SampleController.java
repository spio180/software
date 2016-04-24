package chat_gui;

import java.net.UnknownHostException;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SampleController {
	@FXML private Button butCancel;
	@FXML private Button butConnect;
	@FXML private TextField textClientIP;

	private TcpClientConnection ConnectionToServer;

	@FXML private void closeButtonAction(){
	    // get a handle to the stage
	    Stage stage = (Stage) butCancel.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}
	 
	@FXML private void connectButtonAction() {
		ConnectionToServer = new TcpClientConnection();
		
		try {
			String ipAddress = ConnectionToServer.getCurrentIPAddress();
			textClientIP.setText(ipAddress);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		try {
			ConnectionToServer.Connect();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Message Here...");
	    alert.setHeaderText("Look, an Information Dialog");
	    alert.setContentText("I have a great message for you!");
	    alert.showAndWait();
	}
}
