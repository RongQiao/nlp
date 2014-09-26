package NgramLM;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import basicFiles.TextFile;

public class LMFile extends TextFile{

	public LMFile(String pathname) {
		super(pathname);
	}

	public void getLMResult(DataMap uniMap, DataMap biMap) {
		List<String> unigrams = new ArrayList<String>();
		List<String> bigrams = new ArrayList<String>();		
		getResultLines(unigrams, bigrams);
		getUnigramResult(unigrams, uniMap);
		getBigramResult(bigrams, biMap);
	}
	
	private void getBigramResult(List<String> bigrams, DataMap biMap) {
		for (String line: bigrams) {
			TriSentence st = new TriSentence(line);
			st.setSeperator(' ');
			String words[] = new String[3];
			st.putWords(words);
			StatisticData sd = new StatisticData();
			sd.setLogProbability(Double.parseDouble(words[0]));
			biMap.createKey(words[1] + " " + words[2], sd);
		}
	}

	private void getUnigramResult(List<String> unigrams, DataMap uniMap) {
		for (String line: unigrams) {
			TriSentence st = new TriSentence(line);
			st.setSeperator(' ');
			String words[] = new String[3];
			st.putWords(words);
			StatisticData sd = new StatisticData();
			sd.setLogProbability(Double.parseDouble(words[0]));
			sd.setLogAlpha(Double.parseDouble(words[2]));
			uniMap.createKey(words[1], sd);
		}
	}

	public void getResultLines(List<String> unigrams, List<String> bigrams) {
		List<String> lines = this.readLines();	
		boolean readUnigram = false;
		boolean readBigram = false;
		for (Iterator<String> iter = lines.iterator(); iter.hasNext(); ) {
		    String line = iter.next();
		    if (line.length()<1) {
		    	continue;
		    }
			if (line.indexOf("unigram")>-1) {
				readUnigram = true;
			}
			else {
				if (line.indexOf("bigram")>-1) {
					readUnigram = false;
					readBigram = true;
				}
				else {
					if (readUnigram) {
						unigrams.add(line);
					}
					else if (readBigram) {
						bigrams.add(line);
					}
				}
			}
		}
	}

}
