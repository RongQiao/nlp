package textClass;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import basic.BasicDataMap;
import basic.BasicStatisticData;
import basicFiles.DataFile;
import basicFiles.FileDepot;
import textClass.TextClassResult.TextClassifier;

public class TextClassTrain extends TextClassCorpus{
	public static void main(String[] args) {
		if (args.length < 5) {
			System.out.println("error: files not given.");
		}
		else {
			String dirPos = args[1] + "/pos";
			String dirNeg = args[1] + "/neg";
			String foldNum = args[2];
			String modelFileName = args[4];			
			int foldIndex = Integer.parseInt(foldNum);
			
			trainMain(dirPos, dirNeg, foldIndex, modelFileName);			
		}
	}

	
	public static void trainMain(String dirPos, String dirNeg, int foldIndex,
			String modelFileName) {
		TextClassTrain tt = new TextClassTrain();	
		

		List<String> trainFilesNamePos = TextClassCorpus.getFileCorpus(dirPos, foldIndex);
		tt.addCorpus(trainFilesNamePos, TextClassifier.POSITIVE);
		
		List<String> trainFilesNameNeg = TextClassCorpus.getFileCorpus(dirNeg, foldIndex);
		tt.addCorpus(trainFilesNameNeg, TextClassifier.NEGATIVE);
		
		TextClassTrainResult tr = tt.train();
		tr.outputTrainResultWord(modelFileName);
		int cnt = tr.getDocsCount();
		System.out.println("docs count: " + cnt);
		cnt = tr.getVocabularyCount();
		System.out.println("vocabulary count: " + cnt);
		double prob = tr.getProbability(TextClassifier.POSITIVE);
		System.out.println("pos classifier prob: " + prob);
		tr.getProbability(TextClassifier.NEGATIVE);
		System.out.println("neg classifier prob: " + prob);
	}


	//below list is based on classifier sequence 
	List<BasicStatisticData> categories;	
	TextClassTrainResult trainResult;
	
	public TextClassTrain() {
		super();
		int cnt = getClassCnt();
		categories = new ArrayList<BasicStatisticData>();
		for (int i = 0; i < cnt; i++) {
			categories.add(new BasicStatisticData());
		}
		trainResult = new TextClassTrainResult();
	}

	public TextClassTrainResult train() {
		calculateProb4Class();
		extractVocabulary();
		calculateProbBayes();
		return this.trainResult;
	}

	private void calculateProbBayes() {
		ItemClassifyingMap vMap = trainResult.getVocabularyMap();
		int vocabularyNum = vMap.getMap().size();		//|vocabulary|
		for (int i = 0; i < this.getClassCnt(); i++) {
			TextClassifier type = TextClassifier.getType(i);
			BasicDataMap fileMap = files.get(i);
			int totalCnt = fileMap.calculateCount();	//total # of words in docs j
			Set<Entry<String, BayesStatisticData>> entries = vMap.entrySet();
			for (Entry<String, BayesStatisticData> en: entries) {
				BayesStatisticData bsd = en.getValue();
				int cntOfWordInDocs = bsd.getCount(type);	//number of occurrences of word in docs j
				double prob = (double)(cntOfWordInDocs + 1) / (double) (totalCnt + vocabularyNum);
				prob = Math.log(prob)/Math.log(2);	//log
				bsd.setProbability(type, prob);
			}
		}
	}

	private void extractVocabulary() {
		ItemClassifyingMap vMap = new ItemClassifyingMap();
		for (int i = 0; i < this.getClassCnt(); i++) {
			TextClassifier type = TextClassifier.getType(i);
			BasicDataMap fnMap = this.files.get(i);
			Set<Entry<String, BasicStatisticData>> entries = fnMap.entrySet();
			for (Entry<String, BasicStatisticData> en: entries) {
				String fn = en.getKey();				//file name
				BasicStatisticData sd = en.getValue();	//data for the file
				TextClassFile tcFile = new TextClassFile(fn);
				tcFile.getWords(vMap, sd, type);
			}
		}
		//save to result
		trainResult.setVocabularyMap(vMap);
	}

	private void calculateProb4Class() {
		int totalCnt = 0;
		for (int i = 0; i < this.getClassCnt(); i++) {
			BasicStatisticData sd = categories.get(i);
			int cnt = this.files.get(i).getCount();	//docs count
			sd.setCount(cnt);		
			totalCnt += sd.getCount();
		}
		trainResult.setDocsCount(totalCnt);
		for (int i = 0; i < this.getClassCnt(); i++) {
			BasicStatisticData sd = categories.get(i);
			double prob = (double)sd.getCount() / (double)totalCnt;
			prob = Math.log(prob)/Math.log(2);
			sd.setProbability(prob);
		}
		trainResult.setClassProbability(categories);
	}

}
