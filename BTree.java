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
	private int currentOffset =0;
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

	private int incrementOffset() {
		currentOffset = currentOffset+ 1;
		return currentOffset;

	}

	public void BTreeCreateNode() throws IOException  {
		root = new BTreeNode(m,sequenceLength, currentOffset);
		writeFileMetaData();
		writeNodeToDisk(root, currentOffset);
	}
	public void writeNodeToDisk(BTreeNode node, int nodeOffset) throws IOException {
		int location = (nodeOffset*NODE_SIZE) +FILE_META_OFFSET;
		RAF.seek(location);
		RAF.writeInt(node.getOffset());
		RAF.writeInt(node.getCurrentlyStored());
		RAF.writeInt(node.getParentPointer());
		RAF.writeBoolean(node.getIsLeaf());

		// write child pointers
		if (!node.getIsLeaf()) {
			for(int i = 0; i < node.getM()+1; i++) {
				RAF.writeInt(node.getChildPointer(i));
			}
		}
		// else write a placeholder for child pointers
		else {
			for(int i = 0; i < node.getM()+1; i++) {
				RAF.writeInt(0);
			}
		}

		//writes each key to memory 
		for(int i = 0; i < node.getCurrentlyStored(); i++) {
			RAF.writeLong(node.getKey(i));
			RAF.writeInt(node.getFreq(i));
		}
		//		RAF.close();
		writeFileMetaData();
	}
	public BTreeNode readNodeFromDisk(int pointer) throws IOException {
		BTreeNode node = new BTreeNode(m,sequenceLength, currentOffset);
		int location = (pointer * NODE_SIZE) +FILE_META_OFFSET;
		RAF.seek(location);
		node.setOffset(RAF.readInt());
		node.setCurrentlyStored(RAF.readInt());
		node.setParentPointer(RAF.readInt());
		node.setIsLeaf(RAF.readBoolean());

		for(int j = 0; j < node.getM()+1; j++) {
			node.setChildPointer(RAF.readInt(), j);

		}
		for(int j = 0; j < node.getCurrentlyStored(); j++) {
			node.setKey( RAF.readLong(), j);
			node.setFreq(RAF.readInt(), j);
		}

		return node;
	}
	public void writeFileMetaData() throws IOException {
		RAF.seek(0);					// file meta data always starts at 0
		RAF.writeInt(currentOffset);	// location of root
		RAF.writeInt(m);
		RAF.writeInt(t);
		RAF.writeInt(sequenceLength);
		RAF.writeInt(rootLocation);
		//		RAF.close();
	}

	/**
	 * checks if a value is in the node, if so it will increment the frequency of that key
	 * @param node
	 * @param val
	 * @return
	 * @throws IOException 
	 */
	public boolean checkEqual(BTreeNode node, long val) throws IOException {
		boolean equalsFound = false;

		// check current node
		int i = 0;
		while(i < node.getCurrentlyStored()) {
			if (node.getBTreeObject(i).getKey() == val) {
				node.getBTreeObject(i).incrementFrequency();
				writeNodeToDisk(node,node.getOffset());
				equalsFound = true;
				break;
			}
			i++;
		}

		// check child nodes
		if (equalsFound == false && node.getIsLeaf() == false) {
			i = 0;
			while(i < node.getCurrentlyStored() && val > node.getBTreeObject(i).getKey()) {
				i++;
			}
			int  childPinter = node.getChildPointer(i);
			BTreeNode childNode = readNodeFromDisk(childPinter);
			equalsFound = checkEqual(childNode, val);
		}

		return equalsFound;
	}



	public boolean checkChildIsFull(BTreeNode node, long val) throws IOException {
		int i = 0;
		while(i < node.getCurrentlyStored() && val > node.getBTreeObject(i).getKey()) {
			i++;
		}
		if (node.getIsLeaf()== true) {
			return true;
		}
		int  childPinter = node.getChildPointer(i);
		BTreeNode childNode = readNodeFromDisk(childPinter);
		if (childNode.getCurrentlyStored() == m-1 && childNode.getIsLeaf() ) {
			return true;
		}
		else if (childNode.getCurrentlyStored() == m-1 && !childNode.getIsLeaf()) {
			boolean temp = checkChildIsFull(childNode, val);
			return temp;
		}
		return false;
	}


	public void BTreeInsert(long val) throws IOException  {																					// INSERT

		BTreeNode node = root;
		if (checkEqual(node, val) == true) {
			return;
		}
		if(node.getCurrentlyStored() == m-1 && checkChildIsFull(node, val)) {
			BTreeNode newNode = new BTreeNode(m, sequenceLength, incrementOffset());
			root = newNode;
			rootLocation = root.getOffset();
			newNode.setIsLeaf(false);
			newNode.setCurrentlyStored(0);
			newNode.setIsRoot(true);
			newNode.setChildPointer(node.getOffset(), 0);

			insertNodeNonFull(node,val);
			BTreeSplitChild(newNode,val);
			writeFileMetaData();
		}else {
			insertNodeNonFull(node, val);	
		}
	}
	public void BTreeSplitChild(BTreeNode parent, long val) throws IOException {																		// SPLIT CHILD

		//set Z equal to a new null node, Y is equal to the appropriate child node
		BTreeNode z = new BTreeNode(m, sequenceLength,incrementOffset());		//z = new right child

		int i = parent.getCurrentlyStored();
		i = 0;
		while(i < parent.getCurrentlyStored() && val > parent.getKey(i)) {
			i++;
		}
		int yPointer = parent.getChildPointer(i);
		BTreeNode y = readNodeFromDisk(yPointer);
		z.setIsLeaf(y.getIsLeaf());

		// set parent pointers
		y.setParentPointer(parent.getOffset());
		z.setParentPointer(parent.getOffset());

		// promote middle-1 key to parent 
		i = parent.getCurrentlyStored();
		while(i >= 1 && val < parent.getKey(i-1)) {
			parent.setBTreeObject(parent.getBTreeObject(i-1), i);
			i--;
		}

		parent.setBTreeObject(y.getBTreeObject(middle-1), i);
		parent.incrementCurrentlyStored();

		// take top half objects from array y and put in array z
		int tmp = y.getCurrentlyStored()/2;
		for(int j =0; j<tmp; j++) {
			z.setBTreeObject(y.getBTreeObject(j+tmp), j); //z.key = y.key+t
		}
		y.setCurrentlyStored(middle-1);
		z.setCurrentlyStored(middle);	

		//set child pointer to new child
		i = parent.getCurrentlyStored();
		while(i >= 1 && z.getBTreeObject(0).getKey() < parent.getKey(i-1)) {
			parent.setChildPointer(parent.getChildPointer(i-1), i);
			i--;
		}
		parent.setChildPointer(z.getOffset(), i);


		// if y is not a leaf we need to assign the correct child pointers for child
		if(!y.getIsLeaf()) {	//if y is not a leaf
			for(int j = 0; j < middle+1;j++) {
				z.setChildPointer(y.getChildPointer(j+middle), j); 		//sets z's child pointers to that of y+t
			}
		}

		writeNodeToDisk(parent,parent.getOffset());
		writeNodeToDisk(y,y.getOffset());
		writeNodeToDisk(z,z.getOffset());

	}
	

	public void insertNodeNonFull(BTreeNode node, long val) throws IOException {	
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
			writeNodeToDisk(node,node.getOffset());
		}

		// if node is not a leaf, then we go down to the correct child
		else{
			i = 0;
			while(i < node.getCurrentlyStored() && val > node.getBTreeObject(i).getKey()) {
				i++;
			}

			int  childPinter = node.getChildPointer(i);
			BTreeNode childNode = readNodeFromDisk(childPinter);
			// if child is full and all of its children are full
			if(childNode.getCurrentlyStored() ==  m-1 && checkChildIsFull(childNode, val)) {
				insertNodeNonFull(childNode,val);
				BTreeSplitChild(node,val);
			}
			else {
				insertNodeNonFull(childNode, val);
			}

		}
	}

	
	
	
	
	
	
	
	 
