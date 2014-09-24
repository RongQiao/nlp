package NgramLM;

import java.util.HashMap;
import java.util.Map;

public class DataMapPair extends DataMap {
	private Map<String, PairStatisticData> map;
	
	public DataMapPair() {
		map = new HashMap<String, PairStatisticData>();
	}
	
	public void createKey(String key, int cnt) {
		PairStatisticData sd = new PairStatisticData();
		sd.setCount(cnt);
		map.put(key, sd);
	}
}
