package common;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Serialization {
   public static Boolean SerializeServerConfig(ServerConfig config_to_serialize, String config_file_path) {
	   Boolean result = false;
   
	   try {
		   DocumentBuilderFactory documentBuilderFactory= DocumentBuilderFactory.newInstance();
		   DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();           
		   Document document= documentBuilder.newDocument();           
		   Element element= document.createElement("ServerConfig");
		   document.appendChild(element);    

		   Element ip = document.createElement("IP");
		   ip.appendChild(document.createTextNode(config_to_serialize.IP.toString()));
		   element.appendChild(ip);
   
		   Element port = document.createElement("ServerPort");
		   port.appendChild(document.createTextNode(Integer.toString(config_to_serialize.ServerPort)));
		   element.appendChild(port);
   
		   Element connectionLimit = document.createElement("ConnectionsLimit");
           connectionLimit.appendChild(document.createTextNode(Integer.toString(config_to_serialize.ConnectionsLimit)));
           element.appendChild(connectionLimit);
                      
           TransformerFactory transformerFactory= TransformerFactory.newInstance();
           Transformer transformer= transformerFactory.newTransformer();
           DOMSource source = new DOMSource(document);
           
           StreamResult streamResult = new StreamResult(new File(config_file_path));
           transformer.transform(source, streamResult);
        }
        catch(Exception ex) {
        	Log.WriteToLog(ex.getMessage());
        }
       
        return result;   
    }	
	
   public static Boolean DeserializeServerConfig(String config_file_path, ServerConfig configuration) throws UnknownHostException, ParserConfigurationException, SAXException {
	   Boolean result = false;
	   configuration = new ServerConfig();
	   
	   try
	   {
		   DocumentBuilderFactory documentBuilderFactory= DocumentBuilderFactory.newInstance();
		   DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder(); 		   
		   Document document = documentBuilder.parse(config_file_path);
		   
		   NodeList ipNodeList = document.getElementsByTagName("IP");
		   for (int i=0; i< ipNodeList.getLength(); i++) {
			   Node node = ipNodeList.item(i);
			   
			   if (node.getNodeType() == Node.ELEMENT_NODE) {
				   Element element = (Element) node;
				   configuration.IP = element.getNodeValue();
			   }
		   }
		   
		   NodeList serverPortNodeList = document.getElementsByTagName("SERVERPORT");
		   for (int i=0; i< serverPortNodeList.getLength(); i++) {
			   Node node = serverPortNodeList.item(i);
			   
			   if (node.getNodeType() == Node.ELEMENT_NODE) {
				   Element element = (Element) node;
				   configuration.ServerPort = Integer.parseInt(element.getNodeValue());
			   }
		   }		
		   
		   NodeList connectionsLimitNodeList = document.getElementsByTagName("CONNECTIONSLIMIT");
		   for (int i=0; i< connectionsLimitNodeList.getLength(); i++) {
			   Node node = connectionsLimitNodeList.item(i);
			   
			   if (node.getNodeType() == Node.ELEMENT_NODE) {
				   Element element = (Element) node;
				   configuration.ConnectionsLimit = Integer.parseInt(element.getNodeValue());
			   }
		   }	
		   
		   result = true;
	   }
	   catch(IOException ex) {
		   return false;
	   }

	   return result;
    }
   
   public static String SerializeMessage(Message msg) throws UnsupportedEncodingException {
	   String result;
	   ByteArrayOutputStream baos = new ByteArrayOutputStream();
	   XMLEncoder xmlEncoder = new XMLEncoder(baos);
	   xmlEncoder.writeObject(msg);
	   xmlEncoder.close();
	   result = baos.toString("UTF-8");
	   result = result.replace("\\n", "").replace("\n", "");
	   result = result.concat("\n");
	   return result;
   }

   public static Message DeSerializeMessage(String msg) throws UnsupportedEncodingException {
	   Message result = new Message();
	   ByteArrayInputStream stream = new ByteArrayInputStream(msg.getBytes("UTF-8"));
	   XMLDecoder decoder;

	   try {
		   decoder = new XMLDecoder(stream);
		   result = (Message)decoder.readObject();
	   } catch (Exception ex) {
		   System.out.println(ex.getMessage());
		   ex.printStackTrace();
	   }

	   return result;
   }
}
