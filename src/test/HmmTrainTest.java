package test;

import static org.junit.Assert.*;
import hmm.TagTrainFile;
import hmm.TagTraining;
import hmm.TagTrainingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import NgramLM.NgramTrainingResult;
import basic.TPair;
import basicFiles.AbstractDataFile;
import basicFiles.DataFile;
import basicFiles.SentenceParser;
import basicFiles.TagSentenceParser;
import basicFiles.TextFile;

public class HmmTrainTest {

	@Test
	public void getPairs1() {
		String fileName = "testfiles/testOneSentenceTag.txt";		
		TagTrainFile tdf = new TagTrainFile(fileName);
		List<TPair> pairs = new ArrayList<TPair>(); 
		tdf.getWordTagPairs(pairs);
		int cnt = pairs.size();
		assertTrue(cnt == 34);
		TPair pr = pairs.get(1);
		assertTrue(pr.getS1().equalsIgnoreCase("The"));
		assertTrue(pr.getS2().equalsIgnoreCase("DT"));
	}
	
	@Test
	public void getPairs2() {
		String fileName = "testfiles/testOneSentenceTag.txt";		
		TagTrainFile tdf = new TagTrainFile(fileName);
		List<TPair> pairs = new ArrayList<TPair>(); 
		tdf.getTagPairs(pairs);
		int cnt = pairs.size();
		assertTrue(cnt == 33);
		TPair pr = pairs.get(1);
		for (int i = 0; i < cnt; i++) {
			TPair p = pairs.get(i);
			System.out.println(p.getS1() + " " + p.getS2());
		}
		assertTrue(pr.getS1().equalsIgnoreCase("DT"));
		assertTrue(pr.getS2().equalsIgnoreCase("NN"));
	}	

	@Test
	public void getTrainInfo() {
		String fileName = "testfiles/testOneSentenceTag.txt";		
		TagTrainFile tdf = new TagTrainFile(fileName);
		TagTraining tt = new TagTraining(tdf);
		tt.train();
		TextFile word = new TextFile("testfiles/hw3_word.txt");
		assertTrue(word.exists());
		TextFile wordTag = new TextFile("testfiles/hw3_word_tag.txt");
		assertTrue(wordTag.exists()); 
		TextFile tagTag = new TextFile("testfiles/hw3_tag_tag.txt");
		assertTrue(tagTag.exists()); 
	}
	
	@Test
	public void getTrainResult() {
		String fileName = "testfiles/testOneSentenceTag.txt";		
		TagTrainFile tdf = new TagTrainFile(fileName);
		TagTraining tt = new TagTraining(tdf);
		tt.train();
		TagTrainingResult ttr = new TagTrainingResult();
		ttr.learnTrainResultTag("testfiles/hw3_tag.txt");
		ttr.learnTrainResultWord("testfiles/hw3_word.txt");
		ttr.learnTrainResultTransition("testfiles/hw3_tag_tag.txt");
		ttr.learnTrainResultObservation("testfiles/hw3_word_tag.txt");
	}
	
	@Test
	public void getTrainInfo1() {
		String fileName = "testfiles/hw3_train.txt";		
		TagTrainFile tdf = new TagTrainFile(fileName);
		TagTraining tt = new TagTraining(tdf);
		tt.train();
		TextFile word = new TextFile("testfiles/hw3_word.txt");
		assertTrue(word.exists());
		TextFile wordTag = new TextFile("testfiles/hw3_word_tag.txt");
		assertTrue(wordTag.exists()); 
		TextFile tagTag = new TextFile("testfiles/hw3_tag_tag.txt");
		assertTrue(tagTag.exists()); 
	}
}
