package basic;

import java.util.Collection;

public class ResultParser {
	public static final char DEFAULT_SEPARATOR = ' ';
	private static char separator = DEFAULT_SEPARATOR;	//default value

	public static char getSeparator() {
		return separator;
	}

	public static void setSeparator(char sep) {
		separator = sep;
	}
	
	public static void putItemsToCollection(String content, char sep, Collection<String> container) {
		int len = content.length();
		char buf[] = new char[100];		
		
		String str = new String();
		if (len > 0) {
			byte data[] = new byte[len + 1];
			System.arraycopy(content.getBytes(), 0, data, 0, len);
			data[data.length-1] = (byte) sep;
			int j = 0;
			for (int i = 0; i < data.length; i++) {
				char ch = (char)data[i];
				if ((ch != sep)) {
					buf[j++] = ch;
				}
				else {								
					str = new String(buf, 0, j);
					container.add(str);
					j = 0;
				}

			}		
		}
	}
}
