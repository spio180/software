package chat_gui;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class SampleController {
	@FXML Button butCancel;
	@FXML Button butConnect;

	private TcpClientConnection ConnectionToServer;

	@FXML private void closeButtonAction(){
	    // get a handle to the stage
	    Stage stage = (Stage) butCancel.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}

	@FXML private void connectButtonAction() {
		ConnectionToServer = new TcpClientConnection();
		ConnectionToServer.Connect();
		
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Message Here...");
	    alert.setHeaderText("Look, an Information Dialog");
	    alert.setContentText("I have a great message for you!");
	    alert.showAndWait();
	}
}
