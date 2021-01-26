package atm.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ProcessingThread implements Runnable{
	
	private Socket socket;

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Scanner sc = new Scanner(System.in);
	
	
	public ProcessingThread(Socket socket) {
		super();
		this.socket = socket;
	}


	@Override
	public void run() {
	
		//tutaj obs³uga zdarzeñ klienta
	}
}
