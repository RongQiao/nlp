package basic;

public interface DataMapInterface {
	public boolean containsKey(String key);
	public int getCount(String key);
	public void setCount(String key, int cnt);
	public double getProbability(String key);
}
