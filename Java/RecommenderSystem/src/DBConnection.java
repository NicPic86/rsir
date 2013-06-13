import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;


public class DBConnection implements DataSource {

	private static Connection conn = null;	
	
	@Override
	public Connection getConnection() throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
		//	conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/rsir1213_rsir", "postgres","igor");
			conn = DriverManager.getConnection("jdbc:postgresql://stevie.heliohost.org:5432/rsir1213_rsir", "rsir1213","rsir");

		} catch (ClassNotFoundException e) {	
			e.printStackTrace();
		}		
		return conn;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
