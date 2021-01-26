package atm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//Na razie Kod testowy - do przebudowy ale po³¹czenie z mysql i GUI(wraz z info ze po³¹czono z baz¹) dzia³a
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

		// zamknij serwer w momencie zamkniêcia GUI 
		primaryStage.setOnCloseRequest((WindowEvent) -> {
			System.exit(0);
		});

		Scene main = new Scene(box);
		window.setScene(main);
		primaryStage.setTitle("BANK PKO Bankomat - Server");
	    primaryStage.show();
		messages.appendText("L¹czenie z Baz¹ danych ...\n");
		java.sql.Connection connection = mysql.connectToDatabase("jdbc:mysql://", "127.0.0.1", "atm", "root", "root");//laczenie z baza mysql		
		if(connection!=null)//sprawdz czy polaczono z baz¹
		{
			
			messages.appendText("Po³¹czono z baz¹ danych Mysql\n");
		}
		else		
		{
		 messages.appendText("B³¹d po³¹czenia z Baz¹ danych Mysql\n");		
		
		}
		new Thread(() -> {
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				messages.appendText(String.format("Server Nas³uchuje na porcie: %s%n", port));
				//oczekiwanie na pod³¹czenie przez klientów
				while (true) {
					Socket socket = serverSocket.accept();
					new Thread(new ConnectionHandler(socket, this.mysql)).start();
					messages.appendText(String.format("Nowy Klient Bankomatu Pod³¹czony.%n"));
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
