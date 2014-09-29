package NgramLM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import basicFiles.TextFile;


public class TestData {
	private List<String> pairs;
	private int N;

	public TestData(List<String> pairs, int N) {
		this.pairs = pairs;
		this.N = N;
	}

	public double test(TrainingResultData trd) {
		double logProb = 0.0;
		for (String pair: pairs) {
			double logProbPair = calculateLogProbPair(pair, trd);
			logProb += logProbPair;
		}
//		//test
//		System.out.print("N="+N+"logp=");
//		System.out.println(logProb);		
		logProb = logProb / this.N;
		double perplexity = Math.pow(2, -logProb);
		//outputLanguageModel(trd);
		return perplexity;
	}
	
	private List<String> arrangeOutputContent(TrainingResultData trd) {
		DataMap uniMap = trd.getUniMap();
		DataMap biMap = trd.getBiMap();
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
				String line = sd.getLogProbability() + " "
						+ word;
				content.add(line);
			}	
		}
		return content;
	}
	
	public void outputLanguageModel(TrainingResultData trd) {
		List<String> lmContent = arrangeOutputContent(trd);
		TextFile outFile = new TextFile("xxx.txt");
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

	private double calculateLogProbPair(String pair, TrainingResultData trd) {
		double logProb = 0.0;
		DataMap biMap = trd.getBiMap();
		Map<String, StatisticData> hMap = biMap.getMap();
		StatisticData sd = hMap.get(pair);
		if (sd != null) {
			logProb = sd.getLogProbability();
		}
		else {
			logProb = calculateLogProbKatz(pair, trd);
		}
		return logProb;
	}

	private double calculateLogProbKatz(String pair, TrainingResultData trd) {
		String h = pair.substring(0, pair.indexOf(' '));
		String w = pair.substring(pair.indexOf(' ')+1);
		
		DataMap uniMap = trd.getUniMap();
		double Pkatz = uniMap.getLogAlpha(h) + uniMap.getLogProbability(w);
		return Pkatz;
	}

}
