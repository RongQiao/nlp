package NgramLM;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import basic.ValueComparator;
import basicFiles.TextFile;

public class TrainingData {
	private final String OUT_FILENAME_UNIG = "LM.txt";
	private final double EPSILON = 1e-13;
	private DataMap uniMap;
	private DataMap biMap;
	private List<String> words;
	private List<String> pairs;
	private double estGoodTurnig;
	private int N2;
	private int N1;

	public static void main(String[] args) {
		String fileName = "hw2_train.txt";
		DataFile df = new DataFile(fileName);
		List<String> words = df.getWords();
		List<String> pairs = df.getPairs();
		TrainingData td = new TrainingData(words, pairs);
		td.training();
	}
	
	public TrainingData(List<String> words, List<String> pairs) {
		this.words = words;
		this.pairs = pairs;
	}

	public TrainingData() {
		// TODO Auto-generated constructor stub
	}

//	public void mapWords(List<String> words) {
//		this.words = words; // TODO Auto-generated constructor stub -- pay attention
//		mapSingleWords(words);
//		mapPairWords		
//	}
	
	public void mapPairWords(List<String> pairs) {
		this.pairs = pairs;
		biMap = new DataMap();
		for (String p: pairs) {
			if (biMap.containsKey(p)) {
				int cnt = biMap.getCount(p) + 1;
				biMap.renewKey(p, cnt);
			}
			else {
				biMap.createKey(p, 1);
			}
		}	
	}
	
	public void mapSingleWords(List<String> words) {
		this.words = words; // TODO Auto-generated constructor stub -- pay attention
		uniMap = new DataMap();
		for (String w: words) {
			if (uniMap.containsKey(w)) {
				int cnt = uniMap.getCount(w) + 1;
				uniMap.renewKey(w, cnt);
			}
			else {
				uniMap.createKey(w, 1);
			}
		}
	}

	public void mapWords() {
		if (this.words != null) {
			mapSingleWords(this.words);
		}
		if (this.pairs != null) {
			mapPairWords(this.pairs);
		}
	}

	public void calculateUnigramProb() {
		double DataCnt = this.words.size();
		//test
		System.out.println("total words:" + words.size());
		for (Map.Entry<String, StatisticData> entry: this.uniMap.entrySet()) {
			String word = entry.getKey();
			StatisticData sd = entry.getValue();
			double probability = (double)sd.getCount() / DataCnt;
			sd.setProbability(probability);
		}
	}

	public double getUnigProbability(String word) {
		return uniMap.getProbability(word);
	}
	
	public void training() {
		mapWords();
		calculateUnigramProb();
		calculatePairCount();
		calculateBigramProb();		
		calculateUnigramAlpha();
		calculateBigramBackOffProb();
		outputLanguageModel();
	}

	private void calculateBigramBackOffProb() {
		for (Map.Entry<String, StatisticData> entry: this.biMap.entrySet()) {
			String pair = entry.getKey();
			StatisticData sd = entry.getValue();
			String s1 = getS1(pair);
			double alpha = uniMap.getAlpha(s1);
			String s2 = getS2(pair);
			double Pml = uniMap.getProbability(s2);
			double Pbo = alpha * Pml;
			sd.setBackOffProb(Pbo);
		}
	}

