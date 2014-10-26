package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import basicFiles.FileDepot;
import textClass.TextClassTestResult;
import textClass.TextClassTrain;
import textClass.TextClassTrainResult;
import textClass.TextClassify;
import textClass.TextClassResult.TextClassifier;

public class TextClassifyTest {
	
	public static List<String> getFileCorpus(String dirName, int foldIndex) {		
		FileDepot fd = new FileDepot();
		fd.getAllFilesName(dirName);
		fd.splitFold(10);
		List<String> trainFilesName = fd.getFold(foldIndex);
		return trainFilesName;
	}
	
	public static TextClassify getTextClassify(int foldIndex) {
		TextClassify tc = new TextClassify();	
		
		List<String> trainFilesNamePos = getFileCorpus("txt_sentoken/pos", foldIndex);
		tc.addCorpus(trainFilesNamePos, TextClassifier.POSITIVE);
		
		List<String> trainFilesNameNeg = getFileCorpus("txt_sentoken/neg", foldIndex);
		tc.addCorpus(trainFilesNameNeg, TextClassifier.NEGATIVE);
		
		return tc;
	}

	private TextClassTestResult getRefResult(int foldIndex) {
		TextClassTestResult testRRef = new TextClassTestResult();
		List<String> trainFilesNamePos = getFileCorpus("txt_sentoken/pos", foldIndex);
		testRRef.addResult(trainFilesNamePos, TextClassifier.POSITIVE);
		List<String> trainFilesNameNeg = getFileCorpus("txt_sentoken/neg", foldIndex);
		testRRef.addResult(trainFilesNameNeg, TextClassifier.NEGATIVE);
		return testRRef;
	}
	
	@Test
	public void fold0Test() {
		int foldIndex = 0;
		TextClassTrain tt = TextClassTrainTest.getTextClassTrain(foldIndex);
		TextClassTrainResult tctr = tt.train();
		TextClassify tc = getTextClassify(foldIndex);
		tc.setTrainResult(tctr);
		TextClassTestResult testR = tc.test();
		TextClassTestResult testRRef = getRefResult(foldIndex);
		double ac = testR.calculateAccuracy(testRRef);
		System.out.println(ac);
	}
	
	@Test
	public void fold1TestModelfile() {
		int foldIndex = 0;
		TextClassTrainResult tctr = new TextClassTrainResult();
		tctr.learnTrainResult("txmodel.txt");
		TextClassify tc = getTextClassify(foldIndex);
		tc.setTrainResult(tctr);
		TextClassTestResult testR = tc.test();
		TextClassTestResult testRRef = getRefResult(foldIndex);
		double ac = testR.calculateAccuracy(testRRef);
		System.out.println(ac);
	}
	
	@Test
	public void _10foldTest() {
		int foldIndex = 0;
		for (int i = 0; i < 10; i++) {
			foldIndex = i;
			TextClassTrain tt = TextClassTrainTest.getTextClassTrain(foldIndex);
			TextClassTrainResult tctr = tt.train();
			TextClassify tc = getTextClassify(foldIndex);
			tc.setTrainResult(tctr);
			TextClassTestResult testR = tc.test();
			TextClassTestResult testRRef = getRefResult(foldIndex);
			double ac = testR.calculateAccuracy(testRRef);
			System.out.println(ac);
		}
	}

	public class person{
		String name = "";
		public person(String n) {name = n;}
	}
	public class e extends person{
		String name = "";
		public e(String n) {super(n);name = n;}
	}
}
