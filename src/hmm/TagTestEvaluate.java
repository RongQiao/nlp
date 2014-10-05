package hmm;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import basic.TPair;
import basicFiles.TextFile;

public class TagTestEvaluate {
	private Map<String, TPair> differenceMap;	//for same word, different tag
	private double totalAccurency;
	
	public TagTestEvaluate() {
		differenceMap = new TreeMap<String, TPair>();
	}

	public Map<String, TPair> evaluate(String targetFileName, String refFileName) {
		TextFile targetFile = new TextFile(targetFileName);
		TextFile refFile = new TextFile(refFileName);
		List<String> target = targetFile.readLines();
		List<String> ref = refFile.readLines();
		compare(target, ref);		
		calculateAccurency(refFileName);
		return getDifferenceMap();
	}
	
	private void calculateAccurency(String refFileName) {
		TagTestRefFile refFile = new TagTestRefFile(refFileName);
		TagTraining tt = new TagTraining(refFile);
		tt.trainWithoutOutput();
		int wordTagPairCnt = tt.wordTagPairMap.entrySet().size();
		int diffCnt = differenceMap.size();
		totalAccurency = 1 - (double)diffCnt/(double)wordTagPairCnt;
	}

	public void compare(List<String> target, List<String> ref) {
		differenceMap.clear();
		for (int i = 0; i < target.size(); i++) {
			compareSentence(target.get(i), ref.get(i));
		}
	}


	private void compareSentence(String tgtSt, String refSt) {
		tgtSt += ' ';
		refSt += ' ';
		int tgtCnt = tgtSt.length();
		int refCnt = refSt.length();
		int len = Math.min(tgtCnt, refCnt);
		int spaceIndex = 0;
		for (int i = 0; i < len; i++) {
			if (tgtSt.charAt(i) == ' ') {
				spaceIndex = i;	//keep the latest space index
			}
			if (tgtSt.charAt(i) != refSt.charAt(i)) {
				String subTgt = tgtSt.substring(spaceIndex+1);
				String subRef = refSt.substring(spaceIndex+1);
				//compare the current different pair
				spaceIndex = subTgt.indexOf(' ');
				String pairTgt = subTgt.substring(0, spaceIndex);
				subTgt = subTgt.substring(spaceIndex+1);
				spaceIndex = subRef.indexOf(' ');
				String pairRef = subRef.substring(0, spaceIndex);
				subRef = subRef.substring(spaceIndex+1);
				comparePair(pairTgt, pairRef);
				//cotinue compare the rest part
				compareSentence(subTgt, subRef);
				break;
			}
		}
	}

	private void comparePair(String pairTgt, String pairRef) {
		TPair tgt = new TPair(pairTgt, DummyItems.TAG_SEPERATOR);
		TPair ref = new TPair(pairRef, DummyItems.TAG_SEPERATOR);
		if (tgt.getS1().equalsIgnoreCase(ref.getS1())) {
			TPair tagPair = new TPair(tgt.getS2(), ref.getS2());
			differenceMap.put(tgt.getS1(), tagPair);
		}
	}

	public double getTotalAccurency() {
		return totalAccurency;
	}

	public Map<String, TPair> getDifferenceMap() {
		return differenceMap;
	}


}
