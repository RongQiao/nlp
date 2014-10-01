package hmm;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import basic.BasicStatisticData;
import basic.ResultParser;

public class PairResultParser extends ResultParser{

	public static String parse(String key, BasicStatisticData sd) {
		String line = key;
		line += getSeparator();
		line += sd.getProbability();
		
		return line;
	}

	public static Entry<String, BasicStatisticData> parse(String line) {
		List<String> items = new ArrayList<String>(); 
		putItemsToCollection(line, getSeparator(), items);
		String key = items.get(0) + getSeparator() + items.get(1);
		BasicStatisticData sd = new BasicStatisticData();
		sd.setProbability(Double.parseDouble(items.get(2)));
		Entry<String, BasicStatisticData> en = 
				new AbstractMap.SimpleImmutableEntry<String, BasicStatisticData>(key, sd);
		return en;
	}

}
