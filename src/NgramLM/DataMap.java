package NgramLM;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataMap {
	private Map<String, StatisticData> map;
	
	public DataMap() {
		map = new HashMap<String, StatisticData>();
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	public int getCount(String key) {
		StatisticData sd = map.get(key);
		int ret = 0;
		if (sd != null) {
			ret = sd.getCount();
		}
		return ret;
	}

	public double getProbability(String key) {
		StatisticData sd = map.get(key);
		double ret = 0;
		if (sd != null) {
			ret = sd.getProbability();
		}
		return ret;
	}
	
	public void renewKey(String key, int cnt) {
		StatisticData sd = map.get(key);
		sd.setCount(cnt);
		map.put(key, sd);
	}

	public void createKey(String key, int cnt) {
		StatisticData sd = new StatisticData();
		sd.setCount(cnt);
		map.put(key, sd);
	}

	public Set<Map.Entry<String, StatisticData>> entrySet() {
		return map.entrySet();
	}

	public double getAlpha(String key) {
		StatisticData sd = map.get(key);
		return sd.getAlpha();
	}

	public void increaseFellowCount(String key, int count) {
		StatisticData sd = map.get(key);
		sd.setFollowCount(sd.getFollowCount() + count);
	}

	public double getFollowCount(String key) {
		StatisticData sd = map.get(key);
		return sd.getFollowCount();
	}

	public void updateSumPairProb(String key, double probability) {
		StatisticData sd = map.get(key);
		double sumPairProb = sd.getSumPairProb();
		sumPairProb += probability;
		sd.setSumPairProb(sumPairProb);
	}

	public void updateBackOffPml(String key, double siProb) {
		StatisticData sd = map.get(key);
		double backoffPml = sd.getBackoffPml();
		backoffPml += siProb;
		sd.setBackoffPml(backoffPml);
	}

	public Map<String, StatisticData> getMap() {
		return this.map;
	}

	public void updateSumPairCount(String key, double newPairCount) {
		StatisticData sd = map.get(key);
		double sumCnt = sd.getSumPairCount();
		sumCnt += newPairCount;
		sd.setSumPairCount(sumCnt);
	}

	public boolean getSmoothSign(String key) {
		StatisticData sd = map.get(key);
		return sd.isNeedSmooth();
	}

	public boolean getGoodturningSign(String key) {
		StatisticData sd = map.get(key);
		return sd.isGoodTurning();
	}

	public void createKey(String key, StatisticData sd) {
		map.put(key, sd);
	}

	public double getLogAlpha(String key) {
		StatisticData sd = map.get(key);
		return sd.getLogAlpha();
	}
	
	public double getLogProbability(String key) {
		StatisticData sd = map.get(key);
		return sd.getLogProbability();
	}

}
