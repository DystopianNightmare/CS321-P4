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

	public void BTreeInsert(long val)  {																					// INSERT

		node = root;
		if(node.getCurrentlyStored() == m-1) {

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
	public void BTreeSplitChild(BTreeNode x, long zz) {																		// SPLIT CHILD

		//*** changed for loops to start at zero since arrays being used start at 0

		BTreeNode z = new BTreeNode(m);		//z = new right child
		BTreeNode y = x.getChildNode(x.getCurrentlyStored());	//y = old root
		y.setIsLeaf(true);

		y.setParentPointer(x);
		z.setParentPointer(x);

		x.setBTreeObject(y.getBTreeObject(middle), x.getCurrentlyStored());
		x.incrementCurrentlyStored();
		System.out.println(x.getCurrentlyStored() );
		x.setChildPointer(z, x.getCurrentlyStored());
		z.setIsLeaf(y.getIsLeaf());

		//in psuedocode we set z's n to t-l, but i ddont know if we need to if we call that value from Tree and set globally

		for(int j =0; j<t; j++) {
			z.setBTreeObject(y.getBTreeObject(j+middle), j); //z.key = y.key+t
		}

		if(!y.getIsLeaf()) {	//if y is not a leaf
			for(int j = 0; j < t;j++) {
				z.setChildPointer(y.getChildNode(j+middle), j); 		//sets z's child pointers to that of y+t
			}
		}

		y.setCurrentlyStored(middle);
		z.setCurrentlyStored(middle-1);

		for(int j = x.getCurrentlyStored(); j > middle; j--) {		//what is i?
			x.setChildPointer(x.getChildNode(j), j-1);
		}

		x.setChildPointer(z, x.getCurrentlyStored());

		for(int j = x.getCurrentlyStored(); j > middle; j--) {
			z.setBTreeObject(x.getBTreeObject(j-1), j);
		}
		x.setBTreeObject(y.getBTreeObject(t) ,middle);

	}

	public void insertNodeNonFull(BTreeNode node, long val) {	
		int i = node.getCurrentlyStored();

		if(node.getIsLeaf()) {

			while(i >= 1 && val < node.getBTreeObject(i-1).getKey()) {				//this line and next i had to change i to i-1 -- probaly going to have to for remainder as well
				node.setBTreeObject(node.getBTreeObject(i-1), i);
				i--;
			}
			BTreeObject obj = new BTreeObject(val);
			node.setBTreeObject(obj, i);
			node.incrementCurrentlyStored();	
		}else{
			 i = 0;
			while(i < node.getCurrentlyStored() && val > node.getBTreeObject(i).getKey()) {
				i++;
			}
			
			node = node.getChildNode(i);								/// 	THIS FUCKING BREAKS IT!!!!!!!!!!!!!!!!!!11

			if(node.getCurrentlyStored() ==  m-1) {
				BTreeNode s = node.getParentPointer();
				s.setIsLeaf(false);
			
				s.setChildPointer(node, s.getCurrentlyStored());
				s.setBTreeObject(node.getBTreeObject(middle), s.getCurrentlyStored());
				node.setParentPointer(s);

				BTreeSplitChild(s, val);
				i = s.getCurrentlyStored();
				
				node = s;
			}
			insertNodeNonFull(node, val);
			
		}
	}

	public void printTree() {
		System.out.println(" ");	//blank space
		root.traverse();
	}
	public void promote() {

	}
}