//	public void printTree() throws IOException {
//		System.out.println(" ");	//blank space
//		traverse(root);
//
//	}
//	public void traverse(BTreeNode node) throws IOException {
//		int i = 0;
//	
////		BTreeNode node1 = new BTreeNode(0,0,0);
//		for( i = 0; i < node.getCurrentlyStored(); i++) {
//			if(node.getIsLeaf() == false) {
//				System.out.println();
//				node=readNodeFromDisk(node.getChildPointer(i));
//				traverse(node);
//			}
//			System.out.println(node.getKey(i) + " leaf = "+ node.getIsLeaf()+ "              curr stored in node = "+node.getCurrentlyStored());
//
//		}
//
////		System.out.println(" ");
////		if (this.getIsLeaf() == false) {
////			childPointer(i).traverse(); 
//		System.out.println(" ");
//		if (node.getIsLeaf() == false) {
//			node=readNodeFromDisk(node.getChildPointer(i));
//			traverse(node);
//		}
//	}



	
	public String padZero(long key, int k) {

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
	
	public static String keyToGene(String str) {
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

	public void traverseDump(PrintWriter pw, BTreeNode rootNode, int sequenceLength) throws IOException {
		BTreeNode node = rootNode;
		int i =0;
		for (i = 0; i < node.getCurrentlyStored(); i ++) {

			if(node.getIsLeaf() == false) {
				int  childPinter = node.getChildPointer(i);
				BTreeNode childNode = readNodeFromDisk(childPinter);
				traverseDump(pw, childNode, sequenceLength);
			}

		
			System.out.println(keyToGene((padZero(node.getKey(i), sequenceLength))) +": " + node.getFreq(i));
			pw.println(keyToGene((padZero(node.getKey(i), sequenceLength))) +": " + node.getFreq(i) );	
		}
		if(node.getIsLeaf() == false) {
			int  childPinter = node.getChildPointer(i);
			BTreeNode childNode = readNodeFromDisk(childPinter);
			traverseDump(pw, childNode, sequenceLength);

		}
	}
	
	
	
	public void dumpTree(int k) throws IOException {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(fileName+".txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		traverseDump(pw, root, k);
		pw.close();
	}

	
	
}





