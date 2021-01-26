package atm.server;

import java.net.Socket;

public class ConnectionHandler implements Runnable {
	
	private Socket socket;
	private MysqlAtmDatabase db;
	public ConnectionHandler(Socket socket, MysqlAtmDatabase db) {
		super();
		this.socket = socket;
		this.db = db;
	}
	
	@Override
	public void run() 
	{
		
		//tutaj ca³y kod obs³ugi klienta
	}

}
