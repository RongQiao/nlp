package basic;

public class TPair {
	private String s1;
	private String s2;
	private char separator;
	
	public TPair(String s1, String s2) {
		this.s1 = s1;
		this.s2 = s2;
		//set default separator as ' '
		setSeparator(' ');
	}
	
	public TPair(String str, char sep) {
		this.setSeparator(sep);
		int index = str.indexOf(sep);
		if (index > -1) {
		s1 = str.substring(0, index);
		s2 = str.substring(index+1, str.length());
		}
		//test
		else {
			System.out.println(str + "," + sep);
		}
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

	public String getPair() {
		String pr = getS1() + getSeparator() + getS2();
		return pr;
	}

}
