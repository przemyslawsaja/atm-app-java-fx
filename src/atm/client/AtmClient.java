package atm.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import atm.PinEncryptionSHA1;
import atm.server.ServerResponse;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
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
		// Menu g³ówne
		GridPane menuPane = new GridPane();
		menuPane.setPadding(new Insets(20, 20, 20, 20));
		menuPane.setAlignment(Pos.CENTER);
		menuPane.setVgap(10);

		Text welcomeMsg = new Text("Jak¹ operacjê chcesz wykonaæ?");
		welcomeMsg.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));

		btnDeposit = new Button("Wp³aæ");
		btnWithdraw = new Button("Wyp³aæ");
		btnCheckBal = new Button("SprawdŸ Saldo");
		btnMainMenuExit = new Button("Zakoñcz Sesjê");

		btnDeposit.setMinWidth(275);
		btnWithdraw.setMinWidth(275);
		btnCheckBal.setMinWidth(275);
		btnMainMenuExit.setMinWidth(275);

		btnDeposit.setMinHeight(50);
		btnWithdraw.setMinHeight(50);
		btnCheckBal.setMinHeight(50);
		btnMainMenuExit.setMinHeight(50);

		menuPane.add(welcomeMsg, 0, 0, 2, 1);
		menuPane.add(btnCheckBal, 0, 1);
		menuPane.add(btnDeposit, 0, 2);
		menuPane.add(btnWithdraw, 0, 3);
		menuPane.add(btnMainMenuExit, 0, 4);
		
		//Panel Saldo konta
		GridPane balancePane = new GridPane();

		balancePane.setPadding(new Insets(0, 10, 10, 10));
		balancePane.setAlignment(Pos.CENTER);
		balancePane.setVgap(15);

		lblBalance = new Label("");
		lblBalance.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		lblBalance.setWrapText(true);
		lblBalance.setTextAlignment(TextAlignment.CENTER);
		lblAmt = new Label("");
		lblAmt.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		lblAmt.setWrapText(true);
		btnMain = new Button("Menu G³ówne");
		btnBalInqClose = new Button("WyjdŸ");

		balancePane.add(lblAmt, 0, 0, 2, 1);
		balancePane.add(lblBalance, 0, 2, 2, 1);
		balancePane.add(btnBalInqClose, 3, 4);
		balancePane.add(btnMain, 0, 4);
		//panel wp³aty pieniedzy 
		GridPane depositPane = new GridPane();
		depositPane.setAlignment(Pos.CENTER);
		depositPane.setVgap(15);
		depositPane.setHgap(15);

		btnDepMain = new Button("Menu G³ówne");
		Label lblDeposit = new Label("Wprowadz Kwotê któr¹ chcesz wp³aciæ:");
		lblDeposit.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
		lblDeposit.setWrapText(true);
		btnDepositCash = new Button("Wp³aæ");
		btnDepositCash.setMinSize(100, 40);
		txtDeposit = new TextField();
		txtDeposit.setMinSize(250, 40);
		depositError = new Text();
		depositError.setFill(Color.FIREBRICK);
		depositError.setWrappingWidth(300);

		depositPane.add(lblDeposit, 0, 0, 2, 1);
		depositPane.add(txtDeposit, 0, 2);
		depositPane.add(btnDepositCash, 1, 2);
		depositPane.add(depositError, 0, 4, 2, 1);

		BorderPane depBorderPane = new BorderPane();
		depBorderPane.setPadding(new Insets(15, 15, 15, 15));
		depBorderPane.setCenter(depositPane);
		depBorderPane.setBottom(btnDepMain);
		//Tu dopisaæ kod GUI dla innych zak³adek
	
		loginView = new Scene(loginPane, 400, 450);	
		mainMenuView = new Scene(menuPane, 400, 450);
		balanceView = new Scene(balancePane, 400, 450);
		depositView = new Scene(depBorderPane, 400, 450);
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
		private Screen currentScreen = Screen.LOGIN;
		private ObjectOutputStream out;
		private ObjectInputStream in;
		private Scanner sc = new Scanner(System.in);	
		private int cardId;
		private String pin;
		private float amt;
		private ClientRequest req;
		private ServerResponse res;
		public ProcessingThread(Socket socket) throws IOException {
			super();
			this.socket = socket;
		
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
		
		}


		@Override
		public void run() {
			try {
				goToScreen(currentScreen);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void goToScreen(Screen screen) throws IOException {
			switch (screen) {

			case LOGIN:
				txtUserID.textProperty().addListener(new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						if (!newValue.matches("\\d{0,12}?")) {
							txtUserID.setText(oldValue);
						}
					}
				});
				txtPin.textProperty().addListener(new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						if (!newValue.matches("\\d{0,4}?")) {
							txtPin.setText(oldValue);
						}
					}
				});
				btnSignIn.setOnAction(e -> {
					if (!(txtPin.getText().equals("") || txtUserID.getText().equals(""))) {
						cardId = Integer.parseInt(txtUserID.getText());
						int pintohash= Integer.parseInt(txtPin.getText());
						pin = PinEncryptionSHA1.SHA1(pintohash);
						
						req = ClientRequest.authenticate(cardId, pin);
						try {
							out.writeObject(req);
							processServerRes();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						errorMsg.setText("Wprowadz ID Karty oraz PIN!");
					}
				});
				btnLoginExit.setOnAction(e -> {
					req = ClientRequest.exitSession(-1);
					try {
						out.writeObject(req);
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
					Platform.exit();
				});
				break;
			case MAIN_MENU:
				window.setScene(mainMenuView);
				
				btnCheckBal.setOnAction(e -> {
					try {
						currentScreen = Screen.BALANCE_INQUIRY;
						goToScreen(currentScreen);
						req = ClientRequest.checkBalance(cardId);
						out.writeObject(req);
						processServerRes();
						lblBalance.setText(String.format("Twój stan Konta wynosi %.2f PLN", res.getUpdatedBalance()));
						lblAmt.setText("");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				});
				//przycisk wyp³acania pieniêdzy
				btnDeposit.setOnAction(e -> {
					try {
						currentScreen = Screen.DEPOSIT_PROMPT_AMOUNT;
						goToScreen(currentScreen);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				});
											
				
				//przycisk wyjœcia
				btnMainMenuExit.setOnAction(e -> {
					req = ClientRequest.exitSession(cardId);
					try {
						out.writeObject(req);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Platform.exit();
				});
				break;
			case DEPOSIT_RESULTS:
			case BALANCE_INQUIRY:
				window.setScene(balanceView);
				//przycisk menu g³ówne
				btnMain.setOnAction(e -> {
					try {
						currentScreen = Screen.MAIN_MENU;
						goToScreen(currentScreen);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				});
				//przycisk wyjdz z aplikacji z menu sprawdzenia salda
				btnBalInqClose.setOnAction(e -> {
					req = ClientRequest.exitSession(cardId);
					try {
						out.writeObject(req);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Platform.exit();
				});
				break;
			case DEPOSIT_PROMPT_AMOUNT:
				window.setScene(depositView);
				txtDeposit.setText("");
				depositError.setText("");
				txtDeposit.textProperty().addListener(new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
							txtDeposit.setText(oldValue);
						}
					}
				});				
				btnDepositCash.setOnAction(e -> {
					try {
						if (!txtDeposit.getText().equals("")) {
							amt = Float.parseFloat(txtDeposit.getText());
							req = ClientRequest.deposit(cardId,amt);
							out.writeObject(req);
							processServerRes();
							lblBalance.setText(String.format("Twoje Saldo po wp³acie Wynosi %.2f PLN", res.getUpdatedBalance()));
							lblAmt.setText(String.format("Operacja wp³acenia pieniêdzy powiod³¹ siê. Wp³aci³eœ %.2f PLN", amt));
							currentScreen = Screen.DEPOSIT_RESULTS;
							goToScreen(currentScreen);
						} else {
							currentScreen = Screen.DEPOSIT_PROMPT_AMOUNT;
							depositError.setText("Wprowadz kwotê któr¹ chcesz wp³aciæ!");
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				});
				btnDepMain.setOnAction(e -> {
					try {
						currentScreen = screen.MAIN_MENU;
						goToScreen(currentScreen);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				});
				break;
			}
		}

		private void processServerRes() {
			try {
				if ((res = (ServerResponse) in.readObject()) != null) {
					switch (req.getOperation()) {
					case AUTHENTICATE:
						if (!res.isOperationSuccess()) {
							currentScreen = Screen.LOGIN;
							errorMsg.setText(res.getErrorMessage());
						} else {
							currentScreen = Screen.MAIN_MENU;
							goToScreen(currentScreen);
						}
						break;					
					}
				}
			} catch (Exception ex) {

			}
		}
	

	

}
}
