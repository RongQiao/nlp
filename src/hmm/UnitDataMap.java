package hmm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import basic.DataMapInterface;

public class UnitDataMap implements DataMapInterface{
	private Map<String, WordTagStatisticData> map;
	
	public UnitDataMap() {
		map = new HashMap<String, WordTagStatisticData>();
	}

	@Override
	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	@Override
	public int getCount(String key) {
		int ret = 0;
		if (containsKey(key)) {
			ret = map.get(key).getCount();
		}
		return ret;
	}

	@Override
	public double getProbability(String key) {
		double ret = 0.0;
		if (containsKey(key)) {
			ret = map.get(key).getProbability();
		}
		return ret;
	}

	public void updateKey(String word, String tag) {
		WordTagStatisticData sd = map.get(word);
		//word count +1, no matter tag
		sd.setCount(sd.getCount()+1);	
		//if it's a new tag, insert the new tag
		List<String> tags =sd.getTags();
		if (!tags.contains(tag)) {
			tags.add(tag);	
		}
		map.put(word, sd);
	}

	public void createKey(String word, String tag) {
		WordTagStatisticData sd = new WordTagStatisticData();
		sd.setCount(1);
		List<String> tags =sd.getTags();
		tags.add(tag);
		map.put(word, sd);
	}

	public void createKey(String key, WordTagStatisticData value) {
		map.put(key, value);
	}
	
	@Override
	public void setCount(String key, int cnt) {
		WordTagStatisticData sd = map.get(key);
		sd.setCount(cnt);
	}

	public Set<Entry<String, WordTagStatisticData>> entrySet() {
		return map.entrySet();
	}


}
