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
			boolean temp = checkChildIsFull(childNode, val);
			return temp;
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
		}else {
			insertNodeNonFull(root, val);	
		}
	}
	public void BTreeSplitChild(BTreeNode parent, long zz) {																		// SPLIT CHILD

		//set Z equal to a new null node, Y is equal to the appropriate child node
		BTreeNode z = new BTreeNode(m);		//z = new right child
		int i = parent.getCurrentlyStored();
		i = 0;
		while(i < parent.getCurrentlyStored() && zz > parent.getBTreeObject(i).getKey()) {
			i++;
		}
		BTreeNode y = parent.getChildNode(i);
		z.setIsLeaf(y.getIsLeaf());

		// set parent pointers
		y.setParentPointer(parent);
		z.setParentPointer(parent);
		
		// promote middle-1 key to parent 
		i = parent.getCurrentlyStored();
		while(i >= 1 && zz < parent.getBTreeObject(i-1).getKey()) {
			node.setBTreeObject(parent.getBTreeObject(i-1), i);
			i--;
		}
		parent.setBTreeObject(y.getBTreeObject(middle-1), i);
		parent.incrementCurrentlyStored();
		
		// take top half objects from array y and put in array z
		for(int j =0; j<middle; j++) {
			z.setBTreeObject(y.getBTreeObject(j+middle), j); //z.key = y.key+t
		}
		
		
		//set child pointer to new child
		i = parent.getCurrentlyStored();
		while(i >= 1 && z.getBTreeObject(0).getKey() < parent.getBTreeObject(i-1).getKey()) {
			parent.setChildPointer(parent.getChildNode(i-1), i);
			i--;
		}
		parent.setChildPointer(z, i);



		// if y is not a leaf we need to assign the correct child pointers for child
		if(!y.getIsLeaf()) {	//if y is not a leaf
			for(int j = 0; j < middle+1;j++) {
				z.setChildPointer(y.getChildNode(j+middle), j); 		//sets z's child pointers to that of y+t
			}
		}

		y.setCurrentlyStored(middle-1);
		z.setCurrentlyStored(middle);	
	}

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
		
		// if node is not a leaf, then we go down to the correct child
		else{
			i = 0;
			while(i < node.getCurrentlyStored() && val > node.getBTreeObject(i).getKey()) {
				i++;
			}
			
			BTreeNode childNode = node.getChildNode(i);	
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

	public void printTree() {
		System.out.println(" ");	//blank space
		root.traverse();
		print();
	}
	   public void print () {
		    // Print a textual representation of this B-tree.
		        printSubtree(root, "");
		    }

		    private static void printSubtree (BTreeNode top, String indent) {
		    // Print a textual representation of the subtree of this B-tree whose
		    // topmost node is top, indented by the string of spaces indent.
		        if (top == null)
		            System.out.println(indent + "o");
		        else {
		            System.out.println(indent + "o-->");
		            boolean isLeaf = top.getIsLeaf();
		            String childIndent = indent + "    ";
		            for (int i = 0; i < top.getCurrentlyStored(); i++) {
		                if (! isLeaf)  printSubtree(top.getChildNode(i), childIndent);
		                System.out.println(childIndent + top.getBTreeObject(i).getKey());
		            }
		            if (! isLeaf)  printSubtree(top.getChildNode(top.getCurrentlyStored()), childIndent);
		        }
		    }
}
