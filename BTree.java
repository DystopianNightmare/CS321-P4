import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BTree {

	private BTreeNode root, curr, parent, child, node;
	private int m;
	private File file;
	private int t;
	private int middle; 			// is the middle node

	public BTree(String name, int t)  {
		//make a new file to write to. M is number of values per node, call btreecreate to get a tree with 1 node
		this.t = t;
		m= t*2;
		middle = (int)Math.floor(t);	//find middle node

		root = BTreeCreateNode();
	}

	public BTreeNode BTreeCreateNode()  {
		node = new BTreeNode(m);

		return node;
	}
	
	public boolean checkChildIsFull(BTreeNode node, long val) {
		int i = 0;
		while(i < node.getCurrentlyStored() && val > node.getBTreeObject(i).getKey()) {
			i++;
		}
		if (node.getIsLeaf()== true) {
			return true;
		}
		BTreeNode childNode = node.getChildNode(i);
		if (childNode.getCurrentlyStored() == m-1 && childNode.getIsLeaf()) {
			return true;
		}
		else if (childNode.getCurrentlyStored() == m-1 && !childNode.getIsLeaf()) {
			checkChildIsFull(node, val);
			}
		return false;
	}

	
	public void BTreeInsert(long val)  {																					// INSERT

		node = root;
		if(node.getCurrentlyStored() == m-1 && checkChildIsFull(node, val)) {
			BTreeNode s = new BTreeNode(m);
			root = s;
			s.setIsLeaf(false);
			s.setCurrentlyStored(0);
			s.setChildPointer(node, 0);

			s.setIsRoot(true);


			insertNodeNonFull(node,val);
			BTreeSplitChild(s,val);

			//			insertNodeNonFull(s,val);
		}else {
			insertNodeNonFull(root, val);	
		}
	}
	public void BTreeSplitChild(BTreeNode parent, long zz) {																		// SPLIT CHILD

		//*** changed for loops to start at zero since arrays being used start at 0

		BTreeNode z = new BTreeNode(m);		//z = new right child
		BTreeNode y = parent.getChildNode(parent.getCurrentlyStored());	//y = old root
		z.setIsLeaf(true);

		y.setParentPointer(parent);
		z.setParentPointer(parent);

		parent.setBTreeObject(y.getBTreeObject(middle), parent.getCurrentlyStored());
		parent.incrementCurrentlyStored();
		
		parent.setChildPointer(z, parent.getCurrentlyStored());
//		z.setIsLeaf(y.getIsLeaf());

		//in psuedocode we set z's n to t-l, but i ddont know if we need to if we call that value from Tree and set globally
		// take top half objects from array y and put in array z
		for(int j =1; j<t; j++) {
			z.setBTreeObject(y.getBTreeObject(j+middle), j-1); //z.key = y.key+t
		}

		if(!y.getIsLeaf()) {	//if y is not a leaf
			for(int j = 0; j < t;j++) {
				z.setChildPointer(y.getChildNode(j+middle), j); 		//sets z's child pointers to that of y+t
			}
		}

		y.setCurrentlyStored(middle);
		z.setCurrentlyStored(middle-1);			// -1 ????

//		for(int j = parent.getCurrentlyStored(); j > middle; j--) {		//what is i?
//			parent.setChildPointer(parent.getChildNode(j), j-1);
//		}

		parent.setChildPointer(z, parent.getCurrentlyStored());
//
//		for(int j = parent.getCurrentlyStored(); j > middle; j--) {
//			z.setBTreeObject(parent.getBTreeObject(j), j+1);
//		}
//		parent.setBTreeObject(y.getBTreeObject(t) ,middle);				//?? this makes no sense

	}

	public void insertNodeNonFull(BTreeNode node, long val) {	
		int i = node.getCurrentlyStored();

		// if node is a leaf
		if(node.getIsLeaf()) {

			while(i >= 1 && val < node.getBTreeObject(i-1).getKey()) {				//this line and next i had to change i to i-1 -- probaly going to have to for remainder as well
				node.setBTreeObject(node.getBTreeObject(i-1), i);
				i--;
			}
			
			BTreeObject obj = new BTreeObject(val);
			node.setBTreeObject(obj, i);
			node.incrementCurrentlyStored();	
		}
		
		// if node is not a leaf
		else{
			 i = 0;
			while(i < node.getCurrentlyStored() && val > node.getBTreeObject(i).getKey()) {
				i++;
			}
			
			BTreeNode childNode = node.getChildNode(i);								/// 	THIS FUCKING BREAKS IT!!!!!!!!!!!!!!!!!!11

			if(childNode.getCurrentlyStored() ==  m-1) {
				
				
				insertNodeNonFull(childNode,val);
				BTreeSplitChild(node,val);
				
				
				
//				BTreeNode parentNode = childNode.getParentPointer();
//				parentNode.setIsLeaf(false);
//			
//				parentNode.setChildPointer(childNode, parentNode.getCurrentlyStored());
//				parentNode.setBTreeObject(childNode.getBTreeObject(middle), parentNode.getCurrentlyStored());
//				childNode.setParentPointer(parentNode);
//
//				BTreeSplitChild(parentNode, val);
//				i = parentNode.getCurrentlyStored();
//				
//				childNode = parentNode;
			}
			else {
				insertNodeNonFull(childNode, val);
			}
			
		}
	}

	public void printTree() {
		System.out.println(" ");	//blank space
		root.traverse();
	}
	public void promote() {

	}
}
