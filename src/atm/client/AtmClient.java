package atm.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class AtmClient extends Application{
	
	Stage window;
	Scene loginView, mainMenuView, balanceView, depositView, withdrawView;
	TextField txtUserID, txtPin, txtDeposit, txtWithdraw;
	Button btnSignIn, btnDeposit, btnWithdraw, btnCheckBal, btnMainMenuExit, btnMain, btnDepMain, btnWdMain,
			btnBalInqClose, btnDepositCash, btnWithdrawCash, btnLoginExit;
	Text errorMsg, withdrawError, depositError;
	Label lblBalance, lblAmt;
	static InetAddress hostname;
	static int port=9005;
	public static void main(String[] args) {
		launch(args);
	}
	
	
	@Override
	public void start(Stage primaryStage) 
	{
		//Ustawienia ogólne		
		window = primaryStage;
		primaryStage.setTitle("Bank PKO: Aplikacja Kliencka");

		// Panel Logowania
		GridPane loginPane = new GridPane();
		loginPane.setAlignment(Pos.CENTER);
		loginPane.setHgap(10);
		loginPane.setVgap(10);
		loginPane.setPadding(new Insets(25, 25, 25, 25));
		Text title = new Text("Witamy W Banku PKO\nWprowadz ID karty oraz PIN:");
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		title.setTextAlignment(TextAlignment.CENTER);
		loginPane.add(title, 0, 0, 2, 1);

		Label lblUserID = new Label("ID:");
		loginPane.add(lblUserID, 0, 2);

		txtUserID = new TextField();
		loginPane.add(txtUserID, 1, 2);

		Label lblPin = new Label("PIN:");
		loginPane.add(lblPin, 0, 3);

		txtPin = new PasswordField();
		loginPane.add(txtPin, 1, 3);

		btnSignIn = new Button("Zaloguj");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.BOTTOM_RIGHT);
		btnBox.getChildren().add(btnSignIn);
		btnLoginExit = new Button("Wyjdz");
		btnBox.getChildren().add(btnLoginExit);
		loginPane.add(btnBox, 1, 4);

		errorMsg = new Text();
		errorMsg.setFill(Color.FIREBRICK);
		loginPane.add(errorMsg, 1, 6);
		// Blokada wy³¹czania przez klikniêcie X
		primaryStage.setOnCloseRequest(e -> {
		e.consume();

			// Poka¿ okno dialogowe instrukcji zamkniêcia aplikacji
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Jak roz³¹czyæ siê z serwerem");
			alert.setHeaderText("Prawid³owy sposób roz³¹czenia z serverem Banku:");
			alert.setContentText(
					"Proszê u¿yæ przycisku wyjdz");
			alert.show();
		});
		//proba dzia³ania przycisku Exit
		btnLoginExit.setOnAction(e -> {			
		
			Platform.exit();
		});
		//Tu dopisaæ kod GUI dla innych zak³adek
	
		loginView = new Scene(loginPane, 400, 450);		
		window.setScene(loginView);
		primaryStage.setResizable(false);
		primaryStage.show();
		Socket socket = null;
		//po³¹czenie z serwerem
		try {
			hostname = InetAddress.getByName(null);
			socket = new Socket(hostname, port);
	
			new Thread(new ProcessingThread(socket)).start();//przejscie do procesu obs³uguj¹cego aplikacje kliencka oraz komunikacje z serwerem

		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("I/O Error: " + ex.getMessage());
		}
		
	}
	
	//tutaj obs³uga zdarzeñ scen itd itp.
	private class ProcessingThread implements Runnable{
		
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
			
			
		
			//tutaj obs³uga zdarzeñ klienta po po³aczeniu z serverem
		}
	}

	
	
	

}
