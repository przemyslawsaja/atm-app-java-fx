package atm.server;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//Na razie Kod testowy - do przebudowy ale po��czenie z mysql i GUI(wraz z info ze po��czono z baz�) dzia�a
public class BankServer extends Application {

	Stage window;
	MysqlAtmDatabase mysql=new MysqlAtmDatabase();
	TextArea messages = new TextArea();
	
	@Override
	public void start(Stage primaryStage) {

		window = primaryStage;
		messages.setPrefHeight(550);
		messages.setEditable(false);

		VBox box = new VBox(10, messages);
		box.setPrefSize(500, 600);

		// closes server when closing GUI
		primaryStage.setOnCloseRequest((WindowEvent) -> {
			System.exit(0);
		});

		Scene main = new Scene(box);
		window.setScene(main);
		primaryStage.setTitle("BANK PKO Bankomat - Server");		
		java.sql.Connection connection = mysql.connectToDatabase("jdbc:mysql://", "127.0.0.1", "atm", "root", "root");		
		if(connection!=null)
		{
			messages.setText("Po��czono z baz� danych Mysql");
		}
		else		
		{
			messages.setText("B��d po��czenia z Baz� danych Mysql");		
			
		}
		primaryStage.show();

	}
	
	 public static void main(String[] args) {
	        launch(args);
	    }

}
