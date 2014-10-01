package hmm;

import java.util.ArrayList;
import java.util.List;

import basic.BasicStatisticData;

public class WordTagStatisticData extends BasicStatisticData{
	private List<String> tags;
	
	public WordTagStatisticData() {
		tags = new ArrayList<String>();
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
