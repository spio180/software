package chat_gui;
import java.util.ArrayList;
import java.util.List;
import common.Const;
import common.Message;
import common.Serialization;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	private String loggedUserName;
	private TcpClient tcpConnectionToServer;
	
	public void SetMyUserName(String user_name) {
		this.loggedUserName = user_name;
	}
	
	public String GetMyUserName() {
		return this.loggedUserName;
	}

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
			Message message = new Message();
			message.addLineToMessageBody(Const.BODY, textToSend.getText());
			message.setReceiver(Const.USER_ALL);
			message.setSender(this.loggedUserName);
			message.setType(Const.MSG_DO_WSZYSTKICH);
			
			tcpConnectionToServer.sendMessage(message);
			tcpConnectionToServer.listOfCom.add(textToSend.getText());
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

