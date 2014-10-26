package textClass;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import test.TextClassTrainTest;
import textClass.TextClassResult.TextClassifier;
import basic.BasicDataMap;
import basic.BasicStatisticData;

public class TextClassify extends TextClassCorpus{
	public static void main(String...args) {
		if (args.length < 5) {
			System.out.println("error: files not given.");
		}
		else {
			String dirPos = args[1] + "/pos";
			String dirNeg = args[1] + "/neg";
			String foldNum = args[2];
			String modelFileName = args[4];
			int foldIndex = Integer.parseInt(foldNum);	
			
			testMain(dirPos, dirNeg, foldIndex, modelFileName);			
		}
	}
	
	public static double testMain(String dirPos, String dirNeg, int foldIndex,
			String modelFileName) {
		TextClassTrainResult tctr = new TextClassTrainResult();
		tctr.learnTrainResult(modelFileName);
		
		TextClassify tc = getTextClassify(dirPos, dirNeg, foldIndex);
		
		tc.setTrainResult(tctr);
		TextClassTestResult testR = tc.test();
		TextClassTestResult testRRef = getRefResult(dirPos, dirNeg, foldIndex);
		double ac = testR.calculateAccuracy(testRRef);
		System.out.println(ac);
		return ac;
	}

	public static TextClassTestResult getRefResult(String dirPos, String dirNeg, int foldIndex) {
		TextClassTestResult testRRef = new TextClassTestResult();
		List<String> trainFilesNamePos = getFileCorpus(dirPos, foldIndex, false);
		testRRef.addResult(trainFilesNamePos, TextClassifier.POSITIVE);
		List<String> trainFilesNameNeg = getFileCorpus(dirNeg, foldIndex, false);
		testRRef.addResult(trainFilesNameNeg, TextClassifier.NEGATIVE);
		return testRRef;
	}
	
	public static TextClassify getTextClassify(String dirPos, String dirNeg, int foldIndex) {
		TextClassify tc = new TextClassify();	
		
		List<String> trainFilesNamePos = getFileCorpus(dirPos, foldIndex, false);
		tc.addCorpus(trainFilesNamePos, TextClassifier.POSITIVE);
		
		List<String> trainFilesNameNeg = getFileCorpus(dirNeg, foldIndex, false);
		tc.addCorpus(trainFilesNameNeg, TextClassifier.NEGATIVE);
		
		return tc;
	}
	
	private TextClassTrainResult trainResult;
	private TextClassTestResult testResult;
	
	public TextClassify() {
		testResult = new TextClassTestResult();
	}

	public void setTrainResult(TextClassTrainResult tctr) {
		this.trainResult = tctr;
	}

	public TextClassTrainResult getTrainResult() {
		return trainResult;
	}

	public TextClassTestResult test() {
		for (int i = 0; i < this.getClassCnt(); i++) {
			BasicDataMap fileMap = this.files.get(i);
			Set<Entry<String, BasicStatisticData>> entries = fileMap.entrySet();
			for (Entry<String, BasicStatisticData> en: entries) {
				String fileName = en.getKey();
				BayesStatisticData bsd = calculateNaiveBayes(fileName);
				testResult.addResult(fileName, bsd);
			}
		}
		return testResult;
	}

	private BayesStatisticData calculateNaiveBayes(String fileName) {
		BayesStatisticData bsd = new BayesStatisticData();
		TextClassFile tcf = new TextClassFile(fileName);
		List<String> words = tcf.getWords();
		ItemClassifyingMap map = trainResult.getVocabularyMap();
		for (int i = 0; i < TextClassifier.getCount(); i++) {
			TextClassifier type = TextClassifier.getType(i);
			double probCNB = 1.0;
			for (String word: words) {
				BayesStatisticData wordSd = map.get(word);
				double prob = 0.0;	//only consider known words
				if (wordSd != null ) {
					prob = wordSd.getProbability(type);
				}
				probCNB += prob;	//prob is log
			}
			probCNB += trainResult.getProbability(type);
			bsd.setProbability(type, probCNB);
		}
		return bsd;
	}


}
