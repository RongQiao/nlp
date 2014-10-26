package textClass;

import java.util.List;

import basic.BasicStatisticData;
import textClass.TextClassResult.TextClassifier;

public class TextClassResult {
	public enum TextClassifier{
		POSITIVE,
		NEGATIVE;

		public static int getIndex(TextClassifier type) {
			int index = 0;
			switch (type) {
			case POSITIVE:
				index = 0;
				break;
			case NEGATIVE:
				index = 1;
				break;
			default:
				break;
			}
			return index;
		}

		public static int getCount() {
			return 2;
		}

		public static TextClassifier getType(int index) {
			TextClassifier type = POSITIVE;
			switch (index) {
			case 0:
				type = POSITIVE;
				break;
			case 1:
				type = NEGATIVE;
				break;
			default:
				break;
			}
			return type;
		}
	}
	
	int docsCount;
	List<BasicStatisticData> categories;
	
	public void setDocsCount(int docsCount) {
		this.docsCount = docsCount;
	}

	public int getDocsCount() {
		return docsCount;		
	}
}
