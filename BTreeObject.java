import java.io.Serializable;

public class BTreeObject implements Serializable {

	private long key;
	private int frequency;
	
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
}
