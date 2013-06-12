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

public class Creation {
	
	Random random = new Random();

	public int[] insertCat(){
		int indexarray = random.nextInt(7);
		while (indexarray==0)
			indexarray = random.nextInt(7);
		int indexCategory[]= new int[indexarray];
		for (int i=0; i<indexarray; i++){
			indexCategory[i] = random.nextInt(292);
			System.out.println("key array" + " " +indexCategory[i]);
		}
		return indexCategory;
	}
		
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
	
	public String [] merge(int [] key, Vector <String> dbCategories){
		String [] userCategories = new String [key.length];
		System.out.println("lunghezza" + " " + key.length);
		for (int i = 0; i<key.length; i++){
//			System.out.println("size" + " " +dbCategories.size());
			userCategories[i] = dbCategories.get(key[i]);	
//			System.out.println("valore" + " " +key[i]);
			
		}
		return userCategories;
	}

	public Vector<String> getEvents(String []userCategories)throws SQLException{
		DBConnection dbconn = new DBConnection();
		Connection conn = dbconn.getConnection();
		Vector <String> events = new Vector<String>();
		try{
			for (int i = 0; i < userCategories.length; i++){
			Statement stm = conn.createStatement();
			ResultSet rs = stm.executeQuery("SELECT idsevents FROM id_cat WHERE keyword ='"+userCategories[i]+"' ");
			while (rs.next()){
				String category = rs.getString("idsevents");
				String [] tmp = category.split(",");
				for (int j = 0; j<tmp.length; j++){
					events.add(tmp[random.nextInt(tmp.length)]);
				}
				}
			}
		}
		catch (SQLException e){
			System.out.println(e.getErrorCode());
		}finally{
			conn.close();
		}
		//System.out.println(events);
		return events;
	}
			
	public void writeFile(Vector <String> dbCategories) throws SQLException, IOException {
//		int round = 0;
		int i = 0;
		int maxUser = 9000;
		Creation creation = new Creation();
		File scriptSQL = new File("./scriptUserEventRate.sql");
		FileWriter fwsql = new FileWriter(scriptSQL);
		BufferedWriter bwsql = new BufferedWriter(fwsql);
		String stringSQL = "--DROP TABLE rating; \n CREATE TABLE rating (userid bigint, eventid bigint, rate real);\n";
		bwsql.write(stringSQL);
		//while (round <5){
		String stmt = null;
		String stmt2 = null;
//		File file = new File("./recommender"+round+".txt");
//		File file2 = new File("./user"+round+".txt");
		File file = new File("./recommender.txt");
		File file2 = new File("./user.txt");
		if (!(file.exists() || file2.exists())) {
			file.createNewFile();
			}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		BufferedWriter bw2 = new BufferedWriter(fw2);
		try {				
			stmt2 = "username" + "\t\t" + "category \n"; 
			bw2.write(stmt2);
			System.out.println("Starting to write into the File.");
			for (; i<maxUser; i++){
				String [] tmp = creation.merge(creation.insertCat(), dbCategories);
				Vector <String> tmpevents = creation.getEvents(tmp);
				Vector <String> events = new Vector<String>(new LinkedHashSet<String>(tmpevents));
				for (int count = 0; count <tmp.length; count ++){
					stmt2 = i + "\t" +"\t\t"+"\t"+ tmp[count] + "\n";
					bw2.write(stmt2);
					}
				double[] rate = {1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
				for (int j=0; j<events.size(); j++){
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
//		round++;
//		i = maxUser;
//		maxUser = maxUser+3000;
//		}
		bwsql.close();
	}
	
public static void main(String [] args)throws SQLException,IOException{
		Creation creationMain = new Creation();
		creationMain.writeFile(creationMain.dbCat());
	}
}
