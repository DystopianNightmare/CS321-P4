/**
 * 
 * @author Joe, Josh, Kate
 * This class stores the gene sequence represented as a binary value 
 * this also keeps tack of the frequency this key was added
 */
public class BTreeObject  {

	private long key; 				// binary long value to represent a gene sequence 
	private int frequency;			// number of times a gene is stored in an B Tree
	
	/**
	 * Constructor for A BTree Object 
	 * @param key - binary value representing a gene sequence 
	 */
	public BTreeObject(long key) {
		this.key = key;
		frequency = 1;
	}

	/**
	 * increases the frequency count by 1
	 */
	public void incrementFrequency() {
		frequency++;
	}
	
	/**
	 * Getter for frequency
	 * @return
	 */
	public int getFrequency() {
		return frequency;
	}
	
	/**
	 * getter for key 
	 * @return
	 */
	public long getKey() {
		return key;
	}
	
	/**
	 * To String for key object 
	 */
	public String toString() {
		return String.valueOf(key);
	}

	/**
	 * Setter for frequency 
	 * @param f : frequency to be set to 
	 */
	public void setFrequency(int f) {
		frequency = f;
		
	}
}
