import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class BTree {

	private BTreeNode root;
	private int m;
	private File file;
	private int t;
	private int middle; 			// is the middle node
	private String fileName;
	private BTreeNode root2;
	private int sequenceLength;
	private int nodeNumber =0;
	private RandomAccessFile RAF;
	private static final int FILE_META_OFFSET = 50;
	private static final int NODE_SIZE = 4096;
	private int rootLocation;

	public BTree(String name, int t, int sequenceLength) throws IOException  {
		//make a new file to write to. M is number of values per node, call btreecreate to get a tree with 1 node
		this.t = t;
		m= t*2;
		middle = (int)Math.floor(t);	//find middle node
		fileName = name;
		
		this.sequenceLength = sequenceLength;
		RAF = new RandomAccessFile(fileName, "rw");
		BTreeCreateNode();
	}

	public int getSequenceLength() {
		return sequenceLength;
	}

	public void BTreeCreateNode() throws IOException  {
		root = new BTreeNode(m,sequenceLength, nodeNumber);
		writeFileMetaData();
		writeNodeToDisk(root, nodeNumber);
	}
	public void writeNodeToDisk(BTreeNode node, int nodeNum) throws IOException {
		int location = (nodeNumber * NODE_SIZE) +FILE_META_OFFSET;
		RAF.seek(location);
		RAF.writeInt(node.getLocation());
		RAF.writeInt(node.getCurrentlyStored());
		RAF.writeInt(node.getParentPointer());
		RAF.writeBoolean(node.getIsLeaf());
		
		for(int i = 0; i < node.getM(); i++) {
		RAF.writeInt(node.getChildNode(i));
		}
		for(int i = 0; i < node.getCurrentlyStored()-1; i++) {
			RAF.writeLong(node.getKey(i));
			RAF.writeInt(node.getFreq(location));
			}
//		RAF.close();
		writeFileMetaData();
	}
	public BTreeNode readNodeFromDisk(int i) throws IOException {
		BTreeNode node = new BTreeNode(m,sequenceLength, nodeNumber);
		int location = (i * NODE_SIZE) +FILE_META_OFFSET;
		RAF.seek(location);
		node.setLocation(RAF.readInt());
		node.setCurrentlyStored(RAF.readInt());
		node.setParentPointer(RAF.readInt());
		node.setIsLeaf(RAF.readBoolean());
		for(int j = 0; j < node.getM(); j++) {
			node.setChildPointer(RAF.readInt(), j);
			
			}
		for(int j = 0; j < node.getM()-1; j++) {
			node.setKey( RAF.readLong(), j);
			node.setFreq(RAF.readInt(), j);
			}
		
		return node;
	}
	public void writeFileMetaData() throws IOException {
		RAF.seek(0);
		RAF.writeInt(nodeNumber);
		RAF.writeInt(m);
		RAF.writeInt(t);
		RAF.writeInt(sequenceLength);
		RAF.writeInt(rootLocation);
//		RAF.close();
	}

//	/**
//	 * checks if a value is in the node, if so it will increment the frequency of that key
//	 * @param node
//	 * @param val
//	 * @return
//	 */
//	public boolean checkEqual(BTreeNode node, long val) {
//		boolean equalsFound = false;
//
//		// check current node
//		int i = 0;
//		while(i < node.getCurrentlyStored()) {
//			if (node.getBTreeObject(i).getKey() == val) {
//				node.getBTreeObject(i).incrementFrequency();
//				equalsFound = true;
//				break;
//			}
//			i++;
//		}
//
//		// check child nodes
//		if (equalsFound == false && node.getIsLeaf() == false) {
//			i = 0;
//			while(i < node.getCurrentlyStored() && val > node.getBTreeObject(i).getKey()) {
//				i++;
//			}
//			BTreeNode childNode = node.getChildNode(i);	
//			equalsFound = checkEqual(childNode, val);
//		}
//
//		return equalsFound;
//	}
//
//
//
//	public boolean checkChildIsFull(BTreeNode node, long val) {
//		int i = 0;
//		while(i < node.getCurrentlyStored() && val > node.getBTreeObject(i).getKey()) {
//			i++;
//		}
//		if (node.getIsLeaf()== true) {
//			return true;
//		}
//		BTreeNode childNode = node.getChildNode(i);
//		if (childNode.getCurrentlyStored() == m-1 && childNode.getIsLeaf() ) {
//			return true;
//		}
//		else if (childNode.getCurrentlyStored() == m-1 && !childNode.getIsLeaf()) {
//			boolean temp = checkChildIsFull(childNode, val);
//			return temp;
//		}
//		return false;
//	}
//
//
	public void BTreeInsert(long val)  {																					// INSERT

		
//		if (checkEqual(node, val) == true) {
//			return;
//		}
//		if(node.getCurrentlyStored() == m-1 && checkChildIsFull(node, val)) {
//			BTreeNode s = new BTreeNode(m, sequenceLength);
//			root = s;
//			s.setIsLeaf(false);
//			s.setCurrentlyStored(0);
//			s.setChildPointer(node, 0);
//			s.setIsRoot(true);
//
//			insertNodeNonFull(node,val);
//			BTreeSplitChild(s,val);
//		}else {
			insertNodeNonFull(root, val);	
//		}
	}
//	public void BTreeSplitChild(BTreeNode parent, long zz) {																		// SPLIT CHILD
//
//		//set Z equal to a new null node, Y is equal to the appropriate child node
//		BTreeNode z = new BTreeNode(m, sequenceLength);		//z = new right child
//		node=parent;
//		int i = parent.getCurrentlyStored();
//		i = 0;
//		while(i < parent.getCurrentlyStored() && zz > parent.getBTreeObject(i).getKey()) {
//			i++;
//		}
//		BTreeNode y = parent.getChildNode(i);
//		z.setIsLeaf(y.getIsLeaf());
//
//		// set parent pointers
//		y.setParentPointer(parent);
//		z.setParentPointer(parent);
//
//		// promote middle-1 key to parent 
//		i = parent.getCurrentlyStored();
//		while(i >= 1 && zz < parent.getBTreeObject(i-1).getKey()) {
//			parent.setBTreeObject(parent.getBTreeObject(i-1), i);
//			i--;
//		}
//
//		parent.setBTreeObject(y.getBTreeObject(middle-1), i);
//		parent.incrementCurrentlyStored();
//
//		// take top half objects from array y and put in array z
//		int tmp = y.getCurrentlyStored()/2;
//		for(int j =0; j<tmp; j++) {
//			z.setBTreeObject(y.getBTreeObject(j+tmp), j); //z.key = y.key+t
//		}
//
//
//		//set child pointer to new child
//		i = parent.getCurrentlyStored();
//		while(i >= 1 && z.getBTreeObject(0).getKey() < parent.getBTreeObject(i-1).getKey()) {
//			parent.setChildPointer(parent.getChildNode(i-1), i);
//			i--;
//		}
//		parent.setChildPointer(z, i);
//
//
//
//		// if y is not a leaf we need to assign the correct child pointers for child
//		if(!y.getIsLeaf()) {	//if y is not a leaf
//			for(int j = 0; j < middle+1;j++) {
//				z.setChildPointer(y.getChildNode(j+middle), j); 		//sets z's child pointers to that of y+t
//			}
//		}
//
//		y.setCurrentlyStored(middle-1);
//		z.setCurrentlyStored(middle);	
//	}
//
	public void insertNodeNonFull(BTreeNode node, long val) {	
		int i = node.getCurrentlyStored();

		// if node is a leaf insert key to correct index
		if(node.getIsLeaf()) {

			while(i >= 1 && val < node.getBTreeObject(i-1).getKey()) {
				node.setBTreeObject(node.getBTreeObject(i-1), i);
				i--;
			}

			BTreeObject obj = new BTreeObject(val);
			node.setBTreeObject(obj, i);
			node.incrementCurrentlyStored();	
		}

//		// if node is not a leaf, then we go down to the correct child
//		else{
//			i = 0;
//			while(i < node.getCurrentlyStored() && val > node.getBTreeObject(i).getKey()) {
//				i++;
//			}
//
//			BTreeNode childNode = node.getChildNode(i);	
//			// if child is full and all of its children are full
//			if(childNode.getCurrentlyStored() ==  m-1 && checkChildIsFull(childNode, val)) {
//				insertNodeNonFull(childNode,val);
//				BTreeSplitChild(node,val);
//			}
//			else {
//				insertNodeNonFull(childNode, val);
//			}
//
//		}
	}
//
//	public void printTree() {
//		System.out.println(" ");	//blank space
//		root.traverse();
//		print();
//	}
//	public void print () {
//		// Print a textual representation of this B-tree.
//		printSubtree(root, "");
//	}

//	private static void printSubtree (BTreeNode top, String indent) {
//		// Print a textual representation of the subtree of this B-tree whose
//		// topmost node is top, indented by the string of spaces indent.
//		if (top == null)
//			System.out.println(indent + "o");
//		else {
//			System.out.println(indent + "o-->");
//			boolean isLeaf = top.getIsLeaf();
//			String childIndent = indent + "    ";
//			for (int i = 0; i < top.getCurrentlyStored(); i++) {
//				if (! isLeaf)  printSubtree(top.getChildNode(i), childIndent);
//				System.out.println(childIndent + top.getBTreeObject(i).getKey());
//			}
//			if (! isLeaf)  printSubtree(top.getChildNode(top.getCurrentlyStored()), childIndent);
//		}
//	}

	public void dumpTree(int k) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter("dump");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		root.dumpTraverse(pw,k);
		pw.close();
	}
	
	

	
	
	
	
	
	
	
	
	
}



