package hmm;

import java.util.ArrayList;
import java.util.List;

import basic.ResultParser;
import basic.TPair;
import basicFiles.AbstractDataFile;
import basicFiles.DataFile;
import basicFiles.SentenceParser;
import basicFiles.TagSentenceParser;
import basicFiles.TextFile;

public class TagTrainFile extends DataFile implements DummyInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TagTrainFile(String pathname) {
		super(pathname);
		SentenceParser sp = new TagSentenceParser(); 
		sp.setSeperator(ResultParser.DEFAULT_SEPARATOR);
		this.setStParser(sp);
	}

	public void getWordTagPairs(List<TPair> pairs) {
		List<String> lines = this.readLines();
		TagSentenceParser sp = (TagSentenceParser) this.getStParser();
		for (String line: lines) {
			//add dummy start/end
			line = getDummyStartPair() + line + getDummyEndPair();
			sp.putWordTagPairsToList(line, pairs);
		}
	}

	public void getTagPairs(List<TPair> pairs) {
		List<String> lines = this.readLines();
		TagSentenceParser sp = (TagSentenceParser) this.getStParser();
		for (String line: lines) {
			//add dummy start/end
			line = getDummyStartPair() + line + getDummyEndPair();
			sp.putTagPairsToList(line, pairs);
		}
	}

	public String getDummyStart() {
		return DummyItems.getDummyStart();
	}
	
	public String getDummyStartPair() {
		return DummyItems.getDummyStartPair();
	}
	
	public String getDummyEnd() {
		return DummyItems.getDummyEnd();
	}
	
	public String getDummyEndPair() {
		return DummyItems.getDummyEndPair();
	}
}
