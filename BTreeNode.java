import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public class BTreeNode {

	private BTreeNode[] childPointer;
	private BTreeNode parentPointer;
	private int currentlyStored;
	private boolean isLeaf, isRoot;
	
	private int m;
	private BTreeObject[] objectArray;
	private BTreeNode[] nodes;
	
	//default constructor only to be used when creating Btree with no elements
	public BTreeNode(int m)  {
	isLeaf = true;
	this.m = m;
	objectArray = new BTreeObject[(m*2)-1];
	childPointer = new BTreeNode[m*2];
	currentlyStored = 0;
	parentPointer = null;
		
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
		return nodes[i];
	}
}
