package NgramLM;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import basic.ValueComparator;
import basicFiles.DataFile;
import basicFiles.TextFile;

public class TrainingData extends TrainingResultData{
	public String OUT_FILENAME = "LM.txt";
	private final double EPSILON = 1e-13;
	private DataMap uniMap;
	private DataMap biMap;
	private List<String> words;
	private List<String> pairs;
	private double estGoodTurnig;
	private int N2;
	private int N1;
	private final double SMOOTH_DISCOUNT_NUM = 0.99;

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
//		//test
//		System.out.println("total words:" + words.size());
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
		//calculateBigramBackOffProb();
		outputLanguageModel();
	}

	private void calculateBigramBackOffProb() {
		for (Map.Entry<String, StatisticData> entry: this.biMap.entrySet()) {
			String pair = entry.getKey();
			StatisticData sd = entry.getValue();
			
			String h = getS1(pair);
			double alpha = uniMap.getAlpha(h);
			String w = getS2(pair);
			double Pml = uniMap.getProbability(w);
			double Pbo = alpha * Pml;
			sd.setBackOffProb(Pbo);
		}
	}

	public void outputLanguageModel() {
		List<String> lmContent = arrangeOutputContent();
		TextFile outFile = new TextFile(this.OUT_FILENAME);
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

	public double log2(double num) {
		double ret = (double) (Math.log(num)/Math.log(2)+EPSILON);
		return ret;
	}
	
	private List<String> arrangeOutputContent() {
//		SDataComparator bvc = new SDataComparator(uniMap.getMap());
//		TreeMap<String, StatisticData> sorted_map = new TreeMap<String, StatisticData>(bvc);
//		List<String> content = new ArrayList<String>();
//		if (uniMap != null) {
//			sorted_map.putAll(uniMap.getMap());
//					
//			content.add("unigrams:");
//			//test
//			System.out.println("token:" + sorted_map.size());
//			for (Map.Entry<String, StatisticData> entry: sorted_map.entrySet()) {
//				String word = entry.getKey();
//				StatisticData sd = entry.getValue();
//				String line = word + " "
//						+ sd.getCount();
//				content.add(line);
//			}			
//		}
		//sort map		
		TreeMap<String, StatisticData> sorted_map = new TreeMap<String, StatisticData>();
		List<String> content = new ArrayList<String>();
		if (uniMap != null) {
			sorted_map.putAll(uniMap.getMap());
					
			content.add("unigrams:");
//			//test
//			System.out.println("token:" + sorted_map.size());
			for (Map.Entry<String, StatisticData> entry: sorted_map.entrySet()) {
				String word = entry.getKey();
				StatisticData sd = entry.getValue();
				sd.setLogProbability(log2(sd.getProbability()));
				sd.setLogAlpha(log2(sd.getAlpha()));
				String line = sd.getLogProbability() + " "
						+ word + " "
						+ sd.getLogAlpha();
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
				sd.setLogProbability(log2(sd.getProbability()));
				String line = sd.getLogProbability() + " "
						+ word;
				content.add(line);
			}	
		}
		return content;
	}

	public void calculateN1N2() {
		this.N2 = 0;
		this.N1 = 0;
		for (Map.Entry<String, StatisticData> entry: this.biMap.entrySet()) {
			StatisticData sd = entry.getValue();
			int cnt = sd.getCount();
			if (cnt == 1) {
				this.N1++;
			}
			else if (cnt == 2) {
				this.N2++;
			}
		}
//		//test
//		System.out.println("N2:"+this.N2+",N1:"+N1);
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
			StatisticData sd = entry.getValue();
			
			double pboPairs = sd.getSumPairCount()/(double)sd.getCount();
			double Pml_Follows = sd.getBackoffPml();
			double probForAlpha = 1 - pboPairs;
			//for the case no singleton pair, the pboPairs = 1, use a discount to smooth
			if (Double.compare(probForAlpha, 0)==0) {			
				//use a smoothed probability
				probForAlpha = 1 - pboPairs * SMOOTH_DISCOUNT_NUM ;
				//mark the probability of pair begin with this word need be smoothed, do the smooth after this loop
				sd.setSmoothSign();
			}
			double alpha = probForAlpha/(1-Pml_Follows);
			sd.setAlpha(alpha);
//			//test
//			if (word.equalsIgnoreCase("$1011278")) {
//				System.out.println(word + ","
//						+ sd.getSumPairCount() + ","
//						+ sd.getCount() + ","
//						+ pboPairs + ","
//						+ probForAlpha
//						+ sd.isNeedSmooth()
//						+ sd.getAlpha() + ","
//						+ log2(sd.getAlpha()));
//			}
		}		
		updateBigramProb();
	}
	
	private void discountSmooth(String h) {
		for (Map.Entry<String, StatisticData> entry: biMap.entrySet()) {
			String pair = entry.getKey();
			String s1 = getS1(pair);
			if (s1.equalsIgnoreCase(h)) {
				StatisticData sd = entry.getValue();
				sd.setProbability(sd.getProbability() * SMOOTH_DISCOUNT_NUM);
			}
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
			String h = getS1(pair);
			int pairCnt = sd.getCount();
			if (pairCnt == 1) {
				/*
				 * good-turning:
				 * P(w|h) = 2*(N2/N1)/C(h)
				 */
				double gtCount = 2*((double)this.N2/(double)this.N1);
				double prob = gtCount/(double)uniMap.getFollowCount(h);
				sd.setProbGoodTurning(prob);
				//consider the singleton pair is the only pair for h, we also need update the prob of this pair to Pgt
				sd.setGoodturningSign();
				//uniMap.updateSumPairProb(s1, prob);
				uniMap.updateSumPairCount(h, gtCount);
			}
			else {
				//uniMap.updateSumPairProb(h, sd.getProbability());
				uniMap.updateSumPairCount(h, pairCnt);
			}
			//2.
			String s2 = getS2(pair);
			double siProb = uniMap.getProbability(s2);
			uniMap.updateBackOffPml(h, siProb);
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
			sd.setProbability(probability);
		}
	}
	
	public void updateBigramProb() {
		for (Map.Entry<String, StatisticData> entry: this.biMap.entrySet()) {
			String pair = entry.getKey();
			StatisticData sd = entry.getValue();
			
			String h = getS1(pair);
			double probability = sd.getProbability();
			if (uniMap.getSmoothSign(h))	//discount smooth is for non singleton pair, which is set in uniMap for the h
			{
				probability *= this.SMOOTH_DISCOUNT_NUM;
				sd.setProbability(probability);
			}	
			if (sd.isGoodTurning()) {	//good turning is for a singleton pair, which is in biMap
				sd.setProbability(sd.getProbGoodTurning());
			}
		}
	}
	
	public String getS1(String pair) {
		return pair.substring(0, pair.indexOf(' '));
	}
	
	public String getS2(String pair) {
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

	public DataMap getBiMap() {
		return this.biMap;
	}

	public double getAlpha(String key) {
		return uniMap.getAlpha(key);
	}


}
