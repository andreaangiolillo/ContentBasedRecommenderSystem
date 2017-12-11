import java.util.ArrayList;

import csv.CSVReader;
import lucene.Index;
import lucene.MyAnalyzer;
import source.GetSource;
import source.Source;
import twitter.TwittersUser;
import twitter.User;

public class Main {

	public static void main(String[] args) throws Exception {
		


/*Complete example with new example*/
		TwittersUser twitter = new TwittersUser();
		/*Create XML USER*/
		//twitter.createUser("BBCSport");
	
		
		/*Download Sources*/
//		GetSource source = new GetSource();
//		ArrayList<Source> entertainment = source.sendGet("entertainment");
//		ArrayList<Source> business = source.sendGet("business");
//		ArrayList<Source> gaming = source.sendGet("gaming");
//		ArrayList<Source> music = source.sendGet("music");
//		ArrayList<Source> scienceNature = source.sendGet("science-and-nature");
//		ArrayList<Source> sport = source.sendGet("sport");
//		ArrayList<Source> technology = source.sendGet("technology");
//		ArrayList<Source> general = source.sendGet("general");
//		System.out.println("fine get");
		
		Index index = new Index();
		
		/*Indexing sources*/
//		index.createDocFromSource(entertainment);
//		index.createDocFromSource(sport);
//		index.createDocFromSource(business);
//		index.createDocFromSource(gaming);
//		index.createDocFromSource(music);
//		index.createDocFromSource(scienceNature);
//		index.createDocFromSource(technology);
//		index.createDocFromSource(general);
//		
//		index.writerSource();
		
		
		/*Indixing User*/
//		index.indexUser("BBCSport");		
//		index.writerUser("BBCSport");

		/*Cousine Similarity*/
//		index.similarity("BBCSport");
//		index.similarityLucene("ftfinancenews");
	

		
		
		
	}

}
