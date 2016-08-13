package ChatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(8888);
			System.out.println("Server On!>>>>");
			while(true) {
                Socket client = server.accept();
                new Thread(new Service(client)).start();;
           
            }
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
