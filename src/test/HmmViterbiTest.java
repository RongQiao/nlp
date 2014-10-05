package test;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import hmm.TagTest;
import hmm.TagTestEvaluate;
import hmm.TagTrainingResult;

import org.junit.Test;

import basic.TPair;
import basicFiles.TextFile;

public class HmmViterbiTest {

	@Test
	public void tagOneSentence() {
		String testFile = "hw3_test_00_oneSentence.txt";
		TagTrainingResult ttr = new TagTrainingResult();
		TagTest tt = new TagTest();
		tt.test(ttr, testFile);
		TextFile testResult = new TextFile("taged.txt");
		assertTrue(testResult.exists());
		TagTestEvaluate tte = new TagTestEvaluate();
		Map<String, TPair> dif = tte.evaluate("taged.txt", "taged_ref.txt");
		if (dif.size() > 0) {
			System.out.println(tte.getTotalAccurency());
			Set<Entry<String, TPair>> entries = dif.entrySet();
			for (Entry<String, TPair> en: entries) {
				TPair pr = en.getValue();
				System.out.println(en.getKey() + "/" + pr.getPair());
			}
		}
		assertTrue(dif.size() == 0);
	}

	@Test
	public void tagTest() {
		String testFile = "hw3_test_00.txt";
		TagTrainingResult ttr = new TagTrainingResult();
		TagTest tt = new TagTest();
		tt.test(ttr, testFile);
		TextFile testResult = new TextFile("taged.txt");
		assertTrue(testResult.exists());
		TagTestEvaluate tte = new TagTestEvaluate();
		Map<String, TPair> dif = tte.evaluate("taged.txt", "hw3_test_ref_00.txt");
		if (dif.size() > 0) {
			System.out.println(tte.getTotalAccurency());
			Set<Entry<String, TPair>> entries = dif.entrySet();
			for (Entry<String, TPair> en: entries) {
				TPair pr = en.getValue();
				System.out.println(en.getKey() + "/" + pr.getPair());
			}
		}
		assertTrue(dif.size() == 0);
	}

}
