
/**
 * Node that stores BTree Key objects 
 * Keys are stored in increasing order
 * @author Joe, Josh, Kate
 *
 */
public class BTreeNode {

	private int[] childPointer;			// array of pointers to child nodes
	private int parentPointer;			// pointer to where parent is stored in memory   
	private int currentlyStored; 		// number of keys in object array
	private boolean isLeaf, isRoot;
	private int m;						// maximum order; amount of keys in a node
	private BTreeObject[] objectArray;
	private int sequenceLength;
	private int offset;					// the nodes position in memory, also its node ID

	
	/**
	 * Constructor for an empty BTree Node 
	 * only to be used when creating Btree with no elements
	 */
	public BTreeNode() {
		
	}


	/**
	 * Default constructor for a BTree Object 
	 * @param m : maximum order; amount of keys in a node
	 * @param sequenceLength : length of gene sequence 
	 * @param offset : node position in memory
	 */
	public BTreeNode(int m, int sequenceLength, int offset)  {
		this.offset = offset;
		isLeaf = true;
		this.m = m;
		objectArray = new BTreeObject[m];
		childPointer = new int[m+1];
		currentlyStored = 0;
		parentPointer = 0;
		this.sequenceLength = sequenceLength;

	}
	
	/**
	 * Getter for offset 
	 * @return
	 */
	public int getOffset() {
		return offset;
	}
	
	/**
	 * setter for offset
	 * @param location
	 */
	public void setOffset(int location) {
		this.offset = location;
	}
	
	/**
	 * Getter for m
	 * @return
	 */
	public int getM() {
		return m;
	}
	
	/**
	 * 
	 * @param i
	 * @return the object stored in the i'th position in the object array
	 */
	public BTreeObject getBTreeObject(int i) {
		return objectArray[i];
	}

	/**
	 * Sets a BTree object in the object array at position i 
	 * @param obj
	 * @param i
	 */
	public void  setBTreeObject(BTreeObject obj, int i) {
		objectArray[i] = obj;
	}
	
	/**
	 * @param i
	 * @return long key value for an object in at the i'th position in the object array 
	 */
	public long getKey(int i) {
		return objectArray[i].getKey();
	}
	
	/**
	 * creates a BTree Object with passed value 
	 * stores object in object array at i'th position 
	 * @param val
	 * @param i
	 */
	public void setKey(long val, int i) {
		BTreeObject obj = new BTreeObject(val);
		objectArray[i] = obj;
	}
	
	/**
	 * sets frequency of object at position i
	 * @param f : frequency to be set
	 * @param i
	 */
	public void setFreq(int f, int i) {
		objectArray[i].setFrequency(f);
	}
	
	/**
	 * returns frequency of a BTree object at i'th position
	 * @param i
	 * @return
	 */
	public int getFreq(int i) {
		return objectArray[i].getFrequency();
	}

	/**
	 * Getter for isLeaf 
	 * @return
	 */
	public boolean getIsLeaf() {
		return isLeaf;
	}
	
	/**
	 * Setter for Is Root boolean 
	 * @param b
	 */
	public void setIsRoot(boolean b) {
		isRoot = b;
	}
	
	/**
	 * Setter for is Leaf boolean 
	 * @param b
	 */
	public void setIsLeaf(boolean b) {
		isLeaf = b;
	}
	
	/**
	 * Getter for is root boolean 
	 * @return
	 */
	public boolean getIsRoot() {
		return isRoot;
	}
	
	/**
	 * Getter for Parent pointer
	 * @return
	 */
	public int getParentPointer() {
		return parentPointer;
	}
	
	/**
	 * Setter for Parent Pointer
	 * @param parentPointer
	 */
	public void setParentPointer(int parentPointer) {
		this.parentPointer = parentPointer;
	}
	
	/**
	 * Setter for currently stored 
	 * @param i
	 */
	public void setCurrentlyStored(int i) {
		currentlyStored = i;
	}

	/**
	 * Getter for Currently Stored  
	 * @return
	 */
	public int getCurrentlyStored() {
		return currentlyStored;
	}
	
	/**
	 * increased currently stored variable by 1
	 */
	public void incrementCurrentlyStored() {
		currentlyStored++;
	}
	
	/**
	 * returns a child pointer from the child pointer array
	 * @param i : returns pointer at position i 
	 * @return
	 */
	public int getChildPointer(int i) {
		return childPointer[i];
	}
	
	/**
	 * sets child pointer at position i with passed node
	 * @param node
	 * @param i
	 */
	public void setChildPointer(int node, int i) {
		childPointer[i] = node;
	}
	
}
