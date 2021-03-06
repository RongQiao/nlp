package NgramLM;

import basic.BasicStatisticData;

public class StatisticData extends BasicStatisticData{

	private int followCount;	//how many words followed this word
	private double sumPairProb;	//the bigram prob of pair begin with this word
	private double SumPairCount;
	private double backoffPml;	//the unigram prob of all si for pair "s1 si"
	private double alpha;
	private double probGoodTurning;	
	private double backOffProb;
	private boolean signSmooth;
	private boolean signGoodturning;
	private double logProbability;
	private double logAlpha;
	
	public StatisticData() {
		super();
		followCount = 0;
		sumPairProb = 0;
		SumPairCount = 0;
		backoffPml = 0;
		alpha = 0;
		signSmooth = false;
		signGoodturning = false;
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
	
	public double getProbGoodTurning() {
		return this.probGoodTurning;
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

	public void setSmoothSign() {
		this.signSmooth = true;
	}

	public boolean isNeedSmooth() {
		return this.signSmooth;
	}

	public void setGoodturningSign() {
		this.signGoodturning = true;
	}
	
	public boolean isGoodTurning() {
		return this.signGoodturning;
	}

	public double getLogProbability() {
		return logProbability;
	}

	public void setLogProbability(double logProbability) {
		this.logProbability = logProbability;
	}

	public double getLogAlpha() {
		return logAlpha;
	}

	public void setLogAlpha(double logAlpha) {
		this.logAlpha = logAlpha;
	}
}
