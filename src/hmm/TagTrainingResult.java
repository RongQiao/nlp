package hmm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Set;

import NgramLM.StatisticData;
import basic.BasicStatisticData;
import basic.ResultParser;
import basicFiles.TextFile;

public class TagTrainingResult extends TagData{
	public TagTrainingResult() {
		super();
	}

	public void learnTrainResultWord(String fileName) {
		learnUnitReault(fileName, wordMap);
	}
	
	public void learnTrainResultTag(String fileName) {
		learnUnitReault(fileName, tagMap);
	}
	
	private void learnUnitReault(String fileName, UnitDataMap map) {
		TextFile tf = new TextFile(fileName);
		List<String> lines = tf.readLines();
		for (String line: lines) {
			if (line.length() > 0) {
				Entry<String, WordTagStatisticData> record = WordResultParser.parse(line);
				map.createKey(record.getKey(), record.getValue());
			}
		}
	}

	public void learnTrainResultTransition(String fileName) {
		PairResultParser.setSeparator(ResultParser.DEFAULT_SEPARATOR);
		learnPairProbResult(fileName, tagTagPairMap);
	}

	public void learnTrainResultObservation(String fileName) {
		PairResultParser.setSeparator(ResultParser.DEFAULT_SEPARATOR);
		learnPairProbResult(fileName, wordTagPairMap);
	}
	
	private void learnPairProbResult(String fileName, PairDataMap map) {
		TextFile tf = new TextFile(fileName);
		List<String> lines = tf.readLines();
		for (String line: lines) {
			if (line.length() > 0) {
				Entry<String, BasicStatisticData> record = PairResultParser.parse(line);
				map.createKey(record.getKey(), record.getValue());
			}
		}
	}
	
	public void outputTagTagData(String fileName) {
		//sort map		
		TreeMap<String, BasicStatisticData> sorted_map = new TreeMap<String, BasicStatisticData>();
		sorted_map.putAll(tagTagPairMap.getMap());
		outputPairData(fileName, sorted_map);
	}

	public void outputWordTagData(String fileName) {
		//sort map		
		TreeMap<String, BasicStatisticData> sorted_map = new TreeMap<String, BasicStatisticData>();
		sorted_map.putAll(wordTagPairMap.getMap());
		outputPairData(fileName, sorted_map);
	}

	private void outputPairData(String fileName, Map<String, BasicStatisticData> map) {
		List<String> lines = new ArrayList<String>();
		Set<Entry<String, BasicStatisticData>> st = map.entrySet();
		for (Entry<String, BasicStatisticData> en: st) {
			String line = PairResultParser.parse(en.getKey(), en.getValue());
			lines.add(line);
		}
		writeDataToFile(fileName, lines);
	}
	
	public void outputTrainResultTag(String fileName) {
		//sort map		
		TreeMap<String, WordTagStatisticData> sorted_map = new TreeMap<String, WordTagStatisticData>();
		sorted_map.putAll(tagMap.getMap());
		outputTrainResultUnit(fileName, sorted_map);
	}
	
	public void outputTrainResultWord(String fileName) {
		//sort map		
		TreeMap<String, WordTagStatisticData> sorted_map = new TreeMap<String, WordTagStatisticData>();
		sorted_map.putAll(wordMap.getMap());
		outputTrainResultUnit(fileName, sorted_map);
	}
	
	private void outputTrainResultUnit(String fileName, Map<String, WordTagStatisticData> map) {
		List<String> lines = new ArrayList<String>();
		Set<Entry<String, WordTagStatisticData>> st = map.entrySet();
		for (Entry<String, WordTagStatisticData> en: st) {
			String line = WordResultParser.parse(en.getKey(), en.getValue());
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

	public void learnAllTrainingResult() {
		learnAllTrainingResult("hw3_model.txt");
	}
	
	public void learnAllTrainingResult(String modelFile) {
		String subName = modelFile;
		if (modelFile.indexOf(".") > 0) {
			subName = modelFile.substring(0, modelFile.indexOf("."));
		}
		learnTrainResultWord(subName + "_word.txt");
		learnTrainResultTag(subName + "_tag.txt");
		learnTrainResultTransition(subName + "_tag_tag.txt");
		learnTrainResultObservation(subName + "_word_tag.txt");
	}

	public int getTagCount() {
		return tagMap.entrySet().size();
	}


	public double getTagPairProb(String t2, String t1) {
		String key = t1 + ResultParser.getSeparator() + t2;
		return tagTagPairMap.getProbability(key);

	}




}
