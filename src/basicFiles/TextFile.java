package basicFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class TextFile extends File{
	public TextFile(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static int[] getCommaIndexs(String content, int size) {
		int[] indexs = new int[size];
		indexs[0] = 0;
		int i = 1;
		int indexComma = content.indexOf(',');
		while (indexComma >= 0) {			
			indexs[i++] = indexComma;
			indexComma = content.indexOf(',', indexComma+1);
		}
		return indexs;
	}
	
	public void write(String data) {
		writeFile(data, false);
	}
	
	public void append(String data) {
		writeFile(data, true);
	}
	
	private void writeFile(String data, boolean isAppend) {
		if (!exists()) {
			try {
				createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedWriter bfWriter = null;
		try {
			FileWriter flWriter = null;
			if (isAppend) {
				flWriter = new FileWriter(this, true);
			}
			else {
				flWriter = new FileWriter(this);
			}
			bfWriter = new BufferedWriter(flWriter);
			if (isAppend) {
				bfWriter.newLine();
			}
			bfWriter.write(data);
			bfWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (bfWriter != null) bfWriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public int read(List<String> buf) {	
		if (buf == null) {
			return 0;
		}
		buf.clear();
		if (exists()) {
			BufferedReader bfReader = null;
			try {
				FileReader flReader = new FileReader(this);
				bfReader = new BufferedReader(flReader);
				String line = null;
				while ((line = bfReader.readLine()) != null) {
					buf.add(line);
				}
				bfReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (bfReader != null) bfReader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return buf.size();
	}




}

