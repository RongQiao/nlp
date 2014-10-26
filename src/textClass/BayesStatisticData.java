package textClass;

import java.util.List;

import basic.BasicStatisticData;
import textClass.TextClassResult.TextClassifier;

public class BayesStatisticData extends BasicStatisticData{
	int countInDocs[];	//number of occurrences of a word in docs(class j) 
	double probInDocs[];	//probability of occurrences of a word in docs(class j)
	
	public BayesStatisticData() {
		int cnt = TextClassifier.getCount();
		countInDocs = new int[cnt];
		probInDocs = new double[cnt];
	}

	public void setCount(TextClassifier type, int cnt) {
		int index = TextClassifier.getIndex(type);
		countInDocs[index] = cnt;
	}

	public int getCount(TextClassifier type) {
		int index = TextClassifier.getIndex(type);
		return countInDocs[index];
	}

	public void setProbability(TextClassifier type, double prob) {
		int index = TextClassifier.getIndex(type);
		probInDocs[index] = prob;
	}
	
	public double getProbability(TextClassifier type) {
		int index = TextClassifier.getIndex(type);
		return probInDocs[index];
	}
}
