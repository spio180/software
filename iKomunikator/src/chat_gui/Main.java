package chat_gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("LogWindow.fxml"));
			Scene scene = new Scene(root,280,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Logowanie");
			TextField ipTextField = (TextField) scene.lookup("#userIPTest");
			ipTextField.setText(TcpClient.getCurrentIPAddress());
			ipTextField.requestFocus();
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
