package NgramLM;

import java.util.List;
import java.util.Map;


public class TestData extends TrainingData {
	private List<String> pairs;
	private int N;

	public TestData(List<String> pairs, int N) {
		this.pairs = pairs;
		this.N = N;
	}

	public void test(TrainingResultData trd) {
		double logProb = 0.0;
		for (String pair: pairs) {
			double logProbPair = calculateLogProbPair(pair, trd);
			logProb += logProbPair;
		}
		//test
		System.out.print("N="+N+"logp=");
		System.out.println(logProb);		
		logProb = logProb / pairs.size();//this.N;
		double prob = Math.pow(2, -logProb);

		System.out.println(prob);
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
		String h = getS1(pair);
		String w = getS2(pair);
		
		DataMap uniMap = trd.getUniMap();
		double Pkatz = uniMap.getLogAlpha(h) + uniMap.getLogProbability(w);
		return Pkatz;
	}

}
