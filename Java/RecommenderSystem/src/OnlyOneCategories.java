import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Vector;

public class OnlyOneCategories {
	
	Random random = new Random();

	public Vector<String> dbCat()throws SQLException{
		DBConnection dbconn = new DBConnection();
		Connection conn = dbconn.getConnection();
		Vector <String> dbCategories = new Vector <String>();
		try{
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT keyword FROM ID_CAT");
			while (rs.next()){
				String category = rs.getString("keyword");
				dbCategories.add(category);
			}
		}
		catch (SQLException e){
			System.out.println(e.getErrorCode());
		}
		return dbCategories;
	}

	public Vector<String> getEvents()throws SQLException{
		DBConnection dbconn = new DBConnection();
		Connection conn = dbconn.getConnection();
		Vector <String> events = new Vector<String>();
		try{
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT idsevents FROM id_cat WHERE keyword ='wireless' ");
			while (rs.next()){
				String category = rs.getString("idsevents");
				String [] tmp = category.split(",");
				for (int j = 0; j<tmp.length; j++){
					events.add(tmp[random.nextInt(tmp.length)]);
				}
				}
		}
		catch (SQLException e){
			System.out.println(e.getErrorCode());
		}finally{
			conn.close();
		}
		return events;
	}
			
	public void writeFile(Vector <String> dbCategories) throws SQLException, IOException {
		int i = 990;
		int maxUser = 1200;
		OnlyOneCategories only = new OnlyOneCategories();
		File scriptSQL = new File("./scriptUserEventRateOnlyOne.sql");
		FileWriter fwsql = new FileWriter(scriptSQL);
		BufferedWriter bwsql = new BufferedWriter(fwsql);
		String stringSQL = "--DROP TABLE rating; \n CREATE TABLE onlyone (userid bigint, eventid bigint, rate real);\n";
		bwsql.write(stringSQL);
		String stmt = null;
		String stmt2 = null;
		File file = new File("./recommenderOnlyOne.txt");
		File file2 = new File("./userOnlyOne.txt");
		if (!(file.exists() || file2.exists())) {
			file.createNewFile();
			}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		BufferedWriter bw2 = new BufferedWriter(fw2);
		Vector <String> tmpevents = only.getEvents();
		Vector <String> events = new Vector<String>(new LinkedHashSet<String>(tmpevents));
		System.out.println(events.size());

		try {				
			stmt2 = "username" + "\t\t" + "category \n"; 
			bw2.write(stmt2);
			System.out.println("Starting to write into the File.");
			for (; i<maxUser; i++){
					stmt2 = i + "\t" +"\t\t"+"\t wireless \n";
					bw2.write(stmt2);
				double[] rate = {1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
				int maxevent = random.nextInt(23);
				for (int j=0; j<maxevent; j++){
						int index = random.nextInt(9);
						stmt = i+","+events.get(j)+","+rate[index]+"\n";	
						bw.write(stmt);
						stringSQL = "INSERT INTO rating (userid, eventid, rate) VALUES ("+i+","+events.get(j)+","+rate[index]+");\n ";
						bwsql.write(stringSQL);
					}
			}
		
		}finally{
        	bw.close();
        	bw2.close();
        	System.out.println("File filled correctly!");
			}
		bwsql.close();
	}
	
public static void main(String [] args)throws SQLException,IOException{
		OnlyOneCategories only = new OnlyOneCategories();
		only.writeFile(only.dbCat());
	}
}
