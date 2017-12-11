package lucene;

import java.util.ArrayList;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;

import csv.CSVReader;

public class MyAnalyzer extends Analyzer{

	/*Constructor*/
	public MyAnalyzer(){	
	}

	@Override
	protected TokenStreamComponents createComponents(String field) {
		//Identify tokens separated by whitespacesand punctuation 
		
		/**
		 * A LetterTokenizer is a tokenizer that divides text at non-letters. That's to say, it defines tokens as maximal strings of adjacent letters, 
		 * as defined by java.lang.Character.isLetter() predicate.
		 * Note: this does a decent job for most European languages, but does a terrible job for some Asian languages, where words are not separated by spaces. 
		 **/
		final Tokenizer tokenizer = new LetterTokenizer(); 
		
		CSVReader csv = new CSVReader();
		ArrayList<String> sw = csv.Reader("/home/andrew/Documenti/eclipse/workspace/Twitter-IR/otherFile/stopwords-long");

		
		/*The list is taken from http://www.ranks.nl/stopwords */
		CharArraySet stopWords = new CharArraySet(sw.size(), true);
		
		for (String s : sw){
			stopWords.add(s);
		}
		
		TokenStream result = new StopFilter(tokenizer, stopWords); 
		result = new LengthFilter(result, 3, Integer.MAX_VALUE);
		result = new LowerCaseFilter(result);// reduce token to lower case
		
		/*
		 * Transforms the token stream as per the Porter stemming algorithm. Note: the input to the stemming filter must already be in lower case,
		 *  so you will need to use LowerCaseFilter or LowerCaseTokenizer farther down the Tokenizer chain in order for this to work properly! 
		 */
		result = new PorterStemFilter(result);
		
		
		return new TokenStreamComponents(tokenizer, result);
	}

}
