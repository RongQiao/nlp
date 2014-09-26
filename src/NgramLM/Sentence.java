package NgramLM;

import java.util.Collection;
import java.util.List;

public class Sentence {
	protected static final char STRING_END = '\0';
	protected byte content[];
	protected char seperator;

	public Sentence(String line) {
		content = line.getBytes();
	}

	public Sentence() {
		content = new byte[0];
	}

	public void putWordsToCollection(Collection<String> words) {
		char buf[] = new char[100];		
		
		String word = new String();
		if (content.length > 0) {
			byte data[] = new byte[content.length + 1];
			for (int i = 0; i < data.length; i++) {
				data[i] = 0;
			}
			System.arraycopy(content, 0, data, 0, content.length);
			data[data.length-1] = (byte) this.getSeperator();
			int j = 0;
			for (int i = 0; i < data.length; i++) {
				char ch = (char)data[i];
				if (!isSeperator(ch)) {
					buf[j++] = ch;
				}
				else {								
					word = new String(buf, 0, j);
					words.add(word);
					j = 0;
					//get rid of multi seperator
					while (i+1 < data.length) {
						ch = (char)data[i+1];
						if (isSeperator(ch)) {
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

	public void putPairsToCollection(List<String> pairs) {
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
				if (isSeperator(ch)) {
					buf[j] = STRING_END;
					String currentS = new String(buf, 0, j);

					if (s1.isEmpty()) {
						s1 = currentS;
					}
					else {
						pair = s1 + " " + currentS;
						pairs.add(pair);
						//set the current string as s1, which is the first string for next pair
						s1 = currentS;
					}					
					j = 0;
					//get rid of multi seperator
					while (i+1 < data.length) {
						ch = (char)data[i+1];
						if (isSeperator(ch)) {
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
	
	private boolean isSeperator(char ch) {
		boolean ret = (ch == this.getSeperator()
				|| (ch == '\t'));
		return ret;
	}

	public char getSeperator() {
		return seperator;
	}

	public void setSeperator(char seperator) {
		this.seperator = seperator;
	}

	public void setContent(String line) {
		content = line.getBytes();
	}


}
