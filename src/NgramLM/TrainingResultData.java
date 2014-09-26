package NgramLM;

import basicFiles.TextFile;

public class TrainingResultData {
	public DataMap uniMap;
	public DataMap biMap;
	
	public TrainingResultData() {
		uniMap = new DataMap();
		biMap = new DataMap();
	}
	public String getS1(String pair) {
		return pair.substring(0, pair.indexOf(' '));
	}
	
	public String getS2(String pair) {
		int indexStart = pair.indexOf(' ');
		indexStart += 1;
		return pair.substring(indexStart, pair.length());
	}
	
	/*
	 * P(s2|s1) means prob for pair "s1 s2"
	 */
	public double getBiogProbability(String s1, String s2) {
		String pair = s1 + " " + s2;
		return biMap.getProbability(pair);
	}

	public DataMap getUniMap() {
		return this.uniMap;
	}

	public DataMap getBiMap() {
		return this.biMap;
	}
	
	public void learnFromResultFile(String fileName) {
		LMFile lmFile = new LMFile(fileName);
		lmFile.getLMResult(uniMap, biMap);
	}

}
