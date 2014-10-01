package basicFiles;

import java.util.Collection;
import java.util.List;

import basic.TPair;

public class TagSentenceParser implements SentenceParser {
	private static final char STRING_END = '\0';
	private char wordSeparator;
	private char tagSeparator;

	@Override
	public void putWordsToCollection(String sentence, Collection<String> words) {
	}

	@Override	
	public void putPairsToCollection(String sentence, List<String> pairs) {
		
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
	}

	public void putTagPairsToList(String sentence, List<TPair> pairs) {
		byte content[] = sentence.getBytes();
		char buf[] = new char[100];
		int j = 0;
		String pair = new String();
		
		if (content.length > 0) {
			byte data[] = new byte[content.length + 1];
			System.arraycopy(content, 0, data, 0, content.length);
			data[content.length] = (byte)this.getSeperator();
			String s1 = new String();
			
			for (int i = 0; i < data.length; i++) {
				char ch = (char)data[i];
				if (isWordSeparator(ch)) {
					buf[j] = STRING_END;
					String currentS = new String(buf, 0, j);

					if (s1.isEmpty()) {
						s1 = currentS;
					}
					else {
						TPair p1 = new TPair(s1, '/');
						TPair p2 = new TPair(currentS, '/');
						TPair pr = new TPair(p1.getS2(), p2.getS2());
						pairs.add(pr);
						//set the current string as s1, which is the first string for next pair
						s1 = currentS;
					}					
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
				else {
					buf[j++] = ch;
				}
			}		
		}
	}
}
