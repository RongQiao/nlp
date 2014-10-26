package textClass;

import java.util.ArrayList;
import java.util.List;

import basic.BasicStatisticData;
import basic.ResultParser;
import textClass.TextClassResult.TextClassifier;

public class TextClassResultParser extends ResultParser{

	public static String parse(String key, BayesStatisticData sd) {
		String line = new String();
		char sep = getSeparator(); 
		double prob = sd.getProbability(TextClassifier.POSITIVE);
		//line += sd.getCount(TextClassifier.POSITIVE);
		//line += sep;
		line += prob;
		line += sep;
		prob = sd.getProbability(TextClassifier.NEGATIVE);
		//line += sd.getCount(TextClassifier.NEGATIVE);
		//line += sep;
		line += prob;
		line += sep;
		line += key;
		return line;
	}

	public static String parse(String line, String word, BayesStatisticData sd) {
		String s[] = new String[2];
		for (int i = 0; i < 2; i++) {
			int index = line.indexOf(DEFAULT_SEPARATOR);
			s[i] = line.substring(0, index);
			line = line.substring(index+1);
		}
		word = line;
		double prob = Double.parseDouble(s[0]);
		sd.setProbability(TextClassifier.POSITIVE, prob);
		prob = Double.parseDouble(s[1]);
		sd.setProbability(TextClassifier.NEGATIVE, prob);
		return word;
	}

	public static List<String> parse(List<BasicStatisticData> categories) {
		List<String> lines = new ArrayList<String>();
		char sep = getSeparator();
		for (int i = 0; i < categories.size(); i++) {
			String line = new String();
			//line += i;
			//line += sep;
			line += categories.get(i).getProbability();
			lines.add(line);
		}
		return lines;
	}

	public static void parse(List<String> lines,
			List<BasicStatisticData> categories) {
		char sep = getSeparator();
		for (int i = 0; i < lines.size(); i++) {
			BasicStatisticData cSd = new BasicStatisticData();
			cSd.setProbability(Double.parseDouble(lines.get(i)));
			categories.add(cSd);
		}
	}

}
