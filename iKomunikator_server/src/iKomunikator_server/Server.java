package iKomunikator_server;

/**
 * Simple Http Server opened on given ip and port number.
 * Created by lukasz on 24.04.16.
 */

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {

    private HttpServer mServer;

    public Server(String ip, int port){
        System.out.println("Starting chat server");
        try {
            InetSocketAddress socket = new InetSocketAddress(InetAddress.getByName(ip),port);
            mServer = HttpServer.create(socket, 0);
            mServer.createContext("/", new Parser());
            mServer.setExecutor(null);
            mServer.start();
            System.out.println("Server started");
        } catch (IOException e) {
            System.out.printf("Error binding to the socket\n");
            System.out.println("Server stoped");
            if(Main.debug == true) e.printStackTrace();
        }
    }

    static class Parser implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "To jest odpowiedz z serwera chat o poprawny nawiazaniu komunikacji.";
            System.out.println(response);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
