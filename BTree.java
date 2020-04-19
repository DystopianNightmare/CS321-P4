import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BTree {

	private BTreeNode root, current, parent, child, node;
	private int m;
	private File file;
	
	public BTree(String name, int m) throws IOException {
		//make a new file to write to. M is number of values per node, call btreecreate to get a tree with 1 node
		File file = new File(name);
		this.m = m;
		
		BTreeCreateNode();
	}

	public BTreeNode BTreeCreateNode() throws IOException {
		//this is only called when we first create the tree
		 node = new BTreeNode(file,m);
		return node;
	}
	
	public void addObjectToNode(BTreeObject obj) throws IOException {
		// we have a tree object. lets search for it and either add it to the node or increment frequency
		//check if obj is in the tree
		
		
	}
	public void print() throws IOException {
		//test print method to print the byte[] digit by digit
		
		 InputStream iS = new FileInputStream(node.getFile());
		 int j = iS.read();
		 System.out.println(j);
			System.out.println(iS.readAllBytes().toString());
		iS.close();
	}
	
	
}
