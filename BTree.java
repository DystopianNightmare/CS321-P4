import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BTree {

	private BTreeNode root, current, parent, child, node;
	private int m;
	private File file;

	public BTree(String name, int m)  {
		//make a new file to write to. M is number of values per node, call btreecreate to get a tree with 1 node
		this.m = m;
		root = BTreeCreateNode(m);
	}

	public BTreeNode BTreeCreateNode(int m)  {
		BTreeNode node = new BTreeNode(m);
		return node;
	}

	public void insertNode(long val)  {
		//search to where to find it and if its found increment freq count
		BTreeNode node = root;




	}

	public void BTreeSearch(BTreeNode node,long val) {

	}
	public void BTreeSplitNode(BTreeNode node ) {

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
	public void insertNodeFull(BTreeNode node, long val) {
		//have to split the node and add
	}
}
