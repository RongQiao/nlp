package calculate;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import basic.ValueComparator;

public class CalFreq {

	static Map<String, Integer> map = new HashMap<String, Integer>();
	static String fileName = "pride_and_prejudice.txt";
	//static String fileName = "sherlock_holmes.txt";
	
	public static void main(String[] args) throws IOException {
		InputWord();
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		int progress = 0;

		while (line != null) {
			line = br.readLine();
			if (line == null) {
				break;
			}
			
			if (!line.isEmpty()) {
				List<String> words = GetWords(line);
				for (String word: words) {
					if (map.containsKey(word) != false) {
						int count = map.get(word) + 1;
						map.put(word, count);
					}
				}
			}

		}
		fr.close();
		br.close();
		
		WriteWord(map);

	}

	// put the word into the map
	public static void InputWord() throws IOException {
		System.out.println("begin get the word");
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = new String();
		while (line != null) {
			line = br.readLine();
			if (line == null) {
				break;
			}
			//System.out.println(line);
			//get word from a line
			if (!line.isEmpty()) {
				List<String> words = GetWords(line);
				//for test
				for (String word: words) {
					if (word.length() == 1) {
						if (!((word.charAt(0) == 'a')
								|| (word.charAt(0) == 's')
								|| (word.charAt(0) == 't')
							|| (word.charAt(0) == 'i'))) {
							System.out.println(word + ":" + line);
						}
					}
					map.put(word, 0);
				}
			}
		}
		br.close();
		fr.close();
		System.out.println("get the word is ok");
	}

	//caculate hash value for a word
	public static int CaculateHash(String word) {
		int ret = 0;
		int cnt = word.length();
		char ch = ' ';
		for (int i=0; i < cnt; i++) {
			ch = word.charAt(i);
			ret = (int) (ret + ch * Math.pow(10, i));
		}
		return ret;
	}
	// Separate words from a string
	public static List<String> GetWords(String src) {
		 List<String> words = new ArrayList<String>();
		 while (!src.isEmpty()) {
			 char ch = src.charAt(0);
			 if (!isLetter(ch)) {
				 src = src.substring(1);
			 }
			 else {
				 String word = GetFirstWord(src);
				 if (!word.isEmpty()) {
					 if (!word.equalsIgnoreCase("I")) {
						 word = word.toLowerCase();
					 }
					 words.add(word);
				 }
				 src = src.substring(word.length());
			 }
		 }
		 return words;
	}
	
	// Check a character is Letter
	public static boolean isLetter(char ch) {
		boolean ret = false;
//		Pattern p = Pattern.compile("a-zA-Z");
//		String s = new String(" ");
//		s = s.replace(' ', ch);
//		Matcher m = p.matcher(s);
//		ret = m.matches();
		
		ret = ((ch >= 'A') && (ch <= 'Z')) 
				|| ((ch >= 'a') && (ch <= 'z'));
		
		return ret;
	}
	
	//precondition: the first ch of seq is letter
	public static String GetFirstWord(String seq) {
		int index = 0;
		String ret = new String();
		
		while (isLetter(seq.charAt(index))) {
			index++;
			if (index >= seq.length()) {
				index--;
				break;
			}
		}
		while (!isEndOfWord(seq, index)) {
			index++;
			if (index >= seq.length()) {
				//index--;
				break;
			}
		}
		
		ret = seq.substring(0, index);
		return ret;
	}
	
	public static boolean isEndPunctuation(char ch) {
		boolean ret = false;
		
		String punctuation = new String(",!?;:\"");
		if (punctuation.indexOf(ch) >= 0) {
			ret = true;
		}
		
		return ret;
	}
	
	public static boolean isEndOfWord(String seq, int ind) {
		boolean ret = false;
		
		if (ind >= seq.length()) {
			ret = true;
		}
		else {
			char ch = seq.charAt(ind);
			if ((ch == ' ')
					|| (ch == '\0')){
				ret = true;
			}			
			else {
				if (isEndPunctuation(ch)) {
					ret = true;
				}
				else if (ch == '.') {	
					if (!isAbbreviation(seq, ind)) {
						ret = true;
					}
				}
				else if (ch == '-') {
					int next = ind + 1;
					if (next < seq.length()) {
						if (seq.charAt(next) == '-') {
							ret = true;
						}
					}
				}
				else if (ch == '\'') {
					if (isSpecial(seq, ind)) {
						ret = false;
					}
					else {
						ret = true;
					}
				}
			}
		}

		return ret;
	}

	public static boolean isAbbreviation(String seq, int index) {
		boolean ret = false;
		
		int next = index + 1;
		if (next < seq.length()) {
			char chNext = seq.charAt(next);
			if (isLetter(chNext)) {
				ret = true;
			}
			else if (chNext == ' ') {
				String word = seq.substring(0, index);
				String abbSpecial[] = {"Mr", "Ms", "Mrs", "Dr"};
				for (int i = 0; i < abbSpecial.length; i++) {
					if (word.equalsIgnoreCase(abbSpecial[i])) {
						ret = true;
					}
				}
			}
		}
		
		return ret;
	}
	
	public static boolean isSpecial(String seq, int index) {
		boolean ret = false;
		
		int next = index + 1;
		if (next < seq.length()) {
			char chNext = seq.charAt(next);
			if (isLetter(chNext)) {
				int blank = next;
				while (blank < seq.length()) {
					if (seq.charAt(blank) != ' ') {
						blank++;
					}
					else {
						break;
					}
				}
				String word = seq.substring(index+1, blank);
				String strSpecial[] = {"t", "clock"};
				for (int i = 0; i < strSpecial.length; i++) {
					if (word.equalsIgnoreCase(strSpecial[i])) {
						ret = true;
					}
				}
			}
		}

		return ret;
	}
	// write word and frequency into the file
	public static void WriteWord(Map<String, Integer> outMap) throws IOException {
		System.out.println("begin write the word");
		File file = new File("wordfreq.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter("wordfreq.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		//sort map
		ValueComparator bvc = new ValueComparator(outMap);
		TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
		sorted_map.putAll(map);
		int sortIndext = 0;
		int occurStatics[] = {0, 0, 0};
		for (Map.Entry<String, Integer> entry : sorted_map.entrySet()) {
			String key = entry.getKey().toString();
			int value = entry.getValue();
			bw.write(++sortIndext + ". " + key + " " + value);
			if (value < 4) {
				occurStatics[value-1] = occurStatics[value-1] + 1;
			}
			bw.newLine();
			bw.flush();
		}
		fw.close();
		bw.close();
		System.out.println("occur once words: " + occurStatics[0]);
		System.out.println("occur twice words: " + occurStatics[1]);
		System.out.println("occur three times words: " + occurStatics[2]);
		System.out.println("write the word is ok");
	}
	

}
