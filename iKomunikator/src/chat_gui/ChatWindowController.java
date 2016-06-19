package chat_gui;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class ChatWindowController {

	private Stage stageOknaLogowania;
	private volatile boolean running = true;
	private TcpClient tcpConnectionToServer;
	private String loggedUserName;
	private Map<String, String> sortedUsers;
	private HashMap<String, String> forbiddenExpressions = new HashMap<String, String>();
	private HashMap<String, List<String>> listaListChatow;
	private HashMap<String, ListView<String>> listaListView;
	private HashMap<String, String> convertChars = new HashMap<String, String>();

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
		System.out.println("Kliknieto button WYLOGUJ - poczatek obslugi zdarzenia");
	    Stage stage = (Stage)this.butWyloguj.getScene().getWindow();
	    stage.hide();
	    stage.getOnCloseRequest().handle(new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));

	    this.stageOknaLogowania.show();
	    Scene scena = this.stageOknaLogowania.getScene();
		TextField ipTextField = (TextField) scena.lookup("#userIPTest");
		ipTextField.requestFocus();
		System.out.println("Kliknieto button WYLOGUJ - koniec obslugi zdarzenia");
	}

	@FXML private void butWyslijClick() throws UnsupportedEncodingException{
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
						listaListChatow.put(uzytkownik, new ArrayList<>());
						listaListChatow.get(uzytkownik).add("Rozpocznij czat z " + uzytkownik);
						listaListView.put(uzytkownik, new ListView<String>());
						ListView<String> currentlistView = listaListView.get(uzytkownik);
						currentlistView.setId(uzytkownik.concat("_chat"));
						currentlistView.setItems(FXCollections.observableArrayList(listaListChatow.get(uzytkownik)));
						Tab tab = new Tab();
						tab.setText(uzytkownik);
						tab.setClosable(true);
						tab.setContent(currentlistView);
						this.tabChat.getTabs().add(tab);
					}
				}
			}
		}
	}

	public void InitializeCharConversionTable() {
		this.convertChars = new HashMap<String, String>();
		this.convertChars.put("¹","a");
		this.convertChars.put("æ","c");
		this.convertChars.put("ê","e");
		this.convertChars.put("³","l");
		this.convertChars.put("ñ","n");
		this.convertChars.put("ó","o");
		this.convertChars.put("œ","s");
		this.convertChars.put("Ÿ","z");
		this.convertChars.put("¿","z");

		this.convertChars.put("¥","A");
		this.convertChars.put("Æ","C");
		this.convertChars.put("Ê","E");
		this.convertChars.put("£","L");
		this.convertChars.put("Ñ","N");
		this.convertChars.put("Ó","O");
		this.convertChars.put("Œ","S");
		this.convertChars.put("","Z");
		this.convertChars.put("¯","Z");
	}

    private String ConvertUnacceptableCharacters(String messageText) {
	   	String result = messageText;

    	for(String key : this.convertChars.keySet()){
    		result = result.replaceAll(key, this.convertChars.get(key));
		}

    	return result;
	}

    private void wyslijWiadomosc() throws UnsupportedEncodingException {
		String msg = this.ConvertUnacceptableCharacters(textToSend.getText());

		if (msg.length() != 0) {
			if (WiadomoscZawieraNiedozwoloneWyrazenie()) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(Const.INFO_HEADER);
				alert.setContentText("Wiadomoœæ zawiera niedozwolone wyra¿enia !");
				alert.showAndWait();
				return;
			}

			Message message = new Message();

			if (this.tabChat != null && this.tabChat.getTabs().size()>0) {
				int selectedIndex = this.tabChat.getSelectionModel().getSelectedIndex();

				System.out.println("Selected tab index: " + Integer.toString(selectedIndex));
				Tab tab = this.tabChat.getTabs().get(selectedIndex);

				String currentTabName = tab.getText();
				System.out.println("Current tab name" + currentTabName);

				//Tab: Wszyscy
				if (selectedIndex == 0) {
					System.out.println("Do wszystkich");
					message.setType(Const.MSG_DO_WSZYSTKICH);
					message.setReceiver(Const.USER_ALL);
				}
				else {
					System.out.println("Do pojedynczego");
					message.setType(Const.MSG_DO_UZYTKOWNIKA);
					message.setReceiver(currentTabName);
				}

				message.addLineToMessageBody(Const.BODY, msg);
				message.setSender(loggedUserName);
				tcpConnectionToServer.sendMessage(message);
				this.listaListChatow.get(currentTabName).add(loggedUserName.concat(" - ").concat(msg));
				this.listaListView.get(currentTabName).setItems(FXCollections.observableArrayList(this.listaListChatow.get(currentTabName)));

				//textChat.setItems(FXCollections.observableArrayList(tcpConnectionToServer.listaListChatow.get(currentTabName)));
				textToSend.clear();
			}
		}
    }

    private boolean WiadomoscZawieraNiedozwoloneWyrazenie() {
    	String msg = textToSend.getText();

		for (String key : this.forbiddenExpressions.keySet()) {
			if (msg.toLowerCase().contains(key.toLowerCase())) {
				return true;
			}
		}

    	return false;
    }

    @FXML public void handleEnterPressed(KeyEvent event) throws UnsupportedEncodingException {
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

	public void UpdateForbidden(HashMap<String, String> forbidden) {
		this.forbiddenExpressions = new HashMap<String, String>();

		for (String key : forbidden.keySet()) {
			if (!this.forbiddenExpressions.containsKey(key)) {
				this.forbiddenExpressions.put(key,key);
			}
		}
	}

	public void startListenningThread2() {
		listenningThread = new ListenningThread2();
		listenningThread.start();
		running = true;

		listaListChatow = new HashMap<String, List<String>>();
		listaListChatow.put("Wszyscy", new ArrayList<>());
		listaListChatow.get("Wszyscy").add("iKomunikator - WITAMY !");

		listaListView = new HashMap<String, ListView<String>>();
		listaListView.put("Wszyscy", textChat);

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

		//Listener for All user message
		listenningThread.getReceivedMessageAllProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(final ObservableValue<? extends Number> observable,
		          final Number oldValue, final Number newValue) {

		          Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		            	System.out.println("Message for all received");

		            	listaListView.get("Wszyscy").setItems(FXCollections.observableArrayList(listaListChatow.get("Wszyscy")));

		            }
		          });
		      }
		    });


		//Listener for All user message
		listenningThread.getreceivedMessageSingleUserProperty().addListener(new ChangeListener<Number>() {
		      @Override
		      public void changed(final ObservableValue<? extends Number> observable,
		          final Number oldValue, final Number newValue) {

		          Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		            	System.out.println("Message for single user received");

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
		private IntegerProperty receivedMessageAll;
		private IntegerProperty receivedMessageSingleUser;


		public ListenningThread2() {
		      receivedUserListProperty = new SimpleIntegerProperty(this, "int", 0);
		      receivedMessageAll = new SimpleIntegerProperty(this, "int", 0);
		      receivedMessageSingleUser = new SimpleIntegerProperty(this, "int", 0);
		      setDaemon(true);
		}

		public int getReceivedUserListInt() {
		      return receivedUserListProperty.get();
		}

		public IntegerProperty getReceivedUserList() {
		      return receivedUserListProperty;
		}

		public int getReceivedMessageAllPropertyInt() {
		      return receivedMessageAll.get();
		}


		public IntegerProperty getReceivedMessageAllProperty() {
		      return receivedMessageAll;
		}


		public int getreceivedMessageSingleUserInt() {
		      return receivedMessageSingleUser.get();
		}

		public IntegerProperty getreceivedMessageSingleUserProperty() {
		      return receivedMessageSingleUser;
		}

	 	@Override
	    public void run() {

	    	System.out.println(">>>>ListenningThread2: started");
	    	Message message = new Message();

	    	while (running) {

	    		message = tcpConnectionToServer.getMsgFromInBuff();

	        	if (message != null) {

	        		System.out.println("Received Message type: " + message.getType());

	        		if (message.getType().equals(Const.MSG_LISTA_UZ)) {

	        			if (message.getMessageBody().size() > 0) {
	        				sortedUsers = new TreeMap<String, String>(message.getMessageBody());
	        				receivedUserListProperty.set(receivedUserListProperty.get() + 1);
	        				System.out.println("User list received");

	        			}
					}

	        		if (message.getType().equals(Const.MSG_DO_UZYTKOWNIKA)) {
	        			String messageText = message.getSender().concat(" - ").concat(message.getMessageBody().get(Const.BODY)).replace("\\n", "").replace("\n", "");

	        			System.out.println("Message from " + message.getSender());
	        			receivedMessageSingleUser.set(receivedMessageSingleUser.get() + 1);
					}

					if (message.getType().equals(Const.MSG_DO_WSZYSTKICH)) {
						String messageText = message.getSender().concat(" - ").concat(message.getMessageBody().get(Const.BODY)).replace("\\n", "").replace("\n", "");
						List<String> stringListForAll = listaListChatow.get("Wszyscy");

						stringListForAll.add(messageText);
						receivedMessageAll.set(receivedMessageAll.get() + 1);
					}

					if (message.getType().equals(Const.MSG_LISTA_WYRAZEN_ZABRONIONYCH)) {

						forbiddenExpressions = new HashMap<String, String>();

						for (String key : message.getMessageBody().keySet()) {
						    if (!forbiddenExpressions.containsKey(key)) {
						    	forbiddenExpressions.put(key, message.getMessageBody().get(key));
						    }
						}
					}
	        	}
	    	}

	    	System.out.println(">>>>ListenningThread2: stoped");
	    }
	}
}
