package atm.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MysqlAtmDatabase {

	public static Connection connectToDatabase(String kindOfDatabase, String adress, String dataBaseName,
			String userName, String password) {

		String baza = kindOfDatabase + adress + "/" + dataBaseName;
		java.sql.Connection connection = null;
		try {
			connection = DriverManager.getConnection(baza, userName, password);
		} catch (SQLException e) {

		}
		return connection;
	}

	public static Statement createStatement(Connection connection) {
		try {
			return connection.createStatement();
		} catch (SQLException e) {
			System.out.println("B³¹d createStatement! " + e.getMessage() + ": " + e.getErrorCode());
			System.exit(3);
		}
		return null;
	}

	public boolean authenticateCustomer(Statement s, long ID, String PIN) {

		String sql = "select * from atm_card where card_id='" + ID + "' and pin='" + PIN + "';";
		try {
			ResultSet rss = s.executeQuery(sql);
			if (rss.next() == false)// sprawdza czy zapytanie z podanymi wy¿ej warunkami id oraz Pin zwróci³o
									// u¿ytkownika
			{
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
			return false;
		}

	}

	public Customer setCustomerFromDatabase(Statement s, long ID) {

		String sql = "select customer.* from customer inner join bank_account on bank_account.customer_id=customer.id inner join atm_card on "
				+ "atm_card.account_number=bank_account.account_number where card_id='" + ID + "';";

		try {
			ResultSet rss = s.executeQuery(sql);
			if (rss.next() != false) {

				int customerId = rss.getInt("id");
				String firstname = rss.getString("firstname");
				String surname = rss.getString("surname");
				String street = rss.getString("street");
				String postcode = rss.getString("postcode");
				String city = rss.getString("city");
				int pesel = rss.getInt("id");
				String phone = rss.getString("phone");
				String email = rss.getString("email");
				Customer customer = new Customer(customerId, firstname, surname, street, postcode, city, pesel, phone,
						email);
				return customer;
			} else {
				return null;
			}

		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
			return null;
		}

	}

	public BankAccount setBankAccountFromDatabase(Statement s, long ID) {

		String sql = "select bank_account.* from bank_account inner join atm_card on "
				+ "atm_card.account_number=bank_account.account_number where card_id='" + ID + "';";

		try {
			ResultSet rss = s.executeQuery(sql);
			if (rss.next() != false) {

				String accountNumber = rss.getString("account_number");
				String accountType = rss.getString("account_type");
				float balance = rss.getFloat("balance");
				BankAccount bankAccount = new BankAccount(accountNumber, accountType, balance);
				return bankAccount;
			} else {
				return null;
			}

		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
			return null;
		}

	}

	public AtmCard setAtmCardFromDatabase(Statement s, long ID) {

		String sql = "select * from atm_card where card_id='" + ID + "';";

		try {
			ResultSet rss = s.executeQuery(sql);
			if (rss.next() != false) {
				String pin = rss.getString("pin");
				int cvvCode = rss.getInt("cvv_code");
				String type = rss.getString("type");
				AtmCard atmCard = new AtmCard(ID, pin, cvvCode, type);
				return atmCard;
			} else {
				return null;
			}

		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
			return null;
		}
	}

	public float getAccountBalance(Statement s, String accountNumber) {
		String sql = "select * from bank_account where account_number='" + accountNumber + "';";

		try {
			ResultSet rss = s.executeQuery(sql);
			if (rss.next() != false) {
				float balance = rss.getFloat("balance");
				return balance;
			} else {
				return -1;
			}

		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
			return -1;
		}
	}

	public static void balanceUpdate(Statement s, float balance, String accountNumber) {
		try {
			String sql = "update bank_account set balance=" + balance + " where account_number=" + accountNumber + ";";
			s.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
		}

	}

	public static void registerWithdraw(Statement s, float amount, String accountNumber) {
		try {
			String sql = "INSERT INTO operation_history(amount, details, account_number) VALUES (" + (-amount)
					+ ", 'wyp³ata','" + accountNumber + "');";
			s.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
		}

	}

	public static void registerDeposit(Statement s, float amount, String accountNumber) {
		try {
			String sql = "INSERT INTO operation_history(amount, details, account_number) VALUES (" + amount
					+ ", 'wp³ata','" + accountNumber + "');";
			s.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
		}

	}

	public static ArrayList<OperationHistory> getOperationHistory(Statement s, String accountNunber) {
		ArrayList<OperationHistory> operationHistoryList = new ArrayList();

		String sql = "select operation_date, amount, details from operation_history where account_number='"
				+ accountNunber + "' order by operation_date desc limit 10;";

		try {
			ResultSet rss = s.executeQuery(sql);
			while (rss.next()) {
				String operation_date = rss.getTimestamp("operation_date").toString();
				float amount = rss.getFloat("amount");
				String details = rss.getString("details");
				operationHistoryList.add(new OperationHistory(operation_date, amount, details));
			}
			
			return operationHistoryList;
		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
			return null;
		}
		
		

	}

}
