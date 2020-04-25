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
	
	
	//default constructor only to be used when creating Btree with no elements
	public BTreeNode(int m)  {
	isLeaf = true;
	this.m = m;
	objectArray = new BTreeObject[m];
	childPointer = new BTreeNode[m];
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
			System.out.println(objectArray[i] +" "+ this.getIsLeaf());
			System.out.println(this.currentlyStored);
		}
		
		System.out.println(" ");
		if (this.getIsLeaf() == false) 
            childPointer[i].traverse(); 
	}
}
