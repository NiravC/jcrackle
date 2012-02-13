import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Connector {
	private Connection con;
	
	public Connector() throws ClassNotFoundException, SQLException{

	    Class.forName("org.postgresql.Driver");
			
		String connectionURL = "jdbc:postgresql://localhost:5432/crack";
			
		con = DriverManager.getConnection(connectionURL, "postgres", "pass");


	}
	
	public void query(String q) throws SQLException{
		
		Statement stmt = con.createStatement();
		stmt.executeUpdate(q);
		
	}
		
}

