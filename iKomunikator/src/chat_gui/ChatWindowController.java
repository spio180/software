package chat_gui;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ChatWindowController {

	@FXML private Button butWyczysc;
	@FXML private Button butWyslij;
	@FXML private TextArea textChat;
	//@FXML private ListView userList;

	@FXML private void butWyczyszClick(){
		textChat.clear();

	}

}
