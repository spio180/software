package application;

import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import common.Serialization;
import common.ServerConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainWindow {
	@FXML 
	private Button btnOk;
	
	@FXML 
	private Button btnCancel;	
	
	@FXML 
	private Button btnRefresh;
	
	@FXML 
	private Button btnAdd;
	
	@FXML 
	private Button btnUpdate;
	
	@FXML 
	private Button btnRemove;	
	
	@FXML 
	private TextField txtIP;
	
	@FXML 
	private TextField txtPort;	
	
	@FXML 
	private TextField txtConnectionLimit;		
	
	@FXML 
	private TableView<String> tbvZabronione;			
	
	@FXML 
	private void OnOkMouseClicked() {
		this.WriteToConfig();
	}
	
	@FXML 
	private void OnCancelMouseClicked() {
		Stage stage = (Stage)this.btnCancel.getScene().getWindow();
		stage.close();
	}	
	
	@FXML 
	private void OnRefreshMouseClicked() throws ParserConfigurationException, SAXException {
		this.ReadConfig();	
	}	
	
	@FXML 
	private void OnAddMouseClicked() {
	}	
	
	@FXML 
	private void OnUpdateMouseClicked() {
	}	
	
	@FXML 
	private void OnRemoveMouseClicked() {
	}	


	private void WriteToConfig() {		
		try {		
			ServerConfig conf = new ServerConfig();
			String applicationPath = new File(".").getCanonicalPath().concat("\\Server.cfg");
			conf.IP = this.txtIP.getText();
			conf.ServerPort = Integer.parseInt(this.txtPort.getText());
			conf.ConnectionsLimit = Integer.parseInt(this.txtConnectionLimit.getText());			
			Serialization.SerializeServerConfig(conf, applicationPath);
			Stage stage = (Stage)this.btnCancel.getScene().getWindow();
			stage.close();			
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void ReadConfig() throws ParserConfigurationException, SAXException {		
		try {		
			ServerConfig conf = new ServerConfig();
			String applicationPath = new File(".").getCanonicalPath().concat("\\Server.cfg");
			Serialization.DeserializeServerConfig(applicationPath, conf);
			this.txtConnectionLimit.setText(Integer.toString(conf.ConnectionsLimit));
			this.txtPort.setText(Integer.toString(conf.ServerPort));
			this.txtIP.setText(conf.IP);
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public MainWindow(){
		
	}
}
