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
		middle = (int)Math.floor(t);	//find middle node

		root = BTreeCreateNode();
	}

	public BTreeNode BTreeCreateNode()  {
		node = new BTreeNode(m);
		
		return node;
	}

	public void BTreeInsert(long val)  {
		
		node = root;
		if(node.getCurrentlyStored() == m-1) {
			System.out.println(m +"m");
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
	public void BTreeSplitChild(BTreeNode x, long zz) {

		//*** changed for loops to start at zero since arrays being used start at 0

		BTreeNode z = new BTreeNode(m);		//z = new right child
		BTreeNode y = x.getChildNode(0);	//y = old root
		x.setBTreeObject(y.getBTreeObject(middle), 0);
		x.incrementCurrentlyStored();
		x.setChildPointer(z, 1);
		z.setIsLeaf(y.getIsLeaf());
		
		//in psuedocode we set z's n to t-l, but i ddont know if we need to if we call that value from Tree and set globally
		
		for(int j =1; j<t; j++) {
			z.setBTreeObject(y.getBTreeObject(j+middle), j-1); //z.key = y.key+t
		}
		if(!y.getIsLeaf()) {	//if y is not a leaf
			for(int j = 1; j < t-1;j++) {
				z.setChildPointer(y.getChildNode(j+middle), j); 		//sets z's child pointers to that of y+t
			}
		}
		
		y.setCurrentlyStored(5);
		z.setCurrentlyStored(middle-1);
		x.setCurrentlyStored(1);
		

		for(int j = x.getCurrentlyStored()+1; j > middle+1; j--) {		//what is i?
			x.setChildPointer(x.getChildNode(j+1), j);
		}
		x.setChildPointer(z, middle+1);
		for(int j = x.getCurrentlyStored(); j > middle; j--) {
			z.setBTreeObject(x.getBTreeObject(j), j+1);
		}
		x.setBTreeObject(y.getBTreeObject(t) ,middle);
		x.setCurrentlyStored(x.getCurrentlyStored());
		
		
		
		
	}

	public void insertNodeNonFull(BTreeNode node1, long val) {	
		int i = node.getCurrentlyStored();
		if(node.getIsLeaf()) {
			
			while(i >= 1 && val < node.getBTreeObject(i-1).getKey()) {				//this line and next i had to change i to i-1 -- probsaly going to have to for remainder as well
				node.setBTreeObject(node.getBTreeObject(i-1), i);
				i--;
			}
			BTreeObject obj = new BTreeObject(val);
			node1.setBTreeObject(obj, i);
			node.incrementCurrentlyStored();
		}else{
			while(i >= 1 && val < node.getBTreeObject(i-1).getKey()) {
				i--;
			}
			i++;
			node = node.getChildNode(i);
			if(node.getCurrentlyStored() ==  m-1) {
				BTreeSplitChild(node, i);
				if(val > node.getBTreeObject(i).getKey()) {
					i++;
				}
			}
			insertNodeNonFull(node.getChildNode(i), val);
		}
	}

	public void printTree() {
		System.out.println(" ");	//blank space
		root.traverse();
	}
}
//
//	public void print() throws IOException {
//		//test print method to print the byte[] digit by digit
//		
//		 InputStream iS = new FileInputStream(node.getFile());
//		 //int j = iS.read();
//		 byte[] b = new byte[(int)node.getFile().length()];
//		 iS.read(b);
//		 for(int i = 0; i < 300; i++) {
//			 System.out.println(b[i]);
//		 }
//		iS.close();
//	}
