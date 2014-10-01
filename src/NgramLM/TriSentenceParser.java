package NgramLM;

import java.util.Collection;
import java.util.List;

import basic.TPair;
import basicFiles.SentenceParser;

public class TriSentenceParser implements SentenceParser{
	private char seperator;

	@Override
	public void putWordsToCollection(String sentence, Collection<String> words) {
		String s = sentence;
		for (int i = 0; i < 2; i++) {
			int index = s.indexOf(this.getSeperator());
			words.add(s.substring(0, index));
			s = s.substring(index+1, s.length());
		}
		words.add(s.substring(0, s.length()));
	}

	@Override
	public void putPairsToCollection(String sentence, List<String> pairs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public char getSeperator() {
		return seperator;
	}

	@Override
	public void setSeperator(char seperator) {
		this.seperator= seperator;
	}

	@Override
	public void putPairsToList(String sentence, List<TPair> pairs) {
		// TODO Auto-generated method stub
		
	}

}
