package chat_gui;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import common.Const;
import common.Message;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ChatWindowController {
	
	private Stage stageOknaLogowania;
	private volatile boolean running = true;
	private TcpClient tcpConnectionToServer;
	private String loggedUserName;
	private Map<String, String> sortedUsers;
	
	@FXML
	private Button butWyczysc;
	@FXML
	public Button butWyslij;
	@FXML
	public Button butWyloguj;	
	@FXML
	public ListView<String> textChat;
	@FXML
	private TextField textToSend;
	@FXML
	public ListView<String> userList;
	@FXML
	public TabPane tabChat;
	
	public ListenningThread2 listenningThread;
	
	public void setLoginScene(Stage stage) {
		if (stage == null) {
			throw new NullPointerException();
		}
		
		this.stageOknaLogowania = stage; 
	}

	public String getLoggedUserName() {
		return loggedUserName;
	}

	public void setLoggedUserName(String loggedUserName) {
		this.loggedUserName = loggedUserName;
	}

	public void setTcpConnectionToServer(TcpClient connection) {
		tcpConnectionToServer = connection;
	}

	@FXML
	private void butWyczyscClick() {
		textToSend.clear();
	}
	
	@FXML
	private void butWylogujClick() {
	    Stage stage = (Stage)this.butWyloguj.getScene().getWindow();
	    stage.close();
	    this.stageOknaLogowania.show();
	    Scene scena = this.stageOknaLogowania.getScene();
		TextField ipTextField = (TextField) scena.lookup("#userIPTest");
		ipTextField.requestFocus();
	}		

	@FXML private void butWyslijClick(){
        wyslijWiadomosc();
    }	

	@FXML
	private void OnListaUzytkownikowMouseClick(MouseEvent event) {
		if (this.tabChat != null) {
			if (userList.getSelectionModel().getSelectedItem() != null) {
				String uzytkownik = userList.getSelectionModel().getSelectedItem().toString();

				if (this.ContainsTab(uzytkownik)){
					for (Tab tab : this.tabChat.getTabs()) {
						if (Objects.equals(uzytkownik, tab.getText())) {
							this.tabChat.getSelectionModel().select(tab);
						}
					}
				}
				else{
					if (!Objects.equals(uzytkownik,loggedUserName)) {
						ListView<String> listView = new ListView<String>();
						listView.setId(uzytkownik.concat("_chat"));
						String leadMessage = "Rozpocznij czat z " + uzytkownik;
						listView.getItems().add(leadMessage);
						Tab tab = new Tab();
						tab.setText(uzytkownik);
						tab.setClosable(true);
						tab.setContent(listView);
						this.tabChat.getTabs().add(tab);
					}
				}

			}
		}
	}


    private void wyslijWiadomosc() {
		String msg = textToSend.getText();

		if (msg.length() != 0) {
			Message message = new Message();

			if (this.tabChat != null && this.tabChat.getTabs().size()>0) {
				int selectedIndex = this.tabChat.getSelectionModel().getSelectedIndex();
				Tab tab = this.tabChat.getTabs().get(selectedIndex);

				if (tab.getText() == "Wszyscy") {
					message.setType(Const.MSG_DO_WSZYSTKICH);
					message.setReceiver(tab.getText());
				}
				else {
					message.setType(Const.MSG_DO_UZYTKOWNIKA);
					message.setReceiver(Const.USER_ALL);
				}
				message.setType(Const.MSG_DO_WSZYSTKICH); /// ³atka na komunikacjê
				message.addLineToMessageBody(Const.BODY, msg);
				message.setSender(loggedUserName);
				tcpConnectionToServer.sendMessage(message);
				tcpConnectionToServer.listaListChatow.get(tab.getText()).add(loggedUserName.concat(" - ").concat(msg));
				textChat.setItems(FXCollections.observableArrayList(tcpConnectionToServer.listaListChatow.get(tab.getText())));
				textToSend.clear();
			}
		}
    }

    @FXML public void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            wyslijWiadomosc();
        }
    }

	public Boolean ContainsTab(String title) {
		for (Tab tab : this.tabChat.getTabs()) {
			if (Objects.equals(tab.getText(),title)) {
				return true;
			}
		}

		return false;
	}

	public void startListenningThread2() {
		listenningThread = new ListenningThread2();
		listenningThread.start();
		running = true;
		
		//Listener for User List Update
		listenningThread.getReceivedUserList().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(final ObservableValue<? extends Number> observable,
		          final Number oldValue, final Number newValue) {

		          Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		            	userList.getItems().clear();

		            	for(String key : sortedUsers.keySet()){
							String userName = sortedUsers.get(key);
							userList.getItems().add(userName);
						}

		            	System.out.println("User list updated");
		            }
		          });
		      }
		    });

		//Listener for User List Update
		listenningThread.getReceivedMessageProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(final ObservableValue<? extends Number> observable,
		          final Number oldValue, final Number newValue) {

		          Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		            	System.out.println("Message received");
		            }
		          });
		      }
		    });
	}

	public void terminateListenningThread() {
		running = false;
	}

	public class ListenningThread2 extends Thread {

		private IntegerProperty receivedUserListProperty;
		private IntegerProperty receivedMessageProperty;

		public ListenningThread2() {
		      receivedUserListProperty = new SimpleIntegerProperty(this, "int", 0);
		      receivedMessageProperty = new SimpleIntegerProperty(this, "int", 0);
		      setDaemon(true);
		}

		public int getReceivedUserListInt() {
		      return receivedUserListProperty.get();
		}

		public IntegerProperty getReceivedUserList() {
		      return receivedUserListProperty;
		}

		public int getReceivedMessagePropertyInt() {
		      return receivedMessageProperty.get();
		}

		public IntegerProperty getReceivedMessageProperty() {
		      return receivedMessageProperty;
		}

	 	@Override
	    public void run() {

	    	System.out.println(">>>>ListenningThread2: started");
	    	Message message = new Message();

	    	while (running) {

	    		message = tcpConnectionToServer.getMsgFromInBuff();

	        	if (message != null) {

	        		if (message.getType().equals(Const.MSG_LISTA_UZ)) {

	        			if (message.getMessageBody().size() > 0) {
	        				sortedUsers = new TreeMap<String, String>(message.getMessageBody());
	        				receivedUserListProperty.set(receivedUserListProperty.get() + 1);
	        			}
					}

	        		if (message.getType().equals(Const.MSG_DO_UZYTKOWNIKA)) {
	        			receivedMessageProperty.set(receivedUserListProperty.get() + 1);
					}


					if (message.getType().equals(Const.MSG_DO_WSZYSTKICH)) {
						receivedMessageProperty.set(receivedUserListProperty.get() + 1);
					}
	        	}
	    	}

	    	System.out.println(">>>>ListenningThread2: stoped");
	    }
	}
}
