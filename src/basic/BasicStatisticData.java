package basic;

public class BasicStatisticData {
	private int count;
	private double probability;
	
	public BasicStatisticData() {
		setCount(0);
		setProbability(0.0);
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

}
