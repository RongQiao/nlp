package basic;

public class TPair {
	private String s1;
	private String s2;
	private char separator;
	
	public TPair(String s1, String s2) {
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public TPair(String str, char sep) {
		this.setSeparator(sep);
		int index = str.indexOf(sep);
		s1 = str.substring(0, index);
		s2 = str.substring(index+1, str.length());
	}

	public String getS1() {
		return s1;
	}
	public void setS1(String s1) {
		this.s1 = s1;
	}
	public String getS2() {
		return s2;
	}
	public void setS2(String s2) {
		this.s2 = s2;
	}

	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

}
