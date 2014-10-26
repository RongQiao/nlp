package textClass;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class TextClassTestResult extends TextClassResult{
	private ItemClassifyingMap fileClassifyMap;

	public TextClassTestResult() {
		fileClassifyMap = new ItemClassifyingMap();
	}
	
	public double calculateAccuracy(TextClassTestResult testRRef) {
		double ac = 0.0;
		int diffCnt = 0;
		Set<Entry<String, BayesStatisticData>> entries = fileClassifyMap.entrySet();
		for (Entry<String, BayesStatisticData> en: entries) {
			String fileName = en.getKey();
			BayesStatisticData bsd = en.getValue();
			TextClassifier type = classify(bsd);
			TextClassifier refType = testRRef.getClassification(fileName);
			if (type != refType) {
				diffCnt++;
			}
		}
		int totalCnt = entries.size();
		ac = (double)(totalCnt - diffCnt) / (double)totalCnt;
		return ac;
	}

	private TextClassifier getClassification(String fileName) {
		BayesStatisticData sd = fileClassifyMap.get(fileName);
		return classify(sd);
	}

	private TextClassifier classify(BayesStatisticData bsd) {
		TextClassifier retType = TextClassifier.POSITIVE;
		double maxProb = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < TextClassifier.getCount(); i++) {
			TextClassifier type = TextClassifier.getType(i);
			double prob = bsd.getProbability(type);
			if (prob > maxProb) {
				maxProb = prob;
				retType = type;
			}
		}
		return retType;
	}

	public void addResult(List<String> filesName,
			TextClassifier type) {
		for (String fn: filesName) {
			BayesStatisticData bsd = new BayesStatisticData();
			bsd.setProbability(type, 1.0);
			fileClassifyMap.createKey(fn, bsd);
			
		}
	}

	public void addResult(String fileName, BayesStatisticData bsd) {
		fileClassifyMap.createKey(fileName, bsd);
	}

}
