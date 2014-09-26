package NgramLM;

import java.util.ArrayList;
import java.util.List;

import basicFiles.WordBasedFile;

/*
 * Each line in the data files contains a sentence 
 * (with the sentence starting <s> and endingsymbol </s>).
 * Use white spaces as separator to build a word-based language model.
 * Please treat sentence beginning and ending symbols as a "word".
 */
public class DataFile extends WordBasedFile{

	public DataFile(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<String> getWords() {
		List<String> words = new ArrayList<String>();
		List<String> lines = this.readLines();
		Sentence st = new Sentence();
		st.setSeperator(' ');
		for (String line: lines) {
			st.setContent(line);
			st.putWordsToCollection(words);
		}
		return words;
	}

	public List<String> getPairs() {
		List<String> pairs = new ArrayList<String>();
		List<String> lines = this.readLines();
		Sentence st = new Sentence();
		st.setSeperator(' ');
		for (String line: lines) {
			st.setContent(line);
			st.putPairsToCollection(pairs);
		}
		return pairs;
	}

	public List<String> getSentences() {
		//List<String> sents = new ArrayList<String>();
		List<String> lines = this.readLines();
		return lines;
	}

}
