package NgramLM;

public class StatisticData {

	private int count;
	private int followCount;	//how many words followed this word
	private double probability;
	private double sumPairProb;	//the bigram prob of pair begin with this word
	private double SumPairCount;
	private double backoffPml;	//the unigram prob of all si for pair "s1 si"
	private double alpha;
	private double probGoodTurning;	
	private double backOffProb;
	
	public StatisticData() {
		count = 0;
		followCount = 0;
		probability = 0;
		sumPairProb = 0;
		backoffPml = 0;
		alpha = 0;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public double getAlpha() {
		return alpha;
	}
	
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public int getFollowCount() {
		return followCount;
	}

	public void setFollowCount(int count) {
		this.followCount = count;
	}

	public double getSumPairProb() {
		return sumPairProb;
	}

	public void setSumPairProb(double prob) {
		this.sumPairProb = prob;
	}

	public double getBackoffPml() {
		return backoffPml;
	}

	public void setBackoffPml(double backoffPml) {
		this.backoffPml = backoffPml;
	}

	public void setProbGoodTurning(double prob) {
		this.probGoodTurning = prob;
	}

	public double getBackOffProb() {
		return backOffProb;
	}

	public void setBackOffProb(double pbo) {
		this.backOffProb = pbo;
	}

	public double getSumPairCount() {
		return SumPairCount;
	}

	public void setSumPairCount(double sumPairCount) {
		SumPairCount = sumPairCount;
	}

}
