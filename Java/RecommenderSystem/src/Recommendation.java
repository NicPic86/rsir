import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import java.util.NoSuchElementException;


public class Recommendation {
	
	DataModel model;
	DBConnection dbconn = new DBConnection();
	int userid = 1041;

	public void itemSimilarity() throws TasteException, IOException, SQLException{
		File scriptSQL = new File("./itemRec.sql");
		FileWriter fwsql = new FileWriter(scriptSQL);
		BufferedWriter bwsql = new BufferedWriter(fwsql);
		String stringSQL = "--DROP TABLE recitem; \n CREATE TABLE recitem (userid bigint, eventid text);\n";
		bwsql.write(stringSQL);
		File file = new File("./recommenderItem.txt");
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		model = new FileDataModel(new File("./recomm2.txt"));
		String stmt = null;
		String s ="";

		ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(model);
		Recommender recommender = new GenericItemBasedRecommender(model, itemSimilarity);
		Recommender cachingRecommender = new CachingRecommender(recommender);
		List<RecommendedItem> recommendations = cachingRecommender.recommend(userid, 10);
		for (RecommendedItem recommendedItem : recommendations) {
			s = s + "," + String.valueOf(recommendedItem.getItemID());
			stmt = userid+","+String.valueOf(recommendedItem.getItemID())+ "\n";	
			bw.write(stmt);
		}
		stringSQL = "INSERT INTO recitem (userid, eventid) VALUES ("+userid+",'"+s+"');\n ";
		bwsql.write(stringSQL);
		bw.close();
		bwsql.close();
	}
	
	public void euclideanSimilarity() throws TasteException, IOException, SQLException{
		File scriptSQL = new File("./euclRec.sql");
		FileWriter fwsql = new FileWriter(scriptSQL);
		BufferedWriter bwsql = new BufferedWriter(fwsql);
		String stringSQL = "--DROP TABLE euclitem; \n CREATE TABLE euclitem (userid bigint, eventid text);\n";
		bwsql.write(stringSQL);
		File file = new File("./recommenderEuclidean.txt");
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		model = new FileDataModel(new File("./recomm2.txt"));
		String stmt = null;
		String s ="";
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder(){
		public Recommender buildRecommender(DataModel model) throws TasteException{
		ItemSimilarity similarity = new EuclideanDistanceSimilarity(model);
		System.out.println(similarity);
		return new GenericItemBasedRecommender(model,similarity);}
		};
		Recommender recommender = recommenderBuilder.buildRecommender(model);
		List<RecommendedItem> recommendations = recommender.recommend(userid, 10);
		for (RecommendedItem recommendedItem : recommendations) {
			s = s + "," + String.valueOf(recommendedItem.getItemID());
			stmt = userid+","+String.valueOf(recommendedItem.getItemID())+ "\n";	
			bw.write(stmt);
		}
		stringSQL = "INSERT INTO euclitem (userid, eventid) VALUES ("+userid+",'"+s+"');\n ";
		bwsql.write(stringSQL);
		bw.close();
		bwsql.close();
	}
	
	public void userSimilarity() throws TasteException, IOException, SQLException{
		File scriptSQL = new File("./userRec.sql");
		FileWriter fwsql = new FileWriter(scriptSQL);
		BufferedWriter bwsql = new BufferedWriter(fwsql);
		String stringSQL = "--DROP TABLE recuser; \n CREATE TABLE recuser (userid bigint, eventid text);\n";
		bwsql.write(stringSQL);
		File file = new File("./recommenderUser.txt");
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		model = new FileDataModel(new File("./recomm2.txt"));
		String stmt = null;
		String s ="";
		UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, userSimilarity, model);
		Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
		Recommender cachingRecommender = new CachingRecommender(recommender);
		List<RecommendedItem> recommendations = cachingRecommender.recommend(userid, 10);
		for (RecommendedItem recommendedItem : recommendations) {
			s = s + "," + String.valueOf(recommendedItem.getItemID());
			stmt = userid+","+String.valueOf(recommendedItem.getItemID())+ "\n";	
			bw.write(stmt);
		}
		stringSQL = "INSERT INTO recuser (userid, eventid) VALUES ("+userid+",'"+s+"');\n ";
		bwsql.write(stringSQL);
		bw.close();
		bwsql.close();
	}
	
	public void slope() throws TasteException, IOException, SQLException{
		File scriptSQL = new File("./slopeRec.sql");
		FileWriter fwsql = new FileWriter(scriptSQL);
		BufferedWriter bwsql = new BufferedWriter(fwsql);
		String stringSQL = "--DROP TABLE recslope; \n CREATE TABLE recslope (userid bigint, eventid text);\n";
		bwsql.write(stringSQL);
		File file = new File("./recommenderSlope.txt");
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		model = new FileDataModel(new File("./recommenderOnlyOne.txt"));
		String stmt = null;
		String s = "";
		CachingRecommender cachingRecommender = new CachingRecommender(new SlopeOneRecommender(model));
		List<RecommendedItem> recommendations = cachingRecommender.recommend(userid, 10);
		for (RecommendedItem recommendedItem : recommendations) {
					s = s + "," + String.valueOf(recommendedItem.getItemID());
					stmt = userid+","+String.valueOf(recommendedItem.getItemID())+ "\n";	
					bw.write(stmt);
		}
		stringSQL = "INSERT INTO recslope (userid, eventid) VALUES ("+userid+",'"+s+"');\n ";
		bwsql.write(stringSQL);
		bw.close();
		bwsql.close();
	}
	
	public static void main(String args[]) throws TasteException, SQLException, IOException, NoSuchElementException {
		Recommendation recommend = new Recommendation();
		recommend.slope();
		Recommendation recommendEU = new Recommendation();
		recommendEU.euclideanSimilarity();
		Recommendation recommendIT = new Recommendation();
		recommendIT.itemSimilarity();
		Recommendation recommendUS = new Recommendation();
		recommendUS.userSimilarity();
	}
}
