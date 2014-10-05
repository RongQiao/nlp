package hmm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import NgramLM.StatisticData;
import basic.DataMapInterface;

public class UnitDataMap implements DataMapInterface{
	private Map<String, WordTagStatisticData> map;
	private int totalCount;
	
	public UnitDataMap() {
		map = new HashMap<String, WordTagStatisticData>();
		totalCount = 0;
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

	public void updateFollowed(String key, String tag) {
		WordTagStatisticData sd = map.get(key);

		//if it's a new tag, insert the new tag
		List<String> tags =sd.getTags();
		if (tag != null) {
			if (!tags.contains(tag)) {
				tags.add(tag);	
			}
		}
		map.put(key, sd);
	}
	
	public void updateKey(String key, String tag) {
		WordTagStatisticData sd = map.get(key);
		//key count +1, no matter tag
		sd.setCount(sd.getCount()+1);	
		totalCount++;
		//if it's a new tag, insert the new tag
		List<String> tags =sd.getTags();
		if (tag != null) {
			if (!tags.contains(tag)) {
				tags.add(tag);	
			}
		}
		map.put(key, sd);
	}

	public void createKey(String key, String tag) {
		WordTagStatisticData sd = new WordTagStatisticData();
		sd.setCount(1);
		totalCount++;
		if (tag != null) {
			List<String> tags =sd.getTags();
			tags.add(tag);
		}
		map.put(key, sd);		
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

	public Map<String, WordTagStatisticData> getMap() {
		return map;
	}

	//get total count of all map's key
	public int getCount() {
		return this.totalCount;
	}



}
