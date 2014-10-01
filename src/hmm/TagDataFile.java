package hmm;

import java.util.ArrayList;
import java.util.List;

import basic.TPair;
import basicFiles.AbstractDataFile;
import basicFiles.DataFile;
import basicFiles.SentenceParser;
import basicFiles.TagSentenceParser;
import basicFiles.TextFile;

public class TagDataFile extends DataFile{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TagDataFile(String pathname) {
		super(pathname);
		SentenceParser sp = new TagSentenceParser(); 
		sp.setSeperator(' ');
		this.setStParser(sp);
	}

	public void getWordTagPairs(List<TPair> pairs) {
		List<String> lines = this.readLines();
		TagSentenceParser sp = (TagSentenceParser) this.getStParser();
		for (String line: lines) {
			sp.putWordTagPairsToList(line, pairs);
		}
	}

}