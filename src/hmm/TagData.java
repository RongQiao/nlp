package hmm;

public abstract class TagData {
	public UnitDataMap wordMap;	//record every word: count, and possible tags
	public UnitDataMap tagMap;		//record every tag: count, and possible followed tags
	public PairDataMap wordTagPairMap;	//record word tag pair, such as "The/DT"
	public PairDataMap tagTagPairMap;	//record tag tag pair, such as "DT NN"

	public TagData() {
		wordMap = new UnitDataMap();
		tagMap = new UnitDataMap();		
		wordTagPairMap = new PairDataMap();
		tagTagPairMap = new PairDataMap();		
	}

}
