package source;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.crypto.Data;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * This class pulls out the source from https://newsapi.org/
 *
 * @author Andrea Angiolillo
 */
public class GetSource {

	/**
	 * This method takes the sources from https://newsapi.org/ of category: "category"  
	 *   
	 * @param category String with a category for the sources; the category are: entertainment, business, gaming, music, science-and-nature,
	 * sport, technology, general
	 * @return out  ArrayList<Source> with all sources of "category"
	 */

	// HTTP GET request
	public ArrayList<Source>  sendGet(String category) throws Exception {
		
		String url = "";
		category = category.toLowerCase();
		switch (category){
			case "entertainment":
				url = "https://newsapi.org/v1/sources?language=en&category=entertainment";
				break;
			case "business":
				url = "https://newsapi.org/v1/sources?language=en&category=business";
				break;
			case "gaming":
				url = "https://newsapi.org/v1/sources?language=en&category=gaming";
				break;
			case "music":
				url = "https://newsapi.org/v1/sources?language=en&category=music";
				break;
			case "science-and-nature":
				url = "https://newsapi.org/v1/sources?language=en&category=science-and-nature";
				break;
			case "sport":
				url = "https://newsapi.org/v1/sources?language=en&category=sport";
				break;
			case "technology":
				url = "https://newsapi.org/v1/sources?language=en&category=technology";
				break;
			case "general":
				url = "https://newsapi.org/v1/sources?language=en&category=general";
				break;
			default:
	             throw new IllegalArgumentException("Invalid category: " + category);
			
		}

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");


		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);

		}
		in.close();
		return parsing(response, -1);

	}
	
	/**
	 * This method takes the sources (in json form) and creates an ArrayList of Source (class)
	 * where each Source (class) is a sources of https://newsapi.org/  
	 *   
	 * @param json StringBuffer
	 * @param n int number of source that will be converted
	 * @return out  ArrayList<Source> with all sources of "category"
	 */
	
	public ArrayList<Source>  parsing(StringBuffer json, int n){
		
		JSONObject obj = new JSONObject(json.toString());
		JSONArray sources = obj.getJSONArray("sources");
		//System.out.print(sources.length() + " source length");
		
		ArrayList<Source> out = new ArrayList<Source>();
		String id = "";
		String name = "";
		String description = "";
		String url = "";
		String category = "";
		String language = "";
		String country = "";
		
		if (n < 0 || n > sources.length() ){
		
			for(int i = 0; i < sources.length(); i++){
				
				id = sources.getJSONObject(i).getString("id");
				name = sources.getJSONObject(i).getString("name");
				description = sources.getJSONObject(i).getString("description");
				url = sources.getJSONObject(i).getString("url");
				category = sources.getJSONObject(i).getString("category");
				language = sources.getJSONObject(i).getString("language");
				country = sources.getJSONObject(i).getString("country");
				out.add(new Source(id,name, description, url, category, language,country));
			}
		}else{
			
			for(int i = 0; i < n; i++){
				
				id = sources.getJSONObject(i).getString("id");
				name = sources.getJSONObject(i).getString("name");
				description = sources.getJSONObject(i).getString("description");
				url = sources.getJSONObject(i).getString("url");
				category = sources.getJSONObject(i).getString("category");
				language = sources.getJSONObject(i).getString("language");
				country = sources.getJSONObject(i).getString("country");
				out.add(new Source(id,name, description, url, category, language,country));
				
			}
			
		}
		
		return out;
		
	}
	
	
	
	
}





