import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


public class Recommendation {
	
	public static void main(String args[]) throws TasteException, SQLException, IOException {
	//	DBConnection db = new DBConnection();
		Recommendation rec = new Recommendation();
	//	rec.fillDatabase();
		DataModel model = new FileDataModel(new File("./test.txt"));
	//	PostgreSQLJDBCDataModel model = new PostgreSQLJDBCDataModel(db, "recommendation", "userid", "eventid", "rating", null);
	//	DataModel model = new FileDataModel(new File("./matrix.txt"));
	//	CachingRecommender cachingRecommender = new CachingRecommender(new SlopeOneRecommender(model));
		UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, userSimilarity, model);
		Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
		Recommender cachingRecommender = new CachingRecommender(recommender);
		List<RecommendedItem> recommendations = cachingRecommender.recommend(460, 100);
		for (RecommendedItem recommendedItem : recommendations) {
			System.out.println(recommendedItem);
		}
	}
	
	public void fillDatabase() throws SQLException, IOException {
		String stmt = null;
		Random r = new Random();
		File file = new File("./matrix.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		try {					
			System.out.println("Starting to fill database. 50 events per 50 users.");
			for (int i = 0; i<15000; i++){
				for (int j=0; j<54664; j++ ){
					double[] rate = {0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
					int index = r.nextInt(11);
					int breakhere = r.nextInt(5);
					if (breakhere == 3)
						break;
					stmt = i+","+j+","+rate[index]+"\n";					
					bw.write(stmt);
					
				/*	stmt = "INSERT INTO recommendation(userid, eventid, rating) VALUES(?, ?, ?)";
					pst = conn.prepareStatement(stmt);
		            pst.setInt(1, i);
		            pst.setInt(2, j);    
		            pst.setDouble(3, rate[index]);
		            pst.executeUpdate();*/
				}
				System.out.println("User #"+i+" completed.");
			}
		} catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
        	//pst.close();
        	bw.close();
        	System.out.println("Database filled correctly!");
        }
	}

}
