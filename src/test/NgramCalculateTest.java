package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import basicFiles.DataFile;
import basicFiles.TextFile;
import NgramLM.DataMap;
import NgramLM.StatisticData;
import NgramLM.NgramTraining;

public class NgramCalculateTest {

	@Test
	public void getTrainWords1() {
		String fileName = "testfiles/testOneSentence.txt";
		DataFile df = new DataFile(fileName);
		List<String> words = df.getWords();
		int cnt = words.size();
		System.out.println("cnt=" + cnt);
		assertTrue(cnt == 37);
	}

	@Test
	public void hashMapWords() {
		int c[] = {10, 5, 8};
		String s[] = {"dog", "cat", "kid"};
		List<String> words = new ArrayList<String>();
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i]; j++) {
				words.add(s[i]);
			}
		}

		NgramTraining td = new NgramTraining();
		td.mapSingleWords(words);
		DataMap mp = td.getUniMap();
		for (int k = 0; k < s.length; k++) {			
			assertTrue(mp.getCount(s[k]) == c[k]);
		}
	}
	
	@Test
	public void unigramP() {
		int c[] = {10, 5, 8};
		String s[] = {"dog", "cat", "kid"};
		List<String> words = new ArrayList<String>();
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[i]; j++) {
				words.add(s[i]);
			}
		}
		
		NgramTraining td = new NgramTraining(words, null);
		td.mapWords();
		td.calculateUnigramProb();
		double p = td.getUnigProbability("dog");
		double r = (double)10/(double)23;
		assertTrue(Double.compare(p, r) == 0);
		td.outputLanguageModel();
		TextFile lmUnigram = new TextFile("testfiles/LM.txt");
		assertTrue(lmUnigram.exists());
	}
	
//	@Test
//	public void goodTurningEstimate() {
//		int c[] = {10, 1, 8, 1, 1, 2};
//		String s[] = {"dog", "cat", "kid", "pig", "May", "think"};
//		List<String> words = new ArrayList<String>();
//		for (int i = 0; i < c.length; i++) {
//			for (int j = 0; j < c[i]; j++) {
//				words.add(s[i]);
//			}
//		}
//		
//		NgramTraining td = new NgramTraining(words, null);
//		td.mapWords();
//		double Pgt = td.calculatePgt();
//		double r = (double)1/(double)3 * (double)2 / (double)23;
//		assertTrue(Double.compare(Pgt, r) == 0);
//	}
	
	/*
	 * let w1 = "w1", pairs:
	 * w1, w2	3
	 * w1, w7	5
	 * w1, w111 4
	 * w1, w200 1
	 * w1, w300	1
	 * w1, w489	6
	 */
	@Test
	public void calculatePair() {
		String fileName = "testfiles/pair.txt";
		DataFile df = new DataFile(fileName);
		List<String> pairs = df.getPairs();
		int cnt = pairs.size();
		assertTrue(cnt == (5+9+7+1+1+11));
	}
	
	@Test 
	public void biogramP() {
		String fileName = "testfiles/pair.txt";
		DataFile df = new DataFile(fileName);
		List<String> words = df.getWords();
		List<String> pairs = df.getPairs();
		NgramTraining td = new NgramTraining(words, pairs);
		td.mapWords();
		td.calculatePairCount();
		td.calculateBigramProb();
		double p = td.getBiogProbability("w1", "w2");
		double r = (double)3/(double)20;
		assertTrue(Double.compare(p, r) == 0);
	}
	
	@Test
	public void backOffWeight() {
		String fileName = "testfiles/pair.txt";
		DataFile df = new DataFile(fileName);
		List<String> words = df.getWords();
		List<String> pairs = df.getPairs();
		NgramTraining td = new NgramTraining(words, pairs);
		td.mapWords();
		td.calculateUnigramProb();
		td.calculatePairCount();
		td.calculateBigramProb();		
		td.calculateUnigramAlpha();
		double alpha = td.getAlpha("w1");
		/*
		 * 3/20 + 5/20 + 4/20 + 6/20 + 2*Pgt + Alpha(w)Sum(P(w)) = 1
		 * Sum(P(w)) = 1-(3/20 + 5/20 + 4/20 + 6/20 + 2*Pgt)
		 * double Pgt = (double)0/(double)2 * (double)2 / (double)40;
		 */
		double Pgt = (double)0/(double)2 * (double)2 / (double)40;
		double Pseen = (double)3/(double)20 + (double)5/(double)20 + (double)4/(double)20 + (double)6/(double)20 + 2 * Pgt;
		double Pml = (double)3/(double)40 + (double)5/(double)40 + (double)4/(double)40 + (double)6/(double)40 + 2 * (double)1/(double)40;
		double r = (1-Pseen)/(1-Pml) ;
		System.out.println("alpha = " + alpha + ", r =" + r + "," + Pgt + "," + Pseen + ", " + Pml);
		assertTrue(Double.compare(alpha-r, 0.00000001) < 0);
	}
	
	@Test
	public void unigramTraining() {
		String fileName = "testfiles/hw2_train.txt";
		DataFile df = new DataFile(fileName);
		List<String> words = df.getWords();
		List<String> pairs = df.getPairs();
		NgramTraining td = new NgramTraining(words, pairs);
		td.training();
		TextFile lmUnigram = new TextFile("testfiles/LM.txt");
		assertTrue(lmUnigram.exists());
	}
	
	@Test
	public void getTrainWords2() {
		String fileName = "testfiles/hw2_train.txt";
		DataFile df = new DataFile(fileName);
		List<String> words = df.getWords();
		int cnt = words.size();
		System.out.println("cnt=" + cnt);
		assertTrue(cnt == 608615);
	}
	
	@Test
	public void getTrainPairs() {
		String fileName = "testfiles/hw2_train.txt";
		DataFile df = new DataFile(fileName);
		List<String> pairs = df.getPairs();
		int cnt = pairs.size();
		System.out.println("cnt=" + cnt);
		//assertTrue(cnt == 608615);
	}
}
