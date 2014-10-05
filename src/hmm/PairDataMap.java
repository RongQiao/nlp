package hmm;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import basic.BasicStatisticData;
import basic.DataMapInterface;

public class PairDataMap implements DataMapInterface{
	private Map<String, BasicStatisticData> map;
	private int totalCount;
	
	public PairDataMap() {
		map = new HashMap<String, BasicStatisticData>();
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

	public void createKey(String pair) {
		BasicStatisticData sd = new BasicStatisticData();
		sd.setCount(1);
		totalCount++;
		map.put(pair, sd);
	}

	public void createKey(String key, BasicStatisticData value) {
		map.put(key, value);
	}
	
	public void increaseCount(String pair) {
		BasicStatisticData bsd = map.get(pair);
		int cnt = bsd.getCount();
		cnt++;
		totalCount++;
		bsd.setCount(cnt);
		map.put(pair, bsd);
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



}
