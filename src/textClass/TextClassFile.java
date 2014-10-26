package textClass;

import java.util.List;
import java.util.Map;

import textClass.TextClassResult.TextClassifier;
import basic.BasicDataMap;
import basic.BasicStatisticData;
import basic.ResultParser;
import basicFiles.DataFile;
import basicFiles.RegularSentenceParser;
import basicFiles.SentenceParser;
import basicFiles.TagSentenceParser;

public class TextClassFile extends DataFile{

	public TextClassFile(String pathname) {
		super(pathname);
		SentenceParser sp = new TextClassSentenceParser(); 
		sp.setSeperator(ResultParser.DEFAULT_SEPARATOR);
		this.setStParser(sp);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void getWords(ItemClassifyingMap vMap, BasicStatisticData sd, TextClassifier type) {
		List<String> lines = this.readLines();
		TextClassSentenceParser sp = (TextClassSentenceParser) this.getStParser();
		for (String line: lines) {
			sp.putWordsToCollection(line, vMap, sd, type);
		}
	}

}
