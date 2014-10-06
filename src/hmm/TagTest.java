package hmm;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import NgramLM.RegularSentenceParser;
import basic.BasicStatisticData;
import basic.ResultParser;
import basic.TPair;
import basicFiles.TextFile;

public class TagTest {
	private TagTrainingResult trainResult;
	private RegularSentenceParser stParser;
	private PairDataMap unseenWordTagMap;
	
	//save data
	List<String> tags;
	double transitionTable[][];
	private TagStrategy4UnseenWord unseenStrategy;
	
	
	public TagTest() {
		stParser = new RegularSentenceParser();
		unseenWordTagMap = new PairDataMap();
		tags = null;
	}

	public void test(TagTrainingResult ttr, String fileName) {
		trainResult = ttr;
		getAllTrainingResult();
		TextFile testFile = new TextFile(fileName);
		giveTagForFile(testFile);
	}
	

	private void getAllTrainingResult() {
		trainResult.learnAllTrainingResult();
	}

	private double[][] buildTransitionTable() {
		List<String> tags = getTags();
		int StateCnt = tags.size();
		double[][] trsTable = new double[StateCnt][StateCnt];
		
		for (int i = 0; i < StateCnt; i++) {
			for (int j = 0; j < StateCnt; j++) {
				String Ti = tags.get(i);
				String Tj = tags.get(j);
				double proTi2Tj = trainResult.getTagPairProb(Tj, Ti);					
				if (!(proTi2Tj > 0.0)) {
					//test
					//System.out.println(proTi2Tj + "," + Ti + "->" + Tj);
					if ((Tj.equalsIgnoreCase(DummyItems.getDummyStart()))
							|| (Ti.equalsIgnoreCase(DummyItems.getDummyEnd()))) {
						continue;	//keep X-><s> or <.s>->X as 0.0
					}
				}
				//smooth
				double a = 0.99;
				proTi2Tj = a * proTi2Tj + (1 - a) * trainResult.tagMap.getProbability(Tj);
				trsTable[i][j] = proTi2Tj;
			}
		}
		
		return trsTable;
	}
	
	private List<String> getTags() {
		if (tags == null) {
			Set<Entry<String, WordTagStatisticData>> entrys = trainResult.tagMap.entrySet();
			tags = new ArrayList<String>();
			for (Entry<String, WordTagStatisticData> en: entrys) {
				String t = en.getKey();
				tags.add(t);
			}
		}
		return tags;
	}
	
	private double[][] getTransitionTable() {
		if (transitionTable == null) {
			transitionTable = buildTransitionTable();
		}
		return transitionTable;
	}
	
	private void giveTagForFile(TextFile testFile) {
		TextFile tagedFile = new TextFile("taged.txt");
		tagedFile.clear();
		List<String> sentences = testFile.readLines();
		for (String st: sentences) {
			//add dummy start/end
			st = DummyItems.getDummyStart() + st + DummyItems.getDummyEnd();
			//the out put is without dummy, it's removed in the process
			String tagedSt = giveTagForSentence(st);
			tagedFile.appendLine(tagedSt);
		}
	}

	private String getRidOfDummyPair(String tagedSt) {
		String start = DummyItems.getDummyStartPair();
		String end = DummyItems.getDummyEndPair();
		int index = tagedSt.indexOf(start);
		String ret = tagedSt.substring(index + start.length());
		index = ret.indexOf(end);
		if (index > 0) {
			ret = ret.substring(0, index);
		}
		//test
		else {
			System.out.println(tagedSt);
		}
		return ret;
	}

	private String giveTagForSentence(String st) {
		String ret = "";
		List<String> words = new ArrayList<String>(); 
		stParser.setSeperator(ResultParser.DEFAULT_SEPARATOR);
		stParser.putWordsToCollection(st, words);
		List<String> wordTags = calculateViterbiPath(words);
		//get rid of dummy start/end by ignore the first/last word
		for (int i = 1; i < words.size()-1; i++) {
			String t = wordTags.get(i);
			ret += t;
			if (i < words.size()-1) {
				ret += ResultParser.DEFAULT_SEPARATOR;
			}
		}
		return ret;
	}
	
