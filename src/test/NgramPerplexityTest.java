package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import basicFiles.DataFile;
import NgramLM.NgramTest;
import NgramLM.NgramTraining;
import NgramLM.NgramTrainingResult;

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
	
	private NgramTraining getNgramTraining() {
		String fileName = "hw2_train.txt";
		DataFile df = new DataFile(fileName);
		List<String> words = df.getWords();
		List<String> pairs = df.getPairs();
		NgramTraining trd = new NgramTraining(words, pairs);
		trd.training();
		return trd;
	}
	
	@Test
	public void testProbability() {
		//NgramTraining trd = getNgramTraining();
		NgramTrainingResult trd = new NgramTrainingResult();
		trd.learnFromResultFile("LM.txt");
		
		String fileNameTest = "hw2_test.txt";
		DataFile df = new DataFile(fileNameTest);
		List<String> words = df.getWords();
		List<String> pairs = df.getPairs();
		NgramTest td = new NgramTest(pairs, words.size());
		double ppl = td.test(trd);
		System.out.println(ppl);
	}

}
