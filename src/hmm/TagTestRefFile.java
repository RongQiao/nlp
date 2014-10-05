package hmm;

public class TagTestRefFile extends TagTrainFile{

	public TagTestRefFile(String pathname) {
		super(pathname);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getDummyStart() {
		return "";
	}
	
	public String getDummyStartPair() {
		return "";
	}
	
	public String getDummyEnd() {
		return "";
	}
	
	public String getDummyEndPair() {
		return "";
	}
}
