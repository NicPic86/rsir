import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;


public class Recurrence {
	
	public void writeFileEvents()throws SQLException, IOException{		
		DBConnection dbconn = new DBConnection();
		Connection conn = dbconn.getConnection();
		int i=0;
		File fileEvents = new File("./listEventIDs.txt");
		FileWriter fwEvents = new FileWriter(fileEvents);
		BufferedWriter bwEvents = new BufferedWriter(fwEvents);
		try {
		Statement stm = conn.createStatement();
		ResultSet rs = stm.executeQuery("SELECT eventid FROM event");
		while (rs.next()){
			String eventid = rs.getString("eventid");
			String stringFile = eventid+";\n";
			bwEvents.write(stringFile);
			i=i+1;
			}
		}
		catch (SQLException e){
			System.out.println(e.getErrorCode());
		}finally{
			conn.close();
			bwEvents.close();
		}
	}

	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException, IOException {
		Recurrence recurrence = new Recurrence();
		System.out.println("Start writing file...");
		recurrence.writeFileEvents();
		System.out.println("File closed.");
	}

}
