package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import basicFiles.DataFile;
import NgramLM.TestData;
import NgramLM.TrainingData;
import NgramLM.TrainingResultData;

public class NgramPerplexityTest {

	@Test
	public void getTestWords() {
		String fileName = "hw2_test.txt";
		DataFile df = new DataFile(fileName);
		List<String> words = df.getWords();
		int cnt = words.size();
		System.out.println("cnt=" + cnt);
		//assertTrue(cnt == 37);
	}
	
	@Test 
	public void getTestPairs() {
		String fileName = "hw2_test.txt";
		DataFile df = new DataFile(fileName);
		List<String> pairs = df.getPairs();
		int cnt = pairs.size();
		System.out.println("cnt=" + cnt);
		//assertTrue(cnt == 37);
	}
	
	private TrainingData getTrainingData() {
		String fileName = "hw2_train.txt";
		DataFile df = new DataFile(fileName);
		List<String> words = df.getWords();
		List<String> pairs = df.getPairs();
		TrainingData trd = new TrainingData(words, pairs);
		trd.training();
		return trd;
	}
	
	@Test
	public void testProbability() {
		//TrainingData trd = getTrainingData();
		TrainingResultData trd = new TrainingResultData();
		trd.learnFromResultFile("LM.txt");
		
		String fileNameTest = "hw2_test.txt";
		DataFile df = new DataFile(fileNameTest);
		List<String> words = df.getWords();
		List<String> pairs = df.getPairs();
		TestData td = new TestData(pairs, words.size());
		double ppl = td.test(trd);
		System.out.println(ppl);
	}

}
