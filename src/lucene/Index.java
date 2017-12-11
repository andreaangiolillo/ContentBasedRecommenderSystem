


/*The index stores statistics about terms in order to make term-based search more efficient.
 *  Lucene's index falls into the family of indexes known as an inverted index. 
 *  This is because it can list, for a term, the documents that contain it. 
 *  This is the inverse of the natural relationship, in which documents list terms.*/

package lucene;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.math3.linear.OpenMapRealVector;

import org.apache.commons.math3.linear.RealVectorFormat;
import org.apache.commons.math3.linear.SparseRealVector;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;

import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.fst.Builder;


import source.Source;
import twitter.User;

	public class Index {
		private ArrayList<Document> sources;
		private ArrayList<Document> users;
		private ArrayList<String>  idSource;
		private ArrayList<String>  idUser;
		private FieldType fieldtype;
		private DirectoryReader readerSource;
		private DirectoryReader readerUser;
		private Path sourcePath;
		private Path userPath;
		
		/***Constructor****/
	public Index() {
			this.sources = new ArrayList<Document>();
			this.users = new ArrayList<Document>();
			this.idSource = new ArrayList<String>();
			this.idUser = new ArrayList<String>();
			this.fieldtype = new FieldType();
			this.readerSource = null;
			this .readerUser = null;
			this.userPath = null;
			this.sourcePath = null;
			
			fieldtype.setTokenized(true);
			fieldtype.setStored(true);
			fieldtype.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
			fieldtype.setStoreTermVectorOffsets(true);
			fieldtype.setStoreTermVectors(true);
	
		}
	
	

	public void inizializationReader(String username) throws IOException{
		/*Source*/
		File index = new File("otherFile/indexDirectory");
		sourcePath = Paths.get(index.getCanonicalPath());
		Directory directory = FSDirectory.open(sourcePath);  
		readerSource = DirectoryReader.open(directory);
		
		/*Source*/
		File index1 = new File("otherFile/indexDirectoryUser/"+username);
		userPath = Paths.get(index1.getCanonicalPath());
		Directory directory1 = FSDirectory.open(userPath);  
		readerUser = DirectoryReader.open(directory1);
		
	}
	
	


	public void createDocFromSource (ArrayList<Source> source){
		
		
		Field id;
		Field name;
		Field description;
		Field url;
		Field category;
		Field language;
		Field country;
		Document[] d = new Document[source.size()];
		int i = 0;
		for (Source s : source){
			idSource.add(s.getId());
			
			id = new TextField("id",s.getId(),Field.Store.YES);
			name = new TextField("name",s.getName(),Field.Store.YES);
			description = new Field("description",s.getDescription(), fieldtype);
			url = new TextField("url",s.getUrl(),Field.Store.YES);
			category = new Field("category",s.getCategory(),fieldtype);
			language = new TextField("language",s.getLanguage(),Field.Store.YES);
			country = new TextField("country",s.getCountry(),Field.Store.YES);
		
			
			d[i] = new Document();
			d[i].add(id);
			d[i].add(name);
			d[i].add(description);
			d[i].add(url);
			d[i].add(category);
			d[i].add(language);
			d[i].add(country);
			
			sources.add(d[i]);
			i++;
		}
		
		
	}
	
	public User getUserXML(String username) throws JAXBException{
		/*gets user*/
		File file = new File("otherFile/users/"+username+".xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(User.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		User user = (User) jaxbUnmarshaller.unmarshal(file);
		return user;
	}
	
	public void indexUser(String username) throws ParseException, IOException, JAXBException{
		
		
		User user = getUserXML(username);
		
			
		idUser.add(user.getId());
		
		Document d = new Document();
		Field id = new TextField("id", user.getId(), Field.Store.YES);
		Field name = new TextField("name",user.getName(),Field.Store.YES);
		Field location = new TextField("location",user.getName(),Field.Store.YES);
		Field description = new Field("description", user.getDescription(),fieldtype);
		

		String f = "";
		
		/*Tweets*/
		f = user.getTweets();
		
		
		f = f.toLowerCase();
		f = f.replaceAll("[^a-zA-Z0-9_ # @ . ; \n]" , "");
		f = f.replaceAll("(https)[a-z-z0-9 # @ \\%\\' . ;]*" , "");
		//f = f.replaceAll("(rt)[a-z-z0-9 # @ \\%\\' . ;]*" , "");
		f = f.replaceAll("(#)[a-z-z0-9 # @ \\%\\' . ;]*" , "");
		f = f.replaceAll("(@)[a-z-z0-9 # @ \\%\\' . ;]*" , "");
		Field tweets = new Field("tweets", f, fieldtype);
		tweets.setBoost(1.6f); // set Boost
		//System.out.println("\n F: \n" + f);
		
	
		/*getTweetsFriends*/

		f = "";
		
		for (String s : user.getTweetsFriends()){
			f = f + "\n" + s + "\n"; 
		}
		
		
//		System.out.print("before + n following:"+ user.getTweetsFollowing().size() +"\n:" +f);
		
		f = f.toLowerCase();
		f = f.replaceAll("[^a-zA-Z0-9_ # @ . ; \n]" , "");
		f = f.replaceAll("(https)[a-z-z0-9 # @ \\%\\' . ;]*" , "");
		//f = f.replaceAll("(rt)[a-z-z0-9 # @ \\%\\' . ;]*" , "");
		f = f.replaceAll("(#)[a-z-z0-9 # @ \\%\\' . ;]*" , "");
		f = f.replaceAll("(@)[a-z-z0-9 # @ \\%\\' . ;]*" , "");
		Field tweetsFriends = new Field("tweetsFriends", f, fieldtype);
		tweetsFriends.setBoost(1.3f); // set Boost
		
		/*tweetsFollowing*/
		f = "";
		
		for (String s : user.getTweetsFollowing()){
			f = f + "\n" + s + "\n"; 
		}
		
		


		f = f.toLowerCase();
		f = f.replaceAll("[^a-zA-Z0-9_ # @ . ; \n]" , "");
		f = f.replaceAll("(https)[a-z-z0-9 # @ \\%\\' . ;]*" , "");
		//f = f.replaceAll("(rt)[a-z-z0-9 # @ \\%\\' . ;]*" , "");
		f = f.replaceAll("(#)[a-z-z0-9 # @ \\%\\' . ;]*" , "");
		f = f.replaceAll("(@)[a-z-z0-9 # @ \\%\\' . ;]*" , "");

		Field tweetsFollowing = new Field("tweetsFollowing", f, fieldtype);
		
		d.add(id);
		d.add(tweetsFollowing);
		d.add(tweetsFriends);
		d.add(tweets);

		d.add(location);
		d.add(name);
	
		d.add(description);
		
		users.add(d);
		
	}
	
	
	
	public void writerSource() throws IOException{
		String textPath = "otherFile/indexDirectory";
		Path indexDirectoryPath = Paths.get(textPath);
		Directory directory = FSDirectory.open(indexDirectoryPath); 
		MyAnalyzer analyzer = new MyAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);
		
		for (Document d :sources){
			iwriter.addDocument(d);
		}
		iwriter.close();
		
	}
	
	
	public void writerUser(String username) throws IOException{
		/*create folder */ 
		boolean success = (new File("otherFile/indexDirectoryUser/"+username)).mkdirs();
		if (!success) {
		    
			System.out.println("Error- User: "+username+" - Directory");
		}
		
		String textPath = "otherFile/indexDirectoryUser/"+ username;
		Path indexDirectoryPath = Paths.get(textPath);
		Directory directory = FSDirectory.open(indexDirectoryPath); 
		MyAnalyzer analyzer = new MyAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);
		
		for (Document d :users){
			iwriter.addDocument(d);
		}
		iwriter.close();
		
	}
	
	
	
	/*
	 * During runtime, look up the term frequency vectors for both documents 
	 * using IndexReader.getTermFreqVector(), and look up document frequency 
	 * data for each term using IndexReader.docFreq(). 
	 * That will give you all the components necessary to calculate the 
	 * cosine similarity between the two docs.
	 * 
	 * */
	public String similarity(String user) throws IOException{
		
		
		inizializationReader(user);		
		
		/*STEP1*/
	    /*first find all terms in the index*/
	    Map<String,Integer> terms = allTerms();
	   
        /*STEP2 - frequency - tfvs */ 
        ArrayList<Integer> sourceIds = getdocIdList(sourcePath.toString());

        
        ArrayList<String> fs = new ArrayList<String>();
        fs.add("description");
        fs.add("category");
        
       DocVector[] docs = new DocVector[sourceIds.size()];
       int i = 0;
        for (int sourceId : sourceIds){
        	docs[i] = new DocVector(terms);
        	for(String f : fs){
        		 	
		        Terms vector = readerSource.getTermVector(sourceId, f);
		        TermsEnum termsEnum = null;
		        termsEnum = vector.iterator();
	
		        BytesRef text = null;
		        
		        
		        
		        while ((text = termsEnum.next()) != null) {
		            String term = text.utf8ToString();
		            int freq = (int) termsEnum.totalTermFreq();
	
		            docs[i].setEntry(term, freq);
		 
		        }
        	}
//	        System.out.println(frequencies.toString());
	        docs[i].normalize();
	        i++;
        }
        
        
      
        
        
        DocVector utente;
        ArrayList<String> fls = new ArrayList<String>();
        fls.add("tweets");
        fls.add("description");
        fls.add("tweetsFriends");
        fls.add("tweetsFollowing");

        utente = new DocVector(terms);
        Terms vector ;
        TermsEnum termsEnum = null;
        BytesRef text;
       
		for(String fl : fls){
			vector = readerUser.getTermVector(0, fl);  
		
			System.out.println(fl);
			termsEnum = vector.iterator();
			text = null;
	        while ((text = termsEnum.next()) != null) {
	            String term = text.utf8ToString();
	            int freq = (int) termsEnum.totalTermFreq();

	            utente.setEntry(term, freq);
	 
	        }
			
		}
        
        
      
        
        
        /*STEP3*/
        Map<Integer, Double> cosine = new HashMap<>();
        
        for (int j = 0 ; j < docs.length ; j++){
        	  double cosim00 = getCosineSimilarity(utente, docs[j]);
              //System.out.println("cosim(Utente," +j +")=" + cosim00);
              cosine.put(j, cosim00);
        	
        }
        
        
        
        
        cosine = sortByValue(cosine);
        
        /*PRINT*/
        int c = 1;
        String out = "";
        Document doc;
        String category = "";
        String name = "";
        Iterator it = cosine.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            
            doc = readerSource.document((int) pair.getKey());
            category = doc.getField("category").stringValue();
            name = doc.getField("name").stringValue();
            out = out + "\n" + c + " Title: " + name +  ", Category: " + category + ", Cosine Similarity: "+ pair.getValue();
            System.out.println(c + " Title: " + name +  ", Category: " + category + ", Cosine Similarity: "+ pair.getValue());
            c ++;
            
            it.remove(); // avoids a ConcurrentModificationException
        }    
        
		return out;
	}
	
	
	
	
  public String similarityLucene(String user) throws IOException{
		  
	  inizializationReader(user);		
		
	  
		/*STEP1*/
	    /*first find all terms in the index*/
	  Map<String,Integer> terms = allTerms();
	   
      /*STEP2 - frequency - tfvs */ 
      ArrayList<Integer> sourceIds = getdocIdList(sourcePath.toString());
	  
	  
	  ArrayList<String> fields = new ArrayList<String>();
	  fields.add("description");
	  fields.add("category");
	  
	  BytesRef text = null;
	  TermsEnum termsEnum = null;
	  bagOfWords[] docs = new bagOfWords[sourceIds.size()];
	  int i = 0;
	  for (int sourceId : sourceIds){
		  docs[i] = new bagOfWords(terms);
		  for(String field : fields){
			  Terms vector = readerSource.getTermVector(sourceId, field);
		      
		      termsEnum = vector.iterator();
		      
		              
		      while ((text = termsEnum.next()) != null) {
		    	  String term = text.utf8ToString();
		          int freq = (int) termsEnum.totalTermFreq();// this only return frequency in this doc
	
		          docs[i].setEntry(term, freq, field);
		         // System.out.println("doc"+ i + ": " + term + " freq: " + freq);
		      }
	//	        System.out.println(frequencies.toString());
		      
		  }
		  i++;
	  }
        
      fields.clear();
 	  fields.add("tweets");
	  fields.add("description");
	  fields.add("tweetsFriends");
	  fields.add("tweetsFollowing"); 
	  double b1 =1;
	  bagOfWords utente = new bagOfWords(terms);
	  Map<String, Double> boost = new HashMap<String,Double>();
	  for(String field : fields){
	        Terms vector = readerUser.getTermVector(0, field);
	  
	        termsEnum = vector.iterator();
        
	        while ((text = termsEnum.next()) != null) {
	            String term = text.utf8ToString();
	            int freq = (int) termsEnum.totalTermFreq();

	            utente.setEntry(term, freq, field);
	            if (field.equalsIgnoreCase("tweets") == true || field.equalsIgnoreCase("description") == true){
	            	b1 = 1.6;
	            }else if(field.equalsIgnoreCase("tweetsFriends") == true ){
	            	b1 = 1.3;
	            }
	            boost.put(term, b1);
	            b1 = 1;
	        }
//	        System.out.println(frequencies.toString());

  	}
	  
	  
	  Map<Integer, Double> cosine = new HashMap<>();
	  Set<String> q = utente.vector.keySet();
	 // System.out.println("q: " +q.size() +" docs: " + docs.length );
	  
	  double b = 0;
	  double sum = 0;
	  String field = "";
	  for (int j = 0; j < docs.length; j++){
		 // if ( j == 5 || j == 39){
			  for(String t : q){
				  b = boost.get(t);
				  field = docs[j].termField.get(t);
				  if(field != null){ 
				  //System.out.println("boost: " +b +" field: " + field );
				  
				  sum = sum + (tf(t,docs[j]) * Math.pow(idf(t,docs), 2) * b * norm(field,j) ); // e' sbagliato!
				  //System.out.println("t: " + t + " doc: "+j +" sum: " + sum );
				  }
			  }
			  cosine.put(j, coord(utente, docs[j]) * queryNorm(docs) * sum);
			// System.out.println("j: " + j + " coord: " + coord(utente, docs[j]) + " queryNorm(docs) : " + queryNorm(docs)  + " sum: " + sum );
			  sum = 0;
		//  }
		  
	  }
	  cosine = sortByValue(cosine);
	//  System.out.println(cosine.toString());

	 
	    
      /*PRINT*/
	  String out = "";
      Document doc;
      String category = "";
      String name = "";
      Iterator it = cosine.entrySet().iterator();
      int cont = 0;
      while (it.hasNext()) {
          Map.Entry pair = (Map.Entry)it.next();
          cont ++;
          doc = readerSource.document((int) pair.getKey());
          category = doc.getField("category").stringValue();
          name = doc.getField("name").stringValue();
          System.out.println(cont + " Title: " + name +  ", Category: " + category + ", Cosine Similarity: "+ pair.getValue());
          out = out + "\n" + cont + " Title: " + name +  ", Category: " + category + ", Cosine Similarity: "+ pair.getValue();
          
          it.remove(); // avoids a ConcurrentModificationException
      }  
	  
      return out;
  
  }
	

  
  public Map<String,Integer> allTerms() throws IOException{
	  /*STEP1*/
	    /*first find all terms in the index*/
	    Map<String,Integer> terms = new HashMap<String,Integer>();

	    //Sources
	    int count = 0;
	    Fields fields = MultiFields.getFields(readerSource);
      for (String field : fields) {
          Terms t = fields.terms(field);
          TermsEnum termsEnum = t.iterator();
          while (termsEnum.next() != null) {
          	BytesRef term = termsEnum.term();
              
          	if (terms.containsKey(term.utf8ToString()) == false){		
                  terms.put(term.utf8ToString(), count);
                  count++;
          	}
          	
//              System.out.println(count);
//              System.out.println(term.utf8ToString());
          }
//          System.out.println(count);
      }
      
      //User
	    Fields fields1 = MultiFields.getFields(readerUser);
      for (String field : fields1) {
          Terms t1 = fields1.terms(field);
          TermsEnum termsEnum1 = t1.iterator();
          while (termsEnum1.next() != null) {
          	BytesRef term = termsEnum1.term();
          	
          	if (terms.containsKey(term.utf8ToString()) == false){
          		  
                    terms.put(term.utf8ToString(), count);
                    count++;
          	}
            
//              System.out.println(count);
//              System.out.println(term.utf8ToString());
          }
//          System.out.println(count);
      }
      
//	    System.out.println(terms.toString());
//	    System.out.println("\n "+ terms.size());
	 
      
	  return terms;
	  
	  
  }
	
	/*Sort Map*/
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}
	

	public ArrayList<Integer> getdocIdList(String path ) throws IOException{
		
		
		File index = new File(path);
		Path p = Paths.get(index.getCanonicalPath());
		Directory directory = FSDirectory.open(p);  
		DirectoryReader readerSource = DirectoryReader.open(directory);
		
		
		Query query = new MatchAllDocsQuery();
			
		
		MatchAllDocsQuery everyDocClause = new MatchAllDocsQuery();
		TermQuery termClause = new TermQuery(new Term("category", " "));
		
		
		BooleanQuery.Builder q = new BooleanQuery.Builder();
		q.add(everyDocClause, BooleanClause.Occur.MUST);
		q.add(termClause, BooleanClause.Occur.MUST_NOT);
		BooleanQuery bool = q.build();

		IndexSearcher serc = new IndexSearcher(readerSource);
		
		TopDocs topDocs = serc.search(bool,100);
		ScoreDoc[] score = topDocs.scoreDocs;
		
		//System.out.println("n of sources: " + sources.size());
		
		ArrayList<Integer> docId = new ArrayList<Integer>();
		
		for (ScoreDoc d : score){
			docId.add(d.doc);
			//System.out.println(d.doc);
			
		}
		
		return docId;
	}
	
	
	
	private class DocVector {
	    public Map<String,Integer> terms;
	    public SparseRealVector vector;
	    
	    public DocVector(Map<String,Integer> terms) {
	      this.terms = terms;
	      this.vector = new OpenMapRealVector(terms.size());
	    }
	    
	    public void setEntry(String term, double freq) {
	      if (terms.containsKey(term)) {
	        int pos = terms.get(term);
	        //System.out.println("terms.size: " + terms.size() + " pos: " + pos);
	        vector.setEntry(pos, (double) freq);
	      }
	    }
	    
	    public void normalize() {
	      double sum = vector.getL1Norm();
	      vector = (SparseRealVector) vector.mapDivide(sum);
	    }
	    
	    
	    public String toString() {
	      RealVectorFormat formatter = new RealVectorFormat();
	      return formatter.format(vector);
	    }
	  }
	
	
	
	private class bagOfWords{
		  public Map<String,Integer> terms;
		  public Map<String,Integer> vector;
		  public Map<String, String> termField;
		  
		  public bagOfWords(Map<String,Integer> terms){
			  vector = new HashMap<String, Integer>();
			  termField = new HashMap<String, String>();
			  this.terms = terms;
		  }
		  
		  public void setEntry(String term, double freq, String field) {
		      if (terms.containsKey(term) == true) {
		    	//  System.out.println(term);
		    	  vector.put(term,(int) freq);
		    	  termField.put(term, field);
		      }
		    }
		  
		  
	}

	
	
	  private double getCosineSimilarity(DocVector d1, DocVector d2) {
		    return (d1.vector.dotProduct(d2.vector)) /
		      (d1.vector.getNorm() * d2.vector.getNorm()); //Returns the L2 norm of the vector. The L2 norm is the root of the sum of the squared elements.
		  }
	
	
	  
	
	  /*Implementation: sqrt(freq) 
		Implication: the more frequent a term occurs in a document, the greater its score
		Rationale: documents which contains more of a term are generally more relevant
	   * */
	  
	  public double tf(String term, bagOfWords doc) throws IOException{
		  
		  if (doc.vector.containsKey(term) == true){
			  //System.out.println("term: " + term + " tf: " + Math.sqrt(doc.vector.get(term)));
			  return Math.sqrt(doc.vector.get(term));
		  }
		  
		  return 0;
		
	  }
	  
	  /*https://lucene.apache.org/core/4_0_0/core/org/apache/lucene/search/similarities/TFIDFSimilarity.html#formula_norm*/
	  
	  /*    
	   * docFreq - the number of documents which contain the term
	   * numDocs - the total number of documents in the collection
	   * 
	   * Implementation: log(numDocs/(docFreq+1)) + 1
			Implication: the greater the occurrence of a term in different documents, the lower its score 
			Rationale: common terms are less important than uncommon ones
	   */
	  public double idf(String term, bagOfWords[] docs) throws IOException{
		  
		  double docFreq = 0;
		//  System.out.println(docFreq + " " + term);
		  double numDocs = readerSource.numDocs();
		  
		  int pos = 0;
		  if (docs[0].terms.containsKey(term)) {
			  for(int i = 0; i < docs.length; i++){
				  if(docs[i].vector.containsKey(term)){
					  docFreq++;
				  }
			  }
			  return (Math.log(numDocs/(docFreq+1)) + 1);
		  }else{
		  
		  System.out.println("idf - error");
		  return 0;
		  }
		  
		  
		  
	  }
	  
	  
	  /*
	   *overlap - the number of query terms matched in the document
	   *maxOverlap - the total number of terms in the query 
	   *return a score factor based on term overlap with the query
	   *
	   *Implementation: overlap / maxOverlap
		Implication: of the terms in the query, a document that contains more terms will have a higher score
		Rationale: self-explanatory
	   *
	   * */
	  public double coord (bagOfWords user, bagOfWords doc/*int docid*/) throws IOException{
		  
		 double maxOverlap = user.vector.size();
//		 System.out.println("maxOverlap: " + maxOverlap);
		 double overlap = 0;
		 Set<String> terms = user.vector.keySet();
//		 System.out.println("terms.lenght: " + terms.size());
//		 System.out.println("docs.lenght: " + doc.vector.size());
		for(String term : terms){
			if (doc.vector.containsKey(term) == true){
				overlap ++;
				
			}
		}
		  
		return overlap/maxOverlap; 
	
	  }
	  
	  
	  /*
	   * 
	   * @input fieldname String, Field of term
	   * @input 
	   * norm(t,d)   =   lengthNorm  ·  	∏ 	f.boost() 
	   * */
	  
	  public double norm(String fieldname, int idSource) throws IOException{

	
		  List<IndexableField> fields = readerSource.document(idSource).getFields();
		  
		  double docBoost = 1;
		  
		  for (IndexableField f : fields){
			  docBoost = docBoost * f.boost();
	
		  }
		  
		  
		//  System.out.println("norm-- fieldname: " + fieldname);
			 
		  Terms vector = readerSource.getTermVector(idSource, fieldname);
		  Set<String> terms = new HashSet<String>(); // no duplicate
		  TermsEnum termsEnum = vector.iterator();
		  BytesRef text = null;
		  while ((text = termsEnum.next()) != null) {
			  terms.add(text.utf8ToString());
		  }
		  
		  
		
		  double numTerm = terms.size();
		 
		  /*
		   * 4. lengthNorm
		   *	Implementation: 1/sqrt(numTerms)
		   *	Implication: a term matched in fields with less terms have a higher score
		   *	Rationale: a term in a field with less terms is more important than one with more
		   * 
		   * */
		  double lengthNorm = (1.0 / Math.sqrt(numTerm));
		  //System.out.println("norm-- fieldname: " + fieldname + " lenghtNorm: " + lengthNorm);
		  
		  return lengthNorm * docBoost;
	  }
	  
	  
	  /*is a normalizing factor used to make scores between queries comparable. 
	   * This factor does not affect document ranking (since all ranked documents are multiplied by the same factor), 
	   * but rather just attempts to make scores from different queries (or even different indexes) comparable.
	   *  This is a search time factor computed by the Similarity in effect at search time. 
	   *  The default computation in DefaultSimilarity produces a Euclidean norm: */
	  public double queryNorm(bagOfWords[] docs) throws IOException{
		  ArrayList<String>fields = new ArrayList<String>();
		  Terms vector = null;
		  BytesRef text  = null;
		  TermsEnum termsEnum = null;
		  //int i = 0;
		  double norm = 0;
		  fields.add("tweets");
		  fields.add("description");
		  fields.add("tweetsFriends");
		  fields.add("tweetsFollowing");
		  double boost = 0;
		  ArrayList<String> t = new ArrayList<String>();
		  for (String fl : fields){
			vector = readerUser.getTermVector(0, fl);
			boost = readerUser.document(0).getField(fl).boost();
			termsEnum = vector.iterator();

			
			  while ((text = termsEnum.next()) != null) {
		            String term = text.utf8ToString();
		            if(t.contains(term) == false){ // check duplicate
		            	t.add(term);
		            	norm  = norm + Math.pow(idf(term, docs) * boost , 2);  
		            }else{
		            	//i++;
		            	//System.out.println("duplicate " +i);
		            }
		 
		        }
		  }
		  
		  return 1/norm;
		  
	  }
	  


	  
}
	
	
	
	
	
	
	
	
