package hmm;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import NgramLM.RegularSentenceParser;
import basic.ResultParser;
import basic.TPair;
import basicFiles.TextFile;

public class TagTest {
	private TagTrainingResult trainResult;
	private RegularSentenceParser stParser;
	
	//save data
	List<String> tags;
	double transitionTable[][];
	
	
	public TagTest() {
		stParser = new RegularSentenceParser();
		tags = null;
	}

	public void test(TagTrainingResult ttr, String fileName) {
		trainResult = ttr;
		getAllTrainingResult();
		TextFile testFile = new TextFile(fileName);
		giveTagForFile(testFile);
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
				//test
//				if (proTi2Tj > 0.0) {
//					System.out.println(proTi2Tj + "," + Ti + "->" + Tj);
//				}
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
			String tagedSt = giveTagForSentence(st);
			//get rid of dummy start/end word/tag pair
			//tagedSt = getRidOfDummyPair(tagedSt);
			tagedFile.appendLine(tagedSt);
		}
	}

	private String getRidOfDummyPair(String tagedSt) {
		String start = DummyItems.getDummyStartPair();
		String end = DummyItems.getDummyEndPair();
		int index = tagedSt.indexOf(start);
		String ret = tagedSt.substring(index + start.length());
		index = ret.indexOf(end);
		ret = ret.substring(0, index);
		return ret;
	}

	private String giveTagForSentence(String st) {
		String ret = "";
		List<String> words = new ArrayList<String>(); 
		stParser.setSeperator(ResultParser.DEFAULT_SEPARATOR);
		stParser.putWordsToCollection(st, words);
		List<String> wordTags = calculateViterbiPath(words);
		for (int i = 0; i < words.size(); i++) {
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
		double viterbi[][] = new double[StateCnt][timeCnt];	//state-rows, time-columns
		double a[][] = this.getTransitionTable();
		int back_track[][] = new int[StateCnt][timeCnt];
		//init viterbi, because the first word is <s>, give <s>/<s> value 1.0, else default 0
		//for each states 
		for (int s = 0; s < StateCnt; s++) {
			if (tags.get(s).equalsIgnoreCase("<s>")) {
				viterbi[s][0] = 1.0;
				break;
			}
		}
		//for each time step t from 0 to T
		for (int t = 1; t < timeCnt; t++) {
			//for each states 
			for (int si = 0; si < StateCnt; si++) {
				//for each transition s' from s specified by state-graph
				for (int sj = 0; sj < StateCnt; sj++) {
					//new value <- viterbi[s,t]*a[si,sj]*bsj(Ot)
					double v = viterbi[si][t-1];
					double aij = a[si][sj];
					double bjt = getBsOt(tags.get(sj), words.get(t));
					double new_score = v * aij * bjt;
					if (new_score >viterbi[sj][t]) {
						viterbi[sj][t] = new_score;
						back_track[sj][t] = si;	//for getting the path
						//test
						//System.out.println(words.get(t) + "/" + tags.get(sj) + ":" + new_score);
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
			if (vMax < viterbi[s][lastT]) {
				vMax = viterbi[s][lastT];
				sTrack[lastT] = s;
			}
		}
		int nextS = sTrack[lastT];
		for (int t = timeCnt - 1; t > 1; t--) {
			int s = back_track[nextS][t];
			sTrack[t-1] = s; 
			nextS = s;
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

	private void initViterbi(double[][] viterbi) {
//		//for each time step t from 0 to T
//		for (int t = 0; t < timeCnt-1; t++) {
//			//for each states 
//			for (int si = 0; si < StateCnt; si++) {
//			}
//		}
	}

	private double getBsOt(String tag, String word) {
		String key = word + ResultParser.DEFAULT_SEPARATOR + tag;
		double ret = trainResult.wordTagPairMap.getProbability(key);
		return ret;
	}

	private void getAllTrainingResult() {
		trainResult.learnAllTrainingResult();
	}



}
