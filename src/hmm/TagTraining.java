package hmm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import basic.BasicStatisticData;
import basic.ResultParser;
import basic.TPair;
import basicFiles.TextFile;

public class TagTraining extends TagTrainingResult{
	private TagTrainFile trainFile;

	public TagTraining(TagTrainFile tdf) {
		super();
		this.trainFile = tdf;
	}

	public void train() {
		trainWithoutOutput();
		outputTrainResultTag("hw3_tag.txt");
		outputTrainResultWord("hw3_word.txt");
		outputWordTagData("hw3_word_tag.txt");
		outputTagTagData("hw3_tag_tag.txt");
		//test
		outputResultNum();
	}
	
	public void trainWithoutOutput() {
		getTagsForWord();
		calculateProbWord();
		calculateProbTag();
		calculateProbWordGivenTag();
		getTagPairs();
		calculateProbTagGivenTag();
	}
	
	private void calculateProbWord() {
		calculateProbUnitMap(wordMap);
	}
	
	private void calculateProbTag() {
		calculateProbUnitMap(tagMap);
	}
	
	private void calculateProbUnitMap(UnitDataMap map) {
		Set<Entry<String, WordTagStatisticData>> entries = map.entrySet();
		int base = map.getCount();	//the base is same for all tags
		for (Entry<String, WordTagStatisticData> en: entries) {
			int cnt = map.getCount(en.getKey());
			double prob = (double)cnt / (double)base;
			WordTagStatisticData sd = en.getValue();
			sd.setProbability(prob);
		}		
	}


	public void calculateProbWordGivenTag() {
		Set<Entry<String, BasicStatisticData>> entries = wordTagPairMap.entrySet();
		for (Entry<String, BasicStatisticData> en: entries) {
			TPair pr = new TPair(en.getKey(), ResultParser.DEFAULT_SEPARATOR);
			BasicStatisticData sd = en.getValue();
			String tag = pr.getS2();
			int base = tagMap.getCount(tag);
//			String word = pr.getS1();
//			int base = wordMap.getCount(word);
			int cnt = sd.getCount();
			double prob = (double)cnt / (double)base;
			sd.setProbability(prob);
			//test
//			if (word.equalsIgnoreCase("it")) {
//				System.out.println(word + ","
//						+ base + ","
//						+ cnt + ",");
//			}
		}
	}

	private void calculateProbTagGivenTag() {
		Set<Entry<String, BasicStatisticData>> entries = tagTagPairMap.entrySet();
		double p = 0;	//for test
		for (Entry<String, BasicStatisticData> en: entries) {
			TPair pr = new TPair(en.getKey(), ResultParser.DEFAULT_SEPARATOR);
			BasicStatisticData sd = en.getValue();
			int base = tagMap.getCount(pr.getS1());
			int cnt = sd.getCount();
			double prob = (double)cnt / (double)base;
			sd.setProbability(prob);
			//test			
//			if (pr.getS1().equalsIgnoreCase("<s>")) {
//				p += prob;
//			}
		}
		//test
		//System.out.println("<s> tag/tag prob total: " + p);
	}

	private void getTagPairs() {
		List<TPair> pairs = new ArrayList<TPair>();
		trainFile.getTagPairs(pairs);
		for (TPair pr: pairs) {
			String t1 = pr.getS1();
			String t2 = pr.getS2();
			//tag map, tag is put into map during getTagsForWord()
			if (tagMap.containsKey(t1)) {
				tagMap.updateFollowed(t1, t2);						
			}

			//tag tag pair map
			String pair = pr.getPair();
			if (tagTagPairMap.containsKey(pair)) {
				tagTagPairMap.increaseCount(pair);
			}
			else {
				tagTagPairMap.createKey(pair);
			}
		}
//		//need add the last tag
//		TPair pr = pairs.get(pairs.size()-1);
//		//tag map
//		if (tagMap.containsKey(pr.getS2())) {
//			tagMap.updateKey(pr.getS2(), null);						
//		}
//		else {
//			String t = null;
//			tagMap.createKey(pr.getS2(), t);				
//		}
	}


	private void getTagsForWord() {
		List<TPair> pairs = new ArrayList<TPair>(); 
		trainFile.getWordTagPairs(pairs);
		for (TPair pr: pairs) {
			String word = pr.getS1();
			String tag = pr.getS2();
			//word map
			if (wordMap.containsKey(word)) {
				wordMap.updateKey(word, tag);						
			}
			else {
				wordMap.createKey(word, tag);				
			}
			//tag map
			if (tagMap.containsKey(tag)) {
				tagMap.updateKey(tag, null);						
			}
			else {
				String t = null;
				tagMap.createKey(tag, t);				
			}
			//word tag pair map
			String pair = word + " " + tag;
			if (wordTagPairMap.containsKey(pair)) {
				wordTagPairMap.increaseCount(pair);
			}
			else {
				wordTagPairMap.createKey(pair);
			}
		}
	}

	private void outputResultNum() {
		int cnt = 0;
		
		cnt = wordMap.getCount();
		System.out.println("token count: " + wordMap.entrySet().size() + "," + cnt);
		
		cnt = tagMap.getCount();
		System.out.println("tag count: " + tagMap.entrySet().size() + "," + cnt);
		
		cnt = wordTagPairMap.entrySet().size();
		System.out.println("word/tag count: " + cnt + "," + wordTagPairMap.getCount());
		
		cnt = tagTagPairMap.entrySet().size();
		System.out.println("tag/tag count: " + cnt + "," + tagTagPairMap.getCount());
	}

}
