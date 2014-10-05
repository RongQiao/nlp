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
		int spaceTgt = 0, spaceRef = 0;
		int i = 0; 	//i for tgtSt index
		int j = 0;	//j for refSt index
		while ((i < tgtCnt) && (j < refCnt)) {
			if (tgtSt.charAt(i) == ' ') {
				spaceTgt = i;	//keep the latest space index
			}
			if (refSt.charAt(j) == ' ') {
				spaceRef = j;
			}
			if (tgtSt.charAt(i) == refSt.charAt(j)) {
				i++;
				j++;
			}
			else {
				//s1[i] != s2[j], if one of them is Space, it means a new word, need go back to previous word
				if (tgtSt.charAt(i) == ' ') {
					spaceTgt = getBackSpaceIndex(tgtSt.getBytes(), i-1);
				}
				if (refSt.charAt(j) == ' ') {
					spaceRef = getBackSpaceIndex(refSt.getBytes(), j-1);
				}
				TPair pr = getDiffPairFoward(tgtSt, refSt, spaceTgt, spaceRef);
				String pairTgt = pr.getS1();
				String pairRef = pr.getS2();
				comparePair(pairTgt, pairRef);
				//cotinue compare the rest part
				i = spaceTgt + 1 + pairTgt.length();
				j = spaceRef + 1 + pairRef.length();
			}
		}
	}

	private int getBackSpaceIndex(byte[] bs, int currentSpace) {
		int ret = 0;
		for (int i = currentSpace; i >= 0; i--) {
			if ((char)bs[i] == ' ') {
				ret = i;
				break;
			}
		}
		return ret;
	}

	private TPair getDiffPairFoward(String tgtSt, String refSt, int spaceTgt, int spaceRef) {
		TPair pr = new TPair("$$$", "$$$");
		String subTgt = tgtSt.substring(spaceTgt+1);
		String subRef = refSt.substring(spaceRef+1);
		//compare the current different pair
		int sI = subTgt.indexOf(' ');
		if (sI > -1) {
		String pairTgt = subTgt.substring(0, sI);
		sI = subRef.indexOf(' ');
		String pairRef = subRef.substring(0, sI);
		pr = new TPair(pairTgt, pairRef);
		}
		//test
		else {
			System.out.println(tgtSt);
		}
		return pr;
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
