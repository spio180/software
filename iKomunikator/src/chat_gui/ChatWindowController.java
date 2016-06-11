package chat_gui;

import common.Const;
import common.Message;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class ChatWindowController {
	@FXML private Button butWyczysc;
	@FXML public Button butWyslij;
	@FXML public ListView<String> textChat;
	@FXML private TextField textToSend;
	@FXML private ListView<String> userList;
	@FXML public TabPane tabChat;
	public static volatile String loggedUserName = "";
	private TcpClient tcpConnectionToServer;

	public void setTcpConnectionToServer(TcpClient connection){
		tcpConnectionToServer = connection;
	}

	@FXML private void butWyczyscClick(){
		textToSend.clear();
	}

	@FXML private void butWyslijClick(){
		String msg = textToSend.getText();
		String msgFull = ChatWindowController.loggedUserName.concat(" - ").concat(msg);

		if (msg.length() != 0)
		{
			Message message = new Message();
			message.addLineToMessageBody(Const.BODY, msg);
			message.setReceiver(Const.USER_ALL);
			message.setSender(ChatWindowController.loggedUserName);
			message.setType(Const.MSG_DO_WSZYSTKICH);
			
			tcpConnectionToServer.sendMessage(message);
			tcpConnectionToServer.listOfCom.add(msgFull);
			System.out.println(tcpConnectionToServer.listOfCom.size());
			textChat.setItems(FXCollections.observableArrayList(tcpConnectionToServer.listOfCom));
			textChat.scrollTo(tcpConnectionToServer.listOfCom.size()-1);
			textToSend.clear();
		}
	}
	
	public Boolean ContainsTab(String title) {
		Boolean result = false;
		
		for (Tab tab : this.tabChat.getTabs()) {
			if (tab.getText().toUpperCase() == title.toUpperCase()) {
				result = true;
			}
		}
		
		return result;
	}
}

