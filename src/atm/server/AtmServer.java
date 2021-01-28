package atm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.sql.Statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AtmServer extends Application {

	Stage window;
	MysqlAtmDatabase mysql=new MysqlAtmDatabase();
	TextArea messages = new TextArea();
	private static int port=9005;
	
	 public static void main(String[] args) {
	        launch(args);
	    									}
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
		Connection connection = MysqlAtmDatabase.connectToDatabase("jdbc:mysql://", "127.0.0.1", "atm", "root", "root");//laczenie z baza mysql		
		if(connection!=null)//sprawdz czy polaczono z baz¹
		{
			
			messages.appendText("Po³¹czono z baz¹ danych Mysql\n");
		}
		else		
		{
		
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("B³¹d po³aczenia z baz¹ Mysql Server zostanie zamkniêty");		
			alert.show();
			try
			{
			    Thread.sleep(1000);
			    System.exit(0);
			}
			catch(InterruptedException ex)
			{
			    Thread.currentThread().interrupt();
			}

		}
		
		
		new Thread(() -> {
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				messages.appendText(String.format("Server Nas³uchuje na porcie: %s%n", port));
				//oczekiwanie na pod³¹czenie przez klientów
				while (true) {
					Socket socket = serverSocket.accept();
					new Thread(new ConnectionHandler(socket, this.mysql,connection)).start();//start nowego w¹tku dla obs³ugi nowego klienta
					messages.appendText(String.format("Nowy Klient Bankomatu Pod³¹czony.%n"));
							 }			
				} catch (IOException ioe) {
				System.err.printf("IOException: %s%n", ioe);
				ioe.printStackTrace();
			}
		}).start();		
	}
	 
	 public class ConnectionHandler implements Runnable {
			
			private Socket socket;
			private MysqlAtmDatabase db;
			private Connection connection;
			public ConnectionHandler(Socket socket, MysqlAtmDatabase db,Connection connection) {
				super();
				this.socket = socket;
				this.db = db;
				this.connection=connection;
			}
			
			@Override
			public void run() 
			{
				//kod tylko dla sprawdzenia funkcji - do wywalenia(po zalogowaniu uzytkownika, tworzymy obiekty klas mój pomys³ jest taki, ¿e pracujemy na obiektach klas, dopiero
				//po wylogowaniu klienta zapisujemy zaktualizowany stan konta do bazy)
				Statement st = mysql.createStatement(connection);
				boolean check= mysql.authenticateCustomer(st,12312421, "1234");//tutaj trzeba dorobiæ szyfrowanie has³a, zeby wysy³ac do bazy juz zaszyfrowane has³o do porównania
				if(check ==true) {	 messages.appendText("Uzytkownik istnieje w bazie i podano dobre haslo\n");}
				else { messages.appendText("Nie ma u¿ytkownika o podanym ID oraz hasle\n");}
				Customer customer = mysql.setCustomerFromDatabase(st,12312421);
				messages.appendText(customer.getCity());
				BankAccount bankAccount = mysql.setBankAccountFromDatabase(st, 12312421);
				bankAccount.setCustomer(customer);
				messages.appendText(bankAccount.getCustomer().getEmail());
				AtmCard atmCard= mysql.setAtmCardFromDatabase(st,12312421);
				atmCard.setAccount(bankAccount);
				messages.appendText(atmCard.getType());						
				//tutaj ca³y kod obs³ugi klienta
			}

		}


}
