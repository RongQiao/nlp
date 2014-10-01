package hmm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import basic.BasicStatisticData;
import basic.ResultParser;
import basicFiles.TextFile;

public class TagTrainingResult extends TagData{
	public TagTrainingResult() {
		super();
	}

	public void learnTrainResultWord(String fileName) {
		TextFile tf = new TextFile(fileName);
		List<String> lines = tf.readLines();
		for (String line: lines) {
			if (line.length() > 0) {
				Entry<String, WordTagStatisticData> record = WordResultParser.parse(line);
				wordMap.createKey(record.getKey(), record.getValue());
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
	
	public void outPutTagTagData(String fileName) {
		outputPairData(fileName, tagTagPairMap);
	}

	public void outPutWordTagData(String fileName) {
		outputPairData(fileName, wordTagPairMap);
	}

	private void outputPairData(String fileName, PairDataMap map) {
		List<String> lines = new ArrayList<String>();
		Set<Entry<String, BasicStatisticData>> st = map.entrySet();
		for (Entry<String, BasicStatisticData> en: st) {
			String line = PairResultParser.parse(en.getKey(), en.getValue());
			lines.add(line);
		}
		writeDataToFile(fileName, lines);
	}
	
	public void outputTrainResultTag(String fileName) {
		outputTrainResultUnit(fileName, tagMap);
	}
	
	public void outputTrainResultWord(String fileName) {
		outputTrainResultUnit(fileName, wordMap);
	}
	
	private void outputTrainResultUnit(String fileName, UnitDataMap map) {
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
		if (!outFile.exists()) {
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		outFile.write(content);
	}


}
