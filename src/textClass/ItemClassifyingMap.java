package textClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import textClass.TextClassResult.TextClassifier;
import basic.DataMapInterface;

public class ItemClassifyingMap implements DataMapInterface{
	private Map<String, BayesStatisticData> map;
	private int totalCount;
	
	public ItemClassifyingMap() {
		map = new HashMap<String, BayesStatisticData>();
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

	public void createKey(String key, BayesStatisticData bsd) {
		map.put(key, bsd);
	}
	
	public void createKey(String key, TextClassifier type) {
		BayesStatisticData sd = new BayesStatisticData();
		sd.setCount(type, 1);
		totalCount++;
		map.put(key, sd);
	}
	
	public void increaseCount(String key, TextClassifier type) {
		BayesStatisticData bsd = map.get(key);
		int cnt = bsd.getCount(type);
		cnt++;
		totalCount++;
		bsd.setCount(type, cnt);
		map.put(key, bsd);
	}

	public Set<Entry<String, BayesStatisticData>> entrySet() {
		return map.entrySet();
	}

	public Map<String, BayesStatisticData> getMap() {		
		return map;
	}

	public int getCount() {
		return totalCount;
	}

	public BayesStatisticData get(String key) {
		return map.get(key);
	}

	@Override
	public void setCount(String key, int cnt) {
		// TODO Auto-generated method stub
		
	}



}
