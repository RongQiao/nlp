package basicFiles;

import java.util.List;

public abstract class AbstractDataFile extends TextFile{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AbstractDataFile(String fileName) {
		super(fileName);
	}
	public abstract List<String> getWords();
	public abstract List<String> getPairs();
}
