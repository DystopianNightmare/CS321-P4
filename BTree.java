import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

/**
 * Class that can store a BTree Node in to a BTree
 * Each node is stored, so node less than to root are sorted to the left
 * and nodes greater than the root node are sorted to the right
 *
 *Tree is stored in a Random Access File
 *[ 50 bytes for meta | tree object starts at 50 - | child pointer starts at 3088 ]
 * @author Joe, Josh, Kate
 *
 */
public class BTree {

	private BTreeNode root;
	private int m;					// maximum order 
	private int t;					// minimum degree
	private int middle; 			// is the middle key in a node 
	private String fileName;		
	private int sequenceLength;		// (k) the number of genes in a key
	private int currentOffset =0;	// the node number (multiplied by node size) to get position in the RAF
	private RandomAccessFile RAF;	
	private static final int FILE_META_OFFSET = 50;		// meta data is stored in the first 50 bites 
	private static final int NODE_SIZE = 4096;			// each node can hold 4096 bits of data in the RAF
	private int rootLocation;							// pointer to the location of root node in RAF

	
	/**
	 * Constructor for empty BTree
	 * To be used in reading in a RAF file
	 */
	public BTree() {

	}
	
	/**
	 * Constructor for BTree 
	 * To be used in creating a BTree
	 * @param name : name of file 
	 * @param t : minimum degree
	 * @param sequenceLength : the number of genes in a key
	 * @throws IOException
	 */
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

	/**
	 * getter for sequence length 
	 * @return
	 */
	public int getSequenceLength() {
		return sequenceLength;
	}

	/**
	 * increments the offset by 1
	 * used after creating any new node, to keep track on nodes in memory
	 * @return
	 */
	private int incrementOffset() {
		currentOffset = currentOffset+ 1;
		return currentOffset;

	}

	/**
	 * Creates BTree node with m, sequenceLength, and currentOffset
	 * writes this data to memory 
	 * @throws IOException
	 */
	public void BTreeCreateNode() throws IOException  {
		root = new BTreeNode(m,sequenceLength, currentOffset);
		writeFileMetaData();
		writeNodeToDisk(root, currentOffset);
	}
	
