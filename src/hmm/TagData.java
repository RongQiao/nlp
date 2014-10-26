package hmm;

import basic.BasicDataMap;
import basic.UnitDataMap;

public abstract class TagData {
	public UnitDataMap wordMap;	//record every word: count, and possible tags
	public UnitDataMap tagMap;		//record every tag: count, and possible followed tags
	public BasicDataMap wordTagPairMap;	//record word tag pair, such as "The/DT"
	public BasicDataMap tagTagPairMap;	//record tag tag pair, such as "DT NN"

	public TagData() {
		wordMap = new UnitDataMap();
		tagMap = new UnitDataMap();		
		wordTagPairMap = new BasicDataMap();
		tagTagPairMap = new BasicDataMap();		
	}

}
