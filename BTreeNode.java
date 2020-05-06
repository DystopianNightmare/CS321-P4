import java.io.PrintWriter;


/**
 * 
 *  [ 50 bytes for meta | tree object starts at 50 - | child pointer starts at 3088 ]
 *
 */

public class BTreeNode {

	private int[] childPointer;			// array of pointers to child nodes
	private int parentPointer;			// pointer to where parent is stored in memory   
	private int currentlyStored; 		// number of keys in object array
	private boolean isLeaf, isRoot;
	private  int childOffset = 3088;
	private int treeOffset = 50;
	private int m;
	private BTreeObject[] objectArray;
	private int sequenceLength;
	private int offset;

	public BTreeNode() {
		
	}

	public int getOffset() {
		return offset;
	}
	public void setOffset(int location) {
		this.offset = location;
	}
	//default constructor only to be used when creating Btree with no elements
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
	public int getM() {
		return m;
	}
	public BTreeObject getBTreeObject(int i) {
		return objectArray[i];
	}

	public void  setBTreeObject(BTreeObject obj, int i) {
		objectArray[i] = obj;
	}
	public long getKey(int i) {
		return objectArray[i].getKey();
	}
	public void setKey(long val, int i) {
		BTreeObject obj = new BTreeObject(val);
		objectArray[i] = obj;
	}
	public void setFreq(int f, int i) {
		objectArray[i].setFrequency(f);
	}
	public int getFreq(int i) {
		return objectArray[i].getFrequency();
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsRoot(boolean b) {
		isRoot = b;
	}
	public void setIsLeaf(boolean b) {
		isLeaf = b;
	}
	public boolean getIsRoot() {
		return isRoot;
	}
	public int getParentPointer() {
		return parentPointer;
	}
	public void setParentPointer(int parentPointer) {
		this.parentPointer = parentPointer;
	}
	public void setCurrentlyStored(int i) {
		currentlyStored = i;
	}

	public int getCurrentlyStored() {
		return currentlyStored;
	}
	public void incrementCurrentlyStored() {
		currentlyStored++;
	}
	public boolean isFull() {
		return currentlyStored == m*2-1;
	}
	public int getChildPointer(int i) {
		return childPointer[i];
	}
	public void setChildPointer(int node, int i) {
		childPointer[i] = node;
	}
	
}






