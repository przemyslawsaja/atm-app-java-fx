package atm.server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MysqlAtmDatabase {
	

	public static Connection connectToDatabase(String kindOfDatabase, String adress,
			String dataBaseName, String userName, String password) {

		String baza = kindOfDatabase + adress + "/" + dataBaseName;
		java.sql.Connection connection = null;
		try {
			connection = DriverManager.getConnection(baza, userName, password);
		} catch (SQLException e) {			
		
		}
		return connection;
	}

}
