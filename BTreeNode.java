
import java.io.PrintWriter;


/**
 * 
 *  [ 50 bytes for meta | tree object starts at 50 - | child pointer starts at 3088 ]
 *
 */

public class BTreeNode {

	private int[] childPointer;
	private int parentPointer;
	private int currentlyStored;
	private boolean isLeaf, isRoot;
	private  int childOffset = 3088;
	private int treeOffset = 50;
	private int m;
	private BTreeObject[] objectArray;
	private int sequenceLength;
	private int location;


	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	//default constructor only to be used when creating Btree with no elements
	public BTreeNode(int m, int sequenceLength, int location)  {
		this.location = location;
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
	public void setFreq(int i, int f) {
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
	public int getChildNode(int i) {
		return childPointer[i];
	}
	public void setChildPointer(int node, int i) {
		childPointer[i] = node;
	}
	public void traverse() {
		int i = 0;

		for( i = 0; i < this.getCurrentlyStored(); i++) {
			if(this.getIsLeaf() == false) {
//				childPointer[i].traverse();

			}
			System.out.println(objectArray[i] +" leaf = "+ this.getIsLeaf()+ "              curr stored in node = "+this.getCurrentlyStored()+ " count : " + objectArray[i].getFrequency());

		}

		System.out.println(" ");
//		if (this.getIsLeaf() == false) {
//			childPointer[i].traverse(); 

	}
	
	private String padZero(long key, int k) {

		int keyLength = String.valueOf(key).length();
		int difference = k - keyLength;
		

		if (difference == 0) {
			return Long.toString(key);
		}
		else if (difference == 1) {
			String merge = "0"+Long.toString(key);
			return merge;
		}
		else {
			String zero = "";
			for (int i=0; i<difference; i++) {
				zero = zero+"0";
			}
			String merge = zero+Long.toString(key);
			return merge;
		}
	}
	
	
	private static String keyToGene(String str) {
		String returnStr = "";
		for(int i = 0; i < str.length();i = i+2) {
			String subStr = str.substring(i, i+2);

			if(subStr.charAt(0) == '0' && subStr.charAt(1) == '0') {
				returnStr += "A";
			}
			if(subStr.charAt(0) == '1' && subStr.charAt(1) == '1') {
				returnStr += "T";
			}
			if(subStr.charAt(0) == '0' && subStr.charAt(1) == '1') {
				returnStr += "C";
			}
			if(subStr.charAt(0) == '1' && subStr.charAt(1) == '0') {
				returnStr += "G";
			}

		}
		return returnStr;
	}
	
	
	
	
//	public void dumpTraverse(PrintWriter pw, int k) {
//		int i =0;
//		for( i = 0; i < this.getCurrentlyStored(); i++) {
//			if(this.getIsLeaf() == false) {
//				childPointer[i].dumpTraverse(pw, k);
//			}
//			System.out.println(keyToGene((padZero(objectArray[i].getKey(), k))) +": " + objectArray[i].getFrequency() );
//			pw.println(keyToGene((padZero(objectArray[i].getKey(), k))) +": " + objectArray[i].getFrequency() );
//
//
//		}
//		if (this.getIsLeaf() == false) {
//			childPointer[i].dumpTraverse(pw, k); 
//		}
//	}

}