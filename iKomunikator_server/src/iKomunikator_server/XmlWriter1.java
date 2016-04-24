package iKomunikator_server;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlWriter1 {
	public static int ServerPort;
	public static String ServerIP;
	public static int ConnectionsLimit;
	
	public XmlWriter1(String ip,int port, int connections_limit){
		ServerIP = ip;
		ServerPort = port;
		ConnectionsLimit = connections_limit;		
	}
	
	public Boolean SerializeServerConfig() throws Exception{
		Boolean result = false;
		
		try
		{
			DocumentBuilderFactory documentBuilderFactory= DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
			
			Document document= documentBuilder.newDocument();
			
			Element element= document.createElement("ServerConfig");
			document.appendChild(element);
			
			Element ip = document.createElement("IP");
			ip.appendChild(document.createTextNode(ServerIP));
			element.appendChild(ip);
			
			Element port = document.createElement("ServerPort");
			port.appendChild(document.createTextNode(Integer.toString(ServerPort)));
			element.appendChild(port);
			
			Element connectionLimit = document.createElement("ConnectionsLimit");
			connectionLimit.appendChild(document.createTextNode(Integer.toString(ConnectionsLimit)));
			element.appendChild(connectionLimit);
			
			
			TransformerFactory transformerFactory= TransformerFactory.newInstance();
			Transformer transformer= transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			
			StreamResult streamResult = new StreamResult(new File("Server.cfg"));
			transformer.transform(source, streamResult);
		}
		catch(Exception ex) {
			return false;
		}
		
		return result;
	
	}

}

	
	