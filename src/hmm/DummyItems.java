package hmm;

public class DummyItems {
	public static final String DUMMY_START = "<s>";
	public static final String DUMMY_END = "<.s>";
	public static final char TAG_SEPERATOR = '/';
	private static final char SPACE_SEPERATOR = ' ';
	
	public static String getDummyStart() {
		return DUMMY_START + SPACE_SEPERATOR;
	}
	
	public static String getDummyStartPair() {
		return DUMMY_START + TAG_SEPERATOR + DUMMY_START + SPACE_SEPERATOR;
	}
	
	public static String getDummyEnd() {
		return SPACE_SEPERATOR + DUMMY_END;
	}
	
	public static String getDummyEndPair() {
		return SPACE_SEPERATOR + DUMMY_END + TAG_SEPERATOR + DUMMY_END;
	}
}
