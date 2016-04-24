package common;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ServerConfig implements java.io.Serializable {
	
	public static final long serialVersionUID = 1L; 
	public int ServerPort;
	public String IP;
	public int ConnectionsLimit;	
	public List<String> ForbiddenWords;
	
	public ServerConfig() throws UnknownHostException {
		this.IP = "127.0.0.1";
		this.ServerPort = 58800;
		this.ConnectionsLimit = 10;
		this.ForbiddenWords = new ArrayList<String>();
	}
	
	public ServerConfig(String ip, int port, int connections_limit, List<String> forbidden_words) {
		this.IP = ip;
		this.ServerPort = port;
		this.ConnectionsLimit = connections_limit;		
		this.ForbiddenWords = new ArrayList<String>();
		
		for (String word : forbidden_words) {
			this.ForbiddenWords.add(word);
		}
	}
}
