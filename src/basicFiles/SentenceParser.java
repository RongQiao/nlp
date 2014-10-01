package basicFiles;

import java.util.Collection;
import java.util.List;

import basic.TPair;

//parse sentence, strategy
public interface SentenceParser {
	public void putWordsToCollection(String sentence, Collection<String> words);

	public void putPairsToCollection(String sentence, List<String> pairs);
	
	public void putPairsToList(String sentence, List<TPair> pairs);

	public char getSeperator();

	public void setSeperator(char seperator);

}
