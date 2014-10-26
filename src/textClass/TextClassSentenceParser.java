package textClass;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import textClass.TextClassResult.TextClassifier;
import basic.BasicDataMap;
import basic.BasicStatisticData;
import basic.TPair;
import basic.UnitDataMap;
import basicFiles.RegularSentenceParser;
import basicFiles.SentenceParser;

public class TextClassSentenceParser extends RegularSentenceParser implements SentenceParser {
	public void putWordsToCollection(String sentence,
			ItemClassifyingMap vMap, BasicStatisticData sd, TextClassifier type) {
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
				if (!isSeperator(ch)) {
					buf[j++] = ch;
				}
				else {								
					str = new String(buf, 0, j);
					if (vMap.containsKey(str)) {	//map store vocabulary for whole training files
						vMap.increaseCount(str, type);
					}
					else {
						vMap.createKey(str, type);
					}
					sd.setCount(sd.getCount() + 1);	//sd record the word count for the file
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



}
