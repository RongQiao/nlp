package basicFiles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * a word based file: white space is the separator between two words
 */
public class SentenceBasedFile extends TextFile{
	private SentenceParser stParser;

	public SentenceBasedFile(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public SentenceParser getStParser() {
		return stParser;
	}

	public void setStParser(SentenceParser stParser) {
		this.stParser = stParser;
	}
	
	public List<String> readWords() {
		List<String> buf = new ArrayList<String>();
		List<String> lines = readLines();
		if (lines != null) {
			
		}
		return buf;
	}

}
