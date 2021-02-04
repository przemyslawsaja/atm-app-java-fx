package atm.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import atm.Operations;
import java.sql.Statement;
import java.util.ArrayList;

import atm.client.ClientRequest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.ObservableList;
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
		primaryStage.setTitle("Poly Bank Bankomat - Server");
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
			private Customer customer;
			private BankAccount bankAccount;
			private AtmCard atmCard;
			public ConnectionHandler(Socket socket, MysqlAtmDatabase db,Connection connection) {
				super();
				this.socket = socket;
				this.db = db;
				this.connection=connection;
				
			}
			
			@Override
			public void run() 
			{
				try {
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					Statement st = MysqlAtmDatabase.createStatement(connection);
					ServerResponse res;
					ClientRequest req;

					//odbierz zapytanie od klienta
					while ((req = (ClientRequest) in.readObject()) != null) {

						res = new ServerResponse();

						//wybierz operacje
						switch (req.getOperation()) {
						case AUTHENTICATE:
							res.setOperation(req.getOperation());
							res.setOperationSuccess(this.db.authenticateCustomer(st,req.getcardId(), req.getPin()));
							if (!res.isOperationSuccess())								
							{
								res.setErrorMessage("Wprowadzi³eœ nieprawid³owe ID karty lub PIN.\nSpróbuj ponownie!");										
							}
							else
							{
								customer = mysql.setCustomerFromDatabase(st,req.getcardId());
								bankAccount = mysql.setBankAccountFromDatabase(st, req.getcardId());
								bankAccount.setCustomer(customer);
								atmCard= mysql.setAtmCardFromDatabase(st,req.getcardId());
								atmCard.setAccount(bankAccount);					
							}
							out.writeObject(res);
							break;
						case BALANCE_INQUIRY://sprawdzenie stanu konta
							if(db.getAccountBalance(st,bankAccount.getAccountNumber())!=-1)
									{
										bankAccount.setBalance(db.getAccountBalance(st,bankAccount.getAccountNumber()));
									}
							res.setOperation(req.getOperation());
							res.setOperationSuccess(true);							
							res.setUpdatedBalance(this.bankAccount.getBalance());
							out.writeObject(res);
							break;
						case DEPOSIT://wp³ata pieniedzy
							if(db.getAccountBalance(st,bankAccount.getAccountNumber())!=-1)
							{
								bankAccount.setBalance(db.getAccountBalance(st,bankAccount.getAccountNumber()));
							}
							res.setOperation(req.getOperation());
							res.setOperationSuccess(true);
							res.setRequestedAmount(req.getAmount());
							this.bankAccount.deposit(req.getAmount());
							MysqlAtmDatabase.balanceUpdate(st,this.bankAccount.getBalance(),this.bankAccount.getAccountNumber());
							// rejestracja wp³aty pieniêdzy na konto
							MysqlAtmDatabase.registerDeposit(st, req.getAmount(),this.bankAccount.getAccountNumber());
							res.setUpdatedBalance(this.bankAccount.getBalance());
							out.writeObject(res);
							break;
							
							//wyp³ata pieniedzy
						case WITHDRAW_CUSTOM:
						case WITHDRAW:
							if(db.getAccountBalance(st,bankAccount.getAccountNumber())!=-1)
							{
								bankAccount.setBalance(db.getAccountBalance(st,bankAccount.getAccountNumber()));
							}
							res.setOperation(req.getOperation());
							
							res.setOperationSuccess(bankAccount.withdraw(req.getAmount()));
							
							res.setRequestedAmount(req.getAmount());
							if (!res.isOperationSuccess())								
							{
								res.setErrorMessage("Nie posiadasz wystarczaj¹cych œrodków do wyp³aty wybranej kwoty!");										
							}
							else
							{
								MysqlAtmDatabase.balanceUpdate(st,this.bankAccount.getBalance(),this.bankAccount.getAccountNumber());
								//zarejestrowanie operacji wyp³aty
								MysqlAtmDatabase.registerWithdraw(st, req.getAmount(),this.bankAccount.getAccountNumber());
								res.setUpdatedBalance(this.bankAccount.getBalance());	
							}
							out.writeObject(res);
							break;
						case OPERATION_HISTORY:
							
							res.setOperation(req.getOperation());
							res.setOperationSuccess(true);
							res.setOperationHistoryList(MysqlAtmDatabase.getOperationHistory(st, this.bankAccount.getAccountNumber()));
							
							
							out.writeObject(res);
							break;
						
						}

						if (req.getOperation() != Operations.EXIT) {
							messages.appendText(String.format("%nZapytanie Klienta >>%n%s", req));

							if (res.isOperationSuccess()) {
								messages.appendText(String.format(" => OK%n"));
							} else {
								messages.appendText(String.format(" => B³¹d%n"));
							}
						} else {
							//przypadek gdy klient siê roz³¹cza
							if (req.getcardId() == -1) {
								messages.appendText(
										String.format("%nKlient Bankomatu roz³¹czony%n---%n", req.getcardId()));
							} else {
								messages.appendText(String.format("%nKlien Bankomatu - ID Karty #: %s Roz³¹czony.%n---%n",
										req.getcardId()));
							}
						}

					}
				} catch (

				IOException ioe) {
					System.out.printf("IOException: %s%n", ioe);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

		}


}
