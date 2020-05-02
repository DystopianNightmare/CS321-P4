import java.io.Serializable;

public class BTreeObject implements Serializable {

	private long key;
	private int frequency;
	private int j;
	
	public BTreeObject(long key) {
		this.key = key;
		frequency = 1;
	}

	public void incrementFrequency() {
		frequency++;
	}
	public int getFrequency() {
		return frequency;
	}
	public long getKey() {
		return key;
	}
	

	public String toString() {
		return key + ": " + frequency;
	}
}
