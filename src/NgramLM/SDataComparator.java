package NgramLM;

import java.util.Comparator;
import java.util.Map;

public class SDataComparator implements Comparator<String>{

	Map<String, StatisticData> base;
    public SDataComparator(Map<String, StatisticData> base) {
        this.base = base;
    }
    
	@Override
	public int compare(String a, String b) {
		StatisticData sda = base.get(a);
		StatisticData sdb = base.get(b);
		if (sda.getCount() >= sdb.getCount()) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
	}

}
