import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB {

	public static Connection startDB() throws Exception{
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","username","password");	
		return conn;	
	}
}
