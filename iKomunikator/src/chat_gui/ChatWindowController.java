package chat_gui;


import java.util.ArrayList;
import java.util.List;

import common.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;



public class ChatWindowController {

	@FXML private Button butWyczysc;
	@FXML public Button butWyslij;
	@FXML public ListView<String> textChat;
	@FXML private TextArea textToSend;
	@FXML private ListView<String> userList;

	private TcpClient tcpConnectionToServer;
	//protected List<String> listOfCom = new ArrayList<>();


	public void setTcpConnectionToServer(TcpClient connection){
		tcpConnectionToServer = connection;
	}

	@FXML private void butWyczyszClick(){
		textToSend.clear();

	}


	@FXML private void butWyslijClick(){

		String msg = textToSend.getText();

		if (msg.length() != 0)
		{
			tcpConnectionToServer.sendMessage(textToSend.getText());
			tcpConnectionToServer.listOfCom.add(textToSend.getText());

			System.out.println(tcpConnectionToServer.listOfCom.size());

			textChat.setItems(FXCollections.observableArrayList(tcpConnectionToServer.listOfCom));
			textChat.scrollTo(tcpConnectionToServer.listOfCom.size()-1);
			textToSend.clear();
		}
	}

}
