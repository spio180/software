package chat_gui;


import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ChatWindowController {

	@FXML private Button butWyczysc;
	@FXML private Button butWyslij;
	@FXML private ListView<String> textChat;
	@FXML private TextArea textToSend;
	@FXML private ListView<String> userList;

	private TcpClient tcpConnectionToServer;
	protected List<String> listOfCom = new ArrayList<>();


	public void setTcpConnectionToServer(TcpClient connection){
		tcpConnectionToServer = connection;
	}

	@FXML private void butWyczyszClick(){
		textToSend.clear();

	}


	@FXML private void butWyslijClick(){

		tcpConnectionToServer.sendMessage(textToSend.getText());
		listOfCom.add(textToSend.getText());
		textChat.setItems(FXCollections.observableArrayList(listOfCom));
		textToSend.clear();
	}

}
