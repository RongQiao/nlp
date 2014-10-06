package test;

import static org.junit.Assert.*;

import java.util.List;
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
		tte.setTrainingResult(ttr);
		Map<String, TPair> dif = tte.evaluate("taged.txt", "taged_ref.txt");
		tte.outputAccuracy();
		if (dif.size() > 0) {
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
		tte.setTrainingResult(ttr);
		Map<String, TPair> dif = tte.evaluate("taged.txt", "hw3_test_ref_00.txt");
		if (dif.size() > 0) {
			tte.outputAccuracy();
			Set<Entry<String, TPair>> entries = dif.entrySet();
			for (Entry<String, TPair> en: entries) {
				TPair pr = en.getValue();
				System.out.println(en.getKey() + "/" + pr.getPair());
			}
		}
		assertTrue(dif.size() == 0);
	}

	@Test
	public void tagTestEvaluate() {
		TagTrainingResult ttr = new TagTrainingResult();
		ttr.learnAllTrainingResult();
		TagTestEvaluate tte = new TagTestEvaluate();
		tte.setTrainingResult(ttr);
		Map<String, TPair> dif = tte.evaluate("taged.txt", "hw3_test_ref_00.txt");
		if (dif.size() > 0) {
			tte.outputAccuracy();
			Set<Entry<String, TPair>> entries = dif.entrySet();
			for (Entry<String, TPair> en: entries) {
				String word = en.getKey();
				TPair pr = en.getValue();
				System.out.println(word + "/" + pr.getPair() + 
						(ttr.wordMap.containsKey(word) ? "" : " unseen"));
			}
		}
		assertTrue(dif.size() == 0);
	}
	
	@Test
	public void tagTestEvaluate2() {
		TagTrainingResult ttr = new TagTrainingResult();
		ttr.learnAllTrainingResult();
		TagTestEvaluate tte = new TagTestEvaluate();
		tte.setTrainingResult(ttr);
		TextFile t = new TextFile("taged.txt");
		TextFile r = new TextFile("hw3_test_ref_00.txt");		
		List<String> ts = t.readLines();
		List<String> rs = r.readLines();
		for (int i = 0; i < rs.size(); i++) {
			TextFile tsf = new TextFile("ts.txt");
			TextFile rsf = new TextFile("rs.txt");
			tsf.write(ts.get(i));
			rsf.write(rs.get(i));
			Map<String, TPair> dif = tte.evaluate("ts.txt", "rs.txt");
			if (dif.size() > 0) {
				if (tte.getTotalAccuracy()<0.9) {
					System.out.println(ts.get(i));
					System.out.println(rs.get(i));
					tte.outputAccuracy();
				}
				Set<Entry<String, TPair>> entries = dif.entrySet();
				for (Entry<String, TPair> en: entries) {
					String word = en.getKey();
					TPair pr = en.getValue();
					System.out.println(word + "/" + pr.getPair() + 
							(ttr.wordMap.containsKey(word) ? "" : " unseen"));
				}
			}
		}
	}
}
