package basicFiles;

import java.util.ArrayList;
import java.util.List;

import NgramLM.RegularSentenceParser;

/*
 * Each line in the data files contains a sentence 
 * different data may have different sentence parser
 */
public class DataFile extends SentenceBasedFile{

	public DataFile(String pathname) {
		super(pathname);
		SentenceParser sp = new RegularSentenceParser();
		sp.setSeperator(' ');
		this.setStParser(sp);
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<String> getWords() {
		List<String> words = new ArrayList<String>();
		List<String> lines = this.readLines();
		SentenceParser sp = this.getStParser();
		for (String line: lines) {
			sp.putWordsToCollection(line, words);
		}
		return words;
	}

	public List<String> getPairs() {
		List<String> pairs = new ArrayList<String>();
		List<String> lines = this.readLines();
		SentenceParser sp = this.getStParser();
		for (String line: lines) {
			sp.putPairsToCollection(line, pairs);
		}
		return pairs;
	}


}
