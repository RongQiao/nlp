package test;

import static org.junit.Assert.*;
import hmm.TagDataFile;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import basic.TPair;
import basicFiles.AbstractDataFile;
import basicFiles.DataFile;
import basicFiles.SentenceParser;
import basicFiles.TagSentenceParser;

public class HmmTrainTest {

	@Test
	public void getWords() {
		String fileName = "testOneSentenceTag.txt";		
		TagDataFile tdf = new TagDataFile(fileName);
		List<TPair> pairs = new ArrayList<TPair>(); 
		tdf.getWordTagPairs(pairs);
		int cnt = pairs.size();
		assertTrue(cnt == 32);
		TPair pr = pairs.get(0);
		for (int i = 0; i < cnt; i++) {
			TPair p = pairs.get(i);
			System.out.println(p.getS1() + "/" + p.getS2());
		}
		assertTrue(pr.getS1().equalsIgnoreCase("The"));
		assertTrue(pr.getS2().equalsIgnoreCase("DT"));
	}
}
