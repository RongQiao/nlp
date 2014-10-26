package textClass;

import java.util.ArrayList;
import java.util.List;

import textClass.TextClassResult.TextClassifier;
import basic.BasicDataMap;
import basic.BasicStatisticData;
import basicFiles.FileDepot;

public class TextClassCorpus {	
	//below list is based on classifier sequence 
	List<BasicDataMap> files;	
	
	public TextClassCorpus() {
		int cnt = getClassCnt();
		files = new ArrayList<BasicDataMap>();
		for (int i = 0; i < cnt; i++) {
			files.add(new BasicDataMap());	
		}
	}

	public void addCorpus(List<String> filesName,
			TextClassifier classifier) {
		BasicDataMap map = getMap(classifier);
		for (String fn: filesName) {
			map.createKey(fn);
		}
	}
	
	public BasicDataMap getMap(TextClassifier classifier) {
		int index = TextClassifier.getIndex(classifier);		
		return files.get(index);
	}
	
	public int getClassCnt() {
		return TextClassifier.values().length;
	}

	
	public static List<String> getFileCorpus(String dirName, int foldIndex) {		
		FileDepot fd = new FileDepot();
		fd.getAllFilesName(dirName);
		fd.splitFold(10);
		List<String> trainFilesName = fd.getFoldExcluse(foldIndex);
		return trainFilesName;
	}
	
	public static List<String> getFileCorpus(String dirName, int foldIndex,
			boolean b) {
		FileDepot fd = new FileDepot();
		fd.getAllFilesName(dirName);
		fd.splitFold(10);
		List<String> trainFilesName = fd.getFold(foldIndex);
		return trainFilesName;
	}


}
