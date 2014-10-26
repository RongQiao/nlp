package textClass;

import hmm.WordResultParser;
import hmm.WordTagStatisticData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import basic.BasicStatisticData;
import basicFiles.TextFile;

public class TextClassTrainResult extends TextClassResult{

	ItemClassifyingMap vocabularyMap;

	public int getVocabularyCount() {
		return vocabularyMap.getMap().size();
	}

	public void learnTrainResult(String fileName) {
		categories = new ArrayList<BasicStatisticData>();
		vocabularyMap = new ItemClassifyingMap();
		
		TextFile modelFile = new TextFile(fileName);
		List<String> lines = modelFile.readLines();
		int classCnt = TextClassifier.getCount();
		TextClassResultParser.parse(lines.subList(0, classCnt), categories);
		for (int i = 0; i < classCnt; i++) {
			lines.remove(0);
		}
		for (String line: lines) {
			BayesStatisticData sd = new BayesStatisticData();
			String word = new String();
			word = TextClassResultParser.parse(line, word, sd);
			vocabularyMap.createKey(word, sd);
		}
	}

	public void setVocabularyMap(ItemClassifyingMap vMap) {
		vocabularyMap = vMap;
	}

	public ItemClassifyingMap getVocabularyMap() {
		return this.vocabularyMap;
	}

	public void outputTrainResultWord(String fileName) {
		List<String> lines = new ArrayList<String>();
		lines.addAll(TextClassResultParser.parse(categories));
		ItemClassifyingMap map = getVocabularyMap();
		Set<Entry<String, BayesStatisticData>> entries = map.getMap().entrySet();
		for (Entry<String, BayesStatisticData> en: entries) {
			String line = TextClassResultParser.parse(en.getKey(), en.getValue());
			lines.add(line);
		}
		writeDataToFile(fileName, lines);
	}
	
	private void writeDataToFile(String fileName, List<String> content) {
		TextFile outFile = new TextFile(fileName);
		if (outFile.exists()) {
			outFile.delete();
		}
		try {
			outFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outFile.write(content);
	}

	public double getProbability(TextClassifier type) {
		int index = TextClassifier.getIndex(type);
		BasicStatisticData sd = categories.get(index);
		double prob = sd.getProbability();
		//the stored prob is log(prob), so we need re-calculate it
		prob = Math.pow(2, prob);
		return prob;
	}

	public void setClassProbability(List<BasicStatisticData> categories) {
		this.categories = categories;
	}

}
