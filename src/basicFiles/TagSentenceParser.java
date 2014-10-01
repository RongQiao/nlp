package basicFiles;

import java.util.Collection;
import java.util.List;

import basic.TPair;

public class TagSentenceParser implements SentenceParser {
	private char wordSeparator;
	private char tagSeparator;

	@Override
	public void putWordsToCollection(String sentence, Collection<String> words) {
		int len = sentence.length();
		char buf[] = new char[100];		
		
		String word = new String();
		if (len > 0) {
			byte data[] = new byte[len + 1];
			for (int i = 0; i < data.length; i++) {
				data[i] = 0;
			}
			System.arraycopy(sentence.getBytes(), 0, data, 0, len);
			data[data.length-1] = (byte) this.getSeperator();
			int j = 0;
			for (int i = 0; i < data.length; i++) {
				char ch = (char)data[i];
				if (!isWordSeparator(ch)) {
					buf[j++] = ch;
				}
				else {								
					word = new String(buf, 0, j);
					words.add(word);
					j = 0;
					//get rid of multi seperator
					while (i+1 < data.length) {
						ch = (char)data[i+1];
						if (isWordSeparator(ch)) {
							i++;
						}
						else {
							break;
						}
					}
				}

			}		
		}
	}

	@Override	
	public void putPairsToCollection(String sentence, List<String> pairs) {
		int len = sentence.length();
		char buf[] = new char[100];		
		
		String word = new String();
		if (len > 0) {
			byte data[] = new byte[len + 1];
			for (int i = 0; i < data.length; i++) {
				data[i] = 0;
			}
			System.arraycopy(sentence.getBytes(), 0, data, 0, len);
			data[data.length-1] = (byte) this.getSeperator();
			int j = 0;
			for (int i = 0; i < data.length; i++) {
				char ch = (char)data[i];
				if (!isWordSeparator(ch)) {
					buf[j++] = ch;
				}
				else {								
					word = new String(buf, 0, j);
					
					pairs.add(word);
					j = 0;
					//get rid of multi seperator
					while (i+1 < data.length) {
						ch = (char)data[i+1];
						if (isWordSeparator(ch)) {
							i++;
						}
						else {
							break;
						}
					}
				}

			}		
		}
	}

	@Override
	public char getSeperator() {
		return this.wordSeparator;
	}

	@Override
	public void setSeperator(char seperator) {
		this.wordSeparator = seperator;
	}

	private boolean isWordSeparator(char ch) {
		boolean ret = (ch == this.getSeperator()
				|| (ch == '\t'));
		return ret;
	}

	// an item and its tag is a pair, such as "The/DT"
	public void putWordTagPairsToList(String sentence, List<TPair> pairs) {
		int len = sentence.length();
		char buf[] = new char[100];		
		
		String str = new String();
		if (len > 0) {
			byte data[] = new byte[len + 1];
			for (int i = 0; i < data.length; i++) {
				data[i] = 0;
			}
			System.arraycopy(sentence.getBytes(), 0, data, 0, len);
			data[data.length-1] = (byte) this.getSeperator();
			int j = 0;
			for (int i = 0; i < data.length; i++) {
				char ch = (char)data[i];
				if (!isWordSeparator(ch)) {
					buf[j++] = ch;
				}
				else {								
					str = new String(buf, 0, j);
					TPair pr = new TPair(str, '/');
					pairs.add(pr);
					j = 0;
					//get rid of multi seperator
					while (i+1 < data.length) {
						ch = (char)data[i+1];
						if (isWordSeparator(ch)) {
							i++;
						}
						else {
							break;
						}
					}
				}

			}		
		}
	}

	@Override
	public void putPairsToList(String sentence, List<TPair> pairs) {
		// TODO Auto-generated method stub
		
	}
}
