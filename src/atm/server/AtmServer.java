package atm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//Na razie Kod testowy - do przebudowy ale po��czenie z mysql i GUI(wraz z info ze po��czono z baz�) dzia�a
public class AtmServer extends Application {

	Stage window;
	MysqlAtmDatabase mysql=new MysqlAtmDatabase();
	TextArea messages = new TextArea();
	private static int port=9005;
	
	@Override
	public void start(Stage primaryStage) {

		window = primaryStage;
		messages.setPrefHeight(550);
		messages.setEditable(false);

		VBox box = new VBox(10, messages);
		box.setPrefSize(500, 600);

		// zamknij serwer w momencie zamkni�cia GUI 
		primaryStage.setOnCloseRequest((WindowEvent) -> {
			System.exit(0);
		});

		Scene main = new Scene(box);
		window.setScene(main);
		primaryStage.setTitle("BANK PKO Bankomat - Server");
	    primaryStage.show();
		messages.appendText("L�czenie z Baz� danych ...\n");
		java.sql.Connection connection = mysql.connectToDatabase("jdbc:mysql://", "127.0.0.1", "atm", "root", "root");//laczenie z baza mysql		
		if(connection!=null)//sprawdz czy polaczono z baz�
		{
			
			messages.appendText("Po��czono z baz� danych Mysql\n");
		}
		else		
		{
		 messages.appendText("B��d po��czenia z Baz� danych Mysql\n");		
		
		}
		new Thread(() -> {
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				messages.appendText(String.format("Server Nas�uchuje na porcie: %s%n", port));
				//oczekiwanie na pod��czenie przez klient�w
				while (true) {
					Socket socket = serverSocket.accept();
					new Thread(new ConnectionHandler(socket, this.mysql)).start();
					messages.appendText(String.format("Nowy Klient Bankomatu Pod��czony.%n"));
							 }			
				} catch (IOException ioe) {
				System.err.printf("IOException: %s%n", ioe);
				ioe.printStackTrace();
			}
		}).start();		
	}
	
	 public static void main(String[] args) {
	        launch(args);
	    									}

}