	/*
	 * compare to algorithm from textbook, we don't need give + 2, because we've given dummy start/end
	 */
	private List<String> calculateViterbiPath(List<String> words) {
		List<String> tags = this.getTags();
		int StateCnt = tags.size();
		int timeCnt = words.size();
		double a[][] = this.getTransitionTable();
		double viterbi[][] = new double[StateCnt][timeCnt];	//state-rows, time-columns
		int back_track[][] = new int[StateCnt][timeCnt];
		//init viterbi
		//because the first word is <s>, give <s>/<s> value 1.0, else default 0
		for (int s = 0; s < StateCnt; s++) {
			if (tags.get(s).equalsIgnoreCase("<s>")) {
				viterbi[s][0] = 1.0;
				break;
			}
		}

		//for each time step t from 1 to timeCnt, the first have been initialized
		for (int t = 1; t < timeCnt; t++) {
			String word = words.get(t);			
			//for each states 
			for (int si = 0; si < StateCnt; si++) {
				//for each transition s' from s specified by state-graph
				for (int sj = 0; sj < StateCnt; sj++) {					
					String tag = tags.get(sj);
					//new value <- viterbi[si,t-1]*a[si,sj]*bsj(Ot)
					double v = viterbi[si][t-1];
					double aij = a[si][sj];
					double bjt = getBsOt(tag, word);
					double new_score = v * aij * bjt;
					if (new_score >= viterbi[sj][t]) {
						//test
//						if (word.equalsIgnoreCase("1,111")) {
//							System.out.println(tag + "," + new_score + "," + viterbi[sj][t]);
//						}
						viterbi[sj][t] = new_score;
						back_track[sj][t] = si;	//for getting the path

					}
				}
			}
		}		
		//get the path
		List<String> tagedWords = getBackPath(tags, words, viterbi, back_track);
		return tagedWords;
	}	
	
	private List<String> getBackPath(List<String> tags, List<String> words,
			double[][] viterbi, int[][] back_track) {
		int timeCnt = words.size();
		int stateCnt = tags.size();
		//find the max at the last t
		int lastT = timeCnt - 1;
		int sTrack[] = new int[timeCnt];
		double vMax = 0;
		for (int s = 0; s < stateCnt; s++) {
			if (viterbi[s][lastT] > vMax) {
				vMax = viterbi[s][lastT];
				sTrack[lastT] = s;
			}
		}
		int preS = sTrack[lastT];
		for (int t = timeCnt - 1; t > 1; t--) {
			int currentState = back_track[preS][t];
			sTrack[t-1] = currentState; 
			preS = currentState;
		}
		//out put tag
		List<String> tagedWords = new ArrayList<String>();
		for (int t = 0; t < timeCnt; t++) {
			String tw = words.get(t);
			tw += "/"; 
			tw += tags.get(sTrack[t]);
			tagedWords.add(tw);
		}
		
		return tagedWords;
	}

	//p(w|t)
	private double getBsOt(String tag, String word) {
		double prob = 0.0;
		//check if word is seen
		if (!isSeenWord(word)) {
			//unseen word
			//1. try lower case first
			word = word.toLowerCase();
		}
		if (isSeenWord(word)) {
			//for seen word, check if word/tag pair is seen
			String pair = word + ResultParser.DEFAULT_SEPARATOR + tag;
			if (isSeenPair(pair)) {
				//use training result
				prob =trainResult.wordTagPairMap.getProbability(pair);
			}
			else {
				prob = getBsOtUnseenWordTagPair(tag, word);
			}
		}
		else {
			prob = getBsOtUnseenWord(tag, word);			
		}
		return prob;
	}

	/*
	 * word is seen, but word/pair is unseen, give very small value, because seen pair should have more chance 
	 * assume the min seen word/tag pair is 1, so get the half prob
	 */
	private double getBsOtUnseenWordTagPair(String tag, String word) {
		double prob = 0.0;
		
		//prob = trainResult.wordTagPairMap.getMinProbability();
		//prob /= 2;
		
		return prob;
	}

	private boolean isSeenPair(String pair) {	
		return trainResult.wordTagPairMap.containsKey(pair);
	}

	private double getBsOtUnseenWord(String tag, String word) {
		double prob = 0.0;
		
		String key = word + ResultParser.DEFAULT_SEPARATOR + tag;
		if (unseenWordTagMap.containsKey(key)) {
			//increase the count, keep the prob as the unit prob, so the returned prob = count * unit_prob
			BasicStatisticData sd = unseenWordTagMap.get(key);
			//sd.setCount(sd.getCount()+1);
			//prob = sd.getProbability() * (double)sd.getCount();
			prob = sd.getProbability();
		}
		else {
			//2. use a strategy to give a probability			
			TagStrategy4UnseenWord stg = getUnseenStrategy();
			prob = stg.giveProbToWordTagPair(word, tag);
			//save
			BasicStatisticData sd = new BasicStatisticData();
			sd.setCount(1);
			sd.setProbability(prob);
			unseenWordTagMap.createKey(key, sd);
		}
		
		return prob;
	}

	private TagStrategy4UnseenWord getUnseenStrategy() {
		if (unseenStrategy == null) {
			unseenStrategy = new TagStrategy4UnseenWord();
			unseenStrategy.setTrainResult(trainResult);
		}
		return unseenStrategy;
	}

	private boolean isSeenWord(String word) {		
		return trainResult.wordMap.containsKey(word);
	}




}
