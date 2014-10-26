package basic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BasicDataMap implements DataMapInterface{
	private Map<String, BasicStatisticData> map;
	private int totalCount;
	private double minProbability;
	
	public BasicDataMap() {
		map = new HashMap<String, BasicStatisticData>();
		totalCount = 0;
		minProbability = 0.0;
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
	public void setCount(String pair, int cnt) {
		BasicStatisticData bsd = map.get(pair);
		bsd.setCount(cnt);
		map.put(pair, bsd);
	}
	
	@Override
	public double getProbability(String key) {
		double ret = 0.0;
		if (containsKey(key)) {
			ret = map.get(key).getProbability();
		}
		return ret;
	}

	public void createKey(String key) {
		BasicStatisticData sd = new BasicStatisticData();
		sd.setCount(1);
		totalCount++;
		map.put(key, sd);
	}

	public void createKey(String key, BasicStatisticData value) {
		map.put(key, value);
	}
	
	public void increaseCount(String key) {
		BasicStatisticData bsd = map.get(key);
		int cnt = bsd.getCount();
		cnt++;
		totalCount++;
		bsd.setCount(cnt);
		map.put(key, bsd);
	}

	public Set<Entry<String, BasicStatisticData>> entrySet() {
		return map.entrySet();
	}

	public Map<String, BasicStatisticData> getMap() {		
		return map;
	}

	public int getCount() {
		return totalCount;
	}

	public BasicStatisticData get(String key) {
		return map.get(key);
	}

	public double getMinProbability() {
		if (Double.compare(minProbability, Double.MIN_NORMAL) < 0) {
			Set<Entry<String, BasicStatisticData>> entries = map.entrySet();
			for (Entry<String, BasicStatisticData> en: entries) {
				BasicStatisticData sd = en.getValue();
				if (sd.getCount() == 1) {
					minProbability = sd.getProbability();
					break;
				}
			}
		}
		return minProbability;
	}

	public int calculateCount() {
		int cnt = 0;
		Set<Entry<String, BasicStatisticData>> entries = map.entrySet();
		for (Entry<String, BasicStatisticData> en: entries) {
			BasicStatisticData sd = en.getValue();
			cnt += sd.getCount();
		}
		return cnt;
	}



}
