package basicFiles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * a word based file: white space is the separator between two words
 */
public class WordBasedFile extends TextFile{

	public WordBasedFile(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public List<String> readWords() {
		List<String> buf = new ArrayList<String>();
		List<String> lines = readLines();
		if (lines != null) {
			
		}
		return buf;
	}

}
