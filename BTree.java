import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BTree {

	private BTreeNode root, current, parent, child, node;
	private int m;
	private File file;
	private int t;
	private int middle; 			// is the middle node

	public BTree(String name, int t)  {
		//make a new file to write to. M is number of values per node, call btreecreate to get a tree with 1 node
		this.t = t;
		m= t*2;
		m = (int)Math.floor(m);	//find middle node
		root = BTreeCreateNode();
	}

	public BTreeNode BTreeCreateNode()  {
		BTreeNode node = new BTreeNode();
		return node;
	}

	public void BTreeInsert(BTreeNode node,long val)  {
		
		node = root;
		if(node.getCurrentlyStored() == m) {
			BTreeNode s = new BTreeNode();
			root = s;
			s.setIsLeaf(false);
			s.setCurrentlyStored(0);
			s.setChildPointer(root, 0);
			BTreeSplitChild(s,1);
			insertNodeNonFull(s,val);
		}else {
			insertNodeNonFull(root, val);	
		}
	}
	public void BTreeSplitChild(BTreeNode x, long val) {
		
		//*** changed for loops to start at zero since arrays being used start at 0
		
		BTreeNode z = new BTreeNode();
		BTreeNode y = x.getChildNode(0);
		z.setIsLeaf(y.getIsLeaf());
		//in psuedocode we set z's n to t-l, but i ddont know if we need to if we call that value from Tree and set globally
		for(int j =1; j<t-1; j++) {
			z.setBTreeObject(y.getBTreeObject(j+t), j); //z.key = y.key+t
		}
		if(!y.getIsLeaf()) {	//if y is not a leaf
			for(int j = 1; j < t-1;j++) {
				z.setChildPointer(y.getChildNode(j+t), j); 		//sets z's child pointers to that of y+t
			}
		}
		y.setCurrentlyStored(t-1);
		
		for(int j = x.getCurrentlyStored()+1; j > middle+1; j--) {		//what is i?
			x.setChildPointer(x.getChildNode(j+1), j);
		}
		x.setChildPointer(z, middle+1);
		for(int j = x.getCurrentlyStored(); j > middle; j--) {
			x.setBTreeObject(x.getBTreeObject(j), j+1);
		}
		x.setBTreeObject(y.getBTreeObject(t) ,middle+1);
		x.setCurrentlyStored(x.getCurrentlyStored()+1);
	}
	
	/**
	 * check if the node contains the value. if it does increment the frequency and end insertion. 
	 * if !found then if it is a leaf insert the node, if not a leaf then look for proper child node
	 * @param node
	 * @param val
	 */
	public void insertNodeNonFull(BTreeNode node, long val) {	

		BTreeObject obj = new BTreeObject(val);
		int i = node.getCurrentlyStored();
		boolean found = false;
		for(int j = 0; j < node.getCurrentlyStored(); j++) {

			if (val == node.getBTreeObject(j).getKey()) {
				found = true;
				node.getBTreeObject(j).incrementFrequency(); //means it was found
			}
		}
		if(!found) {
			if(node.getIsLeaf()) {
				while(i >= 1 && val < node.getBTreeObject(i).getKey()) { //while i > 1 and val is les than the objects key
					node.setBTreeObject(node.getBTreeObject(i-1),i) ;
					i--;
				}
				node.setBTreeObject(obj, i);
				node.incrementCurrentlyStored();

			}else {
				while(i >= 1 && val < node.getBTreeObject(i).getKey()) { //while i > 1 and val is les than the objects key
					i--;
				}
				i++;	// i now is equal to the child node location
				node = node.getChildNode(i);
			}
		}
	}
	
}
