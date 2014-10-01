package hmm;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import basic.ResultParser;


public class WordResultParser extends ResultParser{

	public static Entry<String, WordTagStatisticData> parse(String line) {
		WordTagStatisticData sd = new WordTagStatisticData();
		
		char sep = getSeparator();
		List<String> items = new ArrayList<String>();
		putItemsToCollection(line, sep, items);
		String key = items.get(0);
		sd.setCount(Integer.parseInt(items.get(1)));
		items.remove(0);
		items.remove(1);
		sd.setTags(items);
		
		Entry<String, WordTagStatisticData> en = 
				new AbstractMap.SimpleImmutableEntry<String, WordTagStatisticData>(key, sd);
		
		return en;
	}

	public static String parse(String key, WordTagStatisticData sd) {
		String line = key;
		char sep = getSeparator(); 
		line += sep;
		line += sd.getCount();
		line += sep;
		for (String tag: sd.getTags()) {
			line += tag;
			line += sep;
		}
		return line;
	}


}
