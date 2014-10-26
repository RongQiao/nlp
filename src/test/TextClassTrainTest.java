package test;

import static org.junit.Assert.*;
import hmm.TagTrainFile;
import hmm.TagTraining;
import hmm.TagTrainingResult;

import java.util.List;

import org.junit.Test;

import textClass.TextClassTrain;
import textClass.TextClassTrainResult;
import textClass.TextClassResult.TextClassifier;
import basicFiles.DataFile;
import basicFiles.FileDepot;

public class TextClassTrainTest {

	@Test
	public void getWordOneDoc() {
		String fileName = "txt_sentoken/pos/cv000_29590.txt";
		DataFile df = new DataFile(fileName);
		List<String> words = df.getWords();
		int cnt = words.size();
		System.out.println("cnt=" + cnt);
	}
	
	public static List<String> getFileCorpus(String dirName, int foldIndex) {		
		FileDepot fd = new FileDepot();
		fd.getAllFilesName(dirName);
		fd.splitFold(10);
		List<String> trainFilesName = fd.getFoldExcluse(foldIndex);
		return trainFilesName;
	}
	
	public static TextClassTrain getTextClassTrain(int foldIndex) {
		TextClassTrain tt = new TextClassTrain();	
		
		List<String> trainFilesNamePos = getFileCorpus("txt_sentoken/pos", foldIndex);
		tt.addCorpus(trainFilesNamePos, TextClassifier.POSITIVE);
		
		List<String> trainFilesNameNeg = getFileCorpus("txt_sentoken/neg", foldIndex);
		tt.addCorpus(trainFilesNameNeg, TextClassifier.NEGATIVE);
		
		return tt;
	}

	@Test
	public void trainResultTest() {
		TextClassTrain tt = getTextClassTrain(0);		
		TextClassTrainResult tr = tt.train();
		tr.outputTrainResultWord("tc.txt");
		int cnt = tr.getDocsCount();
		System.out.println("docs count: " + cnt);
		assertTrue(cnt == 1800);
		cnt = tr.getVocabularyCount();
		System.out.println("vocabulary count: " + cnt);
		double prob = tr.getProbability(TextClassifier.NEGATIVE);
		System.out.println("classifier prob: " + prob);
//		TextClassTrainResult tctr = new TextClassTrainResult();
//		tctr.learnTrainResult("tc.txt");
//		cnt = tctr.getDocsCount();
//		assertTrue(cnt == 1800);
	}
}
