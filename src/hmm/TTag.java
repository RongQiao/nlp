package hmm;

public class TTag {
	public static final String CD = "CD";
	private String tagName;

	public TTag(String tag) {
		setTagName(tag);
	}

	public double probIsNum() {
		double ret = 0.0;
		if (getTagName().equalsIgnoreCase(CD)) {
			ret = 1.0;
		}
		return ret;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

}