	/**
	 * writes node to memory file 
	 * meta data is in the order as reading data from memory  
	 * @param node
	 * @param nodeOffset
	 * @throws IOException
	 */
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
		writeFileMetaData();
	}
	
	/**
	 * pulls a node from memory and creates a new node
	 * meta data is in the order as writing data to memory
	 * @param pointer
	 * @return
	 * @throws IOException
	 */
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
	
	/**
	 * writes meta data to the first 50 bites of the file
	 * @throws IOException
	 */
	public void writeFileMetaData() throws IOException {
		RAF.seek(0);					// file meta data always starts at 0
		RAF.writeInt(currentOffset);	
		RAF.writeInt(m);
		RAF.writeInt(t);
		RAF.writeInt(sequenceLength);
		RAF.writeInt(rootLocation);		// location of root

		//		RAF.close();
	}

	/**
	 * reads in meta data
	 * in the same order as writing meta data to file
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public BTree readFileMetaData(RandomAccessFile file) throws IOException {
		file.seek(0);
		BTree tree = new BTree();
		tree.setCurrentOffset(file.readInt());
		tree.setM(file.readInt());
		tree.setT(file.readInt());
		tree.setSequenceLength(file.readInt());
		tree.setRootLocation(file.readInt());
		return tree;

	}
	
	/**
	 * Getter method for m (maximum order)
	 * @return
	 */
	public int getM() {
		return m;
	}

	/**
	 * Setter for m (maximum order)
	 * @param m
	 */
	public void setM(int m) {
		this.m = m;
	}

	/**
	 * Getter for t (minimum degree)
	 * @return
	 */
	public int getT() {
		return t;
	}

	/**
	 * Setter for t (minimum degree)
	 * @param t
	 */
	public void setT(int t) {
		this.t = t;
	}

	/**
	 * Getter for current offset, the node ID number 
	 * @return
	 */
	public int getCurrentOffset() {
		return currentOffset;
	}

	/**
	 * Setter for current offset, the node ID number
	 * @param currentOffset
	 */
	public void setCurrentOffset(int currentOffset) {
		this.currentOffset = currentOffset;
	}

	/**
	 * Getter for root location
	 * @return
	 */
	public int getRootLocation() {
		return rootLocation;
	}

	/**
	 * Setter for root location 
	 * @param rootLocation
	 */
	public void setRootLocation(int rootLocation) {
		this.rootLocation = rootLocation;
	}

	/**
	 * Setter for sequenceLength (the number of genes in a key)
	 * @param sequenceLength
	 */
	public void setSequenceLength(int sequenceLength) {
		this.sequenceLength = sequenceLength;
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

	/**
	 * Checks if a child node is full, is so the Insert method will slit current node 
	 * @param node
	 * @param val
	 * @return
	 * @throws IOException
	 */
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


	/**
	 * Inserts nodes into a BTree
	 * If a key value is greater than the root key value, insert to right 
	 * If a key value is less than the root key value, insert to left
	 * If a node is full it will split the node, keeping to the maximum order and minimum degree parameters
	 * @param val : the binary representation of a gene sequence
	 * @throws IOException
	 */
	public void BTreeInsert(long val) throws IOException  {												

		BTreeNode node = root;
		// check if a value is already inserted, if so it will increment frequency
		if (checkEqual(node, val) == true) {
			return;
		}
		
		// checks is a node needs to be split. If so the node is split and the new key is added 
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
		}
		//insert a key into a available node  
		else {
			insertNodeNonFull(node, val);	
		}
	}
	
	
	/**
	 * splits child node
	 * The keys at index middle -1 is placed in a parent node 
	 * The keys in indexes greater or equal to the middle index are placed in a new node 
	 * The new node's is stored in the parents childPointer array    
	 * @param parent
	 * @param val
	 * @throws IOException
	 */
	public void BTreeSplitChild(BTreeNode parent, long val) throws IOException {																

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


	/**
	 * If a node is not full, a new key will be added. This keeps with the ascending order
	 * If a key does not belong in this node, the method iterates to the correct child node
	 * @param node
	 * @param val
	 * @throws IOException
	 */
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

	/**
	 * When converting a binary key back to a gene sequence, 
	 * the correct amount of leading zeros are added to the string
	 * @param key
	 * @param k : number of genes in a sequence
	 * @return
	 */
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

	/**
	 * Converts a binary key to a gene string
	 * @param str
	 * @return
	 */
	public String keyToGene(String str) {
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
		return returnStr.toLowerCase();
	}

	/**
	 * Traverses the BTree and creates a dump file
	 * Dump file arranges the keys in increasing order, 
	 * Each key is printed followed by its frequency 
	 * @param pw : PrintWriter creates the dump file
	 * @param rootNode : node where traversal starts
	 * @param sequenceLength : gene sequence length 
	 * @throws IOException
	 */
	public void traverseDump(PrintWriter pw, BTreeNode rootNode, int sequenceLength) throws IOException {
		BTreeNode node = rootNode;
		int i =0;
		for (i = 0; i < node.getCurrentlyStored(); i ++) {

			if(node.getIsLeaf() == false) {
				int  childPinter = node.getChildPointer(i);
				BTreeNode childNode = readNodeFromDisk(childPinter);
				traverseDump(pw, childNode, sequenceLength);
			}
			pw.println(keyToGene((padZero(node.getKey(i), sequenceLength))).toLowerCase() +": " + node.getFreq(i) );	
		}
		if(node.getIsLeaf() == false) {
			int  childPinter = node.getChildPointer(i);
			BTreeNode childNode = readNodeFromDisk(childPinter);
			traverseDump(pw, childNode, sequenceLength);

		}
	}



	/**
	 * creates a dump file for the the BTree
	 * file is named "dump"
	 * @param k : gene sequence length
	 * @throws IOException
	 */
	public void dumpTree(int k) throws IOException {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter("dump");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		traverseDump(pw, root, k);
		pw.close();
	}
	

	/**
	 * this method will search a BTree for a node containing a key that is equivalent to the query value
	 * @param nodePointer
	 * @param queryKey
	 * @param sequenceLength
	 * @return
	 * @throws IOException
	 */
	public String searchTree(int nodePointer, Long queryKey, int sequenceLength) throws IOException {

		BTreeNode node = readNodeFromDisk(nodePointer);

		if (node.getIsLeaf() == true) {
			for (int i = 0; i < node.getCurrentlyStored(); i ++) {
				if (node.getKey(i) == queryKey) {
					return keyToGene((padZero(node.getKey(i), sequenceLength))) +": " + node.getFreq(i);
				}
			}
			return "none"; 			// if not found return frequency of 0
		}
		else {
			int i =0;
			for (i = 0; i < node.getCurrentlyStored(); i ++) {
				if (node.getKey(i) == queryKey) {
					return keyToGene((padZero(node.getKey(i), sequenceLength))) +": " + node.getFreq(i);
				}
				if (node.getKey(i) > queryKey) {
					int  childPinter = node.getChildPointer(i);
					return searchTree(childPinter, queryKey, sequenceLength);
				}	
			}
			int  childPinter = node.getChildPointer(i);
			return searchTree(childPinter, queryKey, sequenceLength);
			 
		}
	}
	
	/**
	 * reads in a random access file of a BTree
	 * this tree can be searched for a query key 
	 * @param RAFName
	 * @param queryKey
	 * @return
	 * @throws IOException
	 */
	public String readFileSearch(RandomAccessFile RAFName, Long queryKey) throws IOException {
		RAF = RAFName;
		RAF.seek(0);					// file meta data always starts at 0
		currentOffset = RAF.readInt();
		m = RAF.readInt();
		t = RAF.readInt();
		sequenceLength = RAF.readInt();
		int rootPointer = RAF.readInt();
		return searchTree(rootPointer, queryKey, sequenceLength*2);


	}
	
	
}