	public void outputLanguageModel() {
		List<String> lmContent = arrangeOutputContent();
		TextFile outFile = new TextFile(this.OUT_FILENAME_UNIG);
		if (!outFile.exists()) {
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		outFile.write(lmContent);
	}

	private double log2(double num) {
		double ret = (double) (Math.log(num)/Math.log(2)+EPSILON);
		return ret;
	}
	
	private List<String> arrangeOutputContent() {
		//sort map		
		TreeMap<String, StatisticData> sorted_map = new TreeMap<String, StatisticData>();
		List<String> content = new ArrayList<String>();
		if (uniMap != null) {
			sorted_map.putAll(uniMap.getMap());
					
			content.add("unigrams:");
			//test
			System.out.println("token:" + sorted_map.size());
			for (Map.Entry<String, StatisticData> entry: sorted_map.entrySet()) {
				String word = entry.getKey();
				StatisticData sd = entry.getValue();
				String line = log2(sd.getProbability()) + " " 
						+ word + " "
						+ log2(sd.getAlpha());
				content.add(line);
			}			
		}
		
		if (biMap != null) {
			content.add("bigrams:");
			sorted_map.clear();
			sorted_map.putAll(biMap.getMap());
			for (Map.Entry<String, StatisticData> entry: sorted_map.entrySet()) {
				String word = entry.getKey();
				StatisticData sd = entry.getValue();
				String line = log2(sd.getBackOffProb()) + " " 
						+ word;
				content.add(line);
			}	
		}
		return content;
	}

	public void calculateN1N2() {
		this.N2 = 0;
		this.N1 = 0;
		for (Map.Entry<String, StatisticData> entry: this.uniMap.entrySet()) {
			StatisticData sd = entry.getValue();
			int cnt = sd.getCount();
			if (cnt == 1) {
				this.N1++;
			}
			else if (cnt == 2) {
				this.N2++;
			}
		}
	}

	//for n/m
//	private double getPreciseDivide(double n, double m) {
//		
//		BigDecimal dn = BigDecimal.valueOf(n);
//		BigDecimal dm = BigDecimal.valueOf(m);
//		BigDecimal ret = dn.divide(dm, 20, BigDecimal.ROUND_UP);
//		return ret.doubleValue();
//	}
	
	public void calculateUnigramAlpha() {
		calculateN1N2();
		calculateSumPairProb();
		for (Map.Entry<String, StatisticData> entry: uniMap.entrySet()) {
			String word = entry.getKey();
			StatisticData uniSD = entry.getValue();
			
			double pboPairs = uniSD.getSumPairCount()/(double)uniSD.getCount();
			double Pml_Follows = uniSD.getBackoffPml();
			double probForAlpha = 1 - pboPairs;
			//for the case no singleton pair, the pboPairs = 1, use a discount to smooth
			if (Double.compare(probForAlpha, 0)==0) {				
				probForAlpha = 1 - pboPairs * 0.99;
			}
			double alpha = probForAlpha/(1-Pml_Follows);
			uniSD.setAlpha(alpha);
		}		
	}

	/*
	 * 1. for pair "s1 si", sum all pair's biogram prob which the cnt > 0 = sumPairProb
	 *    for cnt == 1, use good-turning to smooth
	 * 2. sum all unigram prob of si = backoffPml
	 */
	public void calculateSumPairProb() {
		for (Map.Entry<String, StatisticData> entry: biMap.entrySet()) {
			String pair = entry.getKey();
			StatisticData sd = entry.getValue();
			//1.
			String s1 = getS1(pair);
			int pairCnt = sd.getCount();
			if (pairCnt == 1) {
				/*
				 * good-turning:
				 * P(w|h) = 2*(N2/N1)/C(h)
				 */
				double gtCount = 2*((double)this.N2/(double)this.N1);
				double prob = gtCount/(double)uniMap.getFollowCount(s1);
				sd.setProbGoodTurning(prob);
				//uniMap.updateSumPairProb(s1, prob);
				uniMap.updateSumPairCount(s1, gtCount);
//				//test
//				//System.out.println(pair);
//				if (s1.indexOf("$1011278")>-1) {
//					System.out.println(s1 + "," + uniMap.getFollowCount(s1) + "," + uniMap.getProbability("</s>") + prob);
//				}
			}
			else {
				//test
				//System.out.println(pair);
				if (s1.indexOf("$102")>-1) {
					System.out.println(s1 + "," + pair + "," + uniMap.getFollowCount(s1) + ",");
				}
				//uniMap.updateSumPairProb(s1, sd.getProbability());
				uniMap.updateSumPairCount(s1, pairCnt);
			}
			//2.
			String s2 = getS2(pair);
			double siProb = uniMap.getProbability(s2);
			uniMap.updateBackOffPml(s1, siProb);
		}
	}


	//count all pair begin with word s1, record it as fellowCnt in uniMap
	public void calculatePairCount() {		
		for (Map.Entry<String, StatisticData> entry: biMap.entrySet()) {
			String pair = entry.getKey();
			String s1 = getS1(pair);
			uniMap.increaseFellowCount(s1, entry.getValue().getCount());
		}
	}

	public void calculateBigramProb() {
		for (Map.Entry<String, StatisticData> entry: this.biMap.entrySet()) {
			String pair = entry.getKey();
			StatisticData sd = entry.getValue();
			
			String s1 = getS1(pair);
			double s1PairCnt = uniMap.getFollowCount(s1);
			double probability = (double)sd.getCount() / s1PairCnt;// + EPSILON;
			//test
			//System.out.println(s1 + s1Cnt + probability);
			sd.setProbability(probability);
		}
	}
	
	private String getS1(String pair) {
		return pair.substring(0, pair.indexOf(' '));
	}
	
	private String getS2(String pair) {
		int indexStart = pair.indexOf(' ');
		indexStart += 1;
		return pair.substring(indexStart, pair.length());
	}
	
	/*
	 * P(s2|s1) means prob for pair "s1 s2"
	 */
	public double getBiogProbability(String s1, String s2) {
		String pair = s1 + " " + s2;
		return biMap.getProbability(pair);
	}

	public DataMap getUniMap() {
		return this.uniMap;
	}

	public double getAlpha(String key) {
		return uniMap.getAlpha(key);
	}


}
