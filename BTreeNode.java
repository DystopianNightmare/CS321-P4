import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;

public class BTreeNode {

	private BTreeNode[] childPointer;
	private BTreeNode parentPointer;
	private int currentlyStored;
	private boolean isLeaf, isRoot;

	private int m;
	private BTreeObject[] objectArray;
	private int sequenceLength;


	//default constructor only to be used when creating Btree with no elements
	public BTreeNode(int m, int sequenceLength)  {
		isLeaf = true;
		this.m = m;
		objectArray = new BTreeObject[m];
		childPointer = new BTreeNode[m+1];
		currentlyStored = 0;
		parentPointer = null;
		this.sequenceLength = sequenceLength;

	}
	public BTreeObject getBTreeObject(int i) {
		return objectArray[i];
	}

	public void  setBTreeObject(BTreeObject obj, int i) {
		objectArray[i] = obj;
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
	public BTreeNode getParentPointer() {
		return parentPointer;
	}
	public void setParentPointer(BTreeNode parentPointer) {
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
	public BTreeNode getChildNode(int i) {
		return childPointer[i];
	}
	public void setChildPointer(BTreeNode node, int i) {
		childPointer[i] = node;
	}
	public void traverse() {
		int i = 0;

		for( i = 0; i < this.getCurrentlyStored(); i++) {
			if(this.getIsLeaf() == false) {
				childPointer[i].traverse();

			}
			System.out.println(objectArray[i] +" leaf = "+ this.getIsLeaf()+ "              curr stored in node = "+this.getCurrentlyStored()+ " count : " + objectArray[i].getFrequency());

		}

		System.out.println(" ");
		if (this.getIsLeaf() == false) 
			childPointer[i].traverse(); 

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
	
	
	
	
	public void dumpTraverse(PrintWriter pw, int k) {
		int i =0;
		for( i = 0; i < this.getCurrentlyStored(); i++) {
			if(this.getIsLeaf() == false) {
				childPointer[i].dumpTraverse(pw, k);
			}
			System.out.println(keyToGene((padZero(objectArray[i].getKey(), k))) +": " + objectArray[i].getFrequency() );
			pw.println(keyToGene((padZero(objectArray[i].getKey(), k))) +": " + objectArray[i].getFrequency() );


		}
		if (this.getIsLeaf() == false) {
			childPointer[i].dumpTraverse(pw, k); 
		}
	}
	public String convertToString(long val) {
	
		
		return null;
	}
}
