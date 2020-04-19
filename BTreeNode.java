import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class BTreeNode {

private int[] childPointer;
private int parentPointer;
private int currentlyStored;
private boolean isLeaf, isRoot;
private byte[] node;
private int m;
private File file2;
//default constructor only to be used when creating Btree with no elements
	public BTreeNode(File file, int m) throws IOException {
		node = new byte[4096]; 
		isLeaf = true;
		currentlyStored = 0;
		this.m = m;
		copyMToNode(m);
		//
		 file2 = new File("te");
		OutputStream output = null;
		
		 output = new FileOutputStream(file2, true);
		 
		String s = "test";
		
		output.write(s.getBytes());
		System.out.println("here");
	
	}
	public File getFile() {
		return file2;
	}
	
//	public void addObj(long val) throws IOException {
//		currentlyStored++;
//		BTreeObject obj = new BTreeObject(val);
//		ByteArrayOutputStream boas = new ByteArrayOutputStream();
//		ObjectOutputStream oos = new ObjectOutputStream(boas);
//		oos.writeObject(obj);
//		byte[] data = boas.toByteArray();
//		copyTreeObjectToNode(data);
//		
//		
//	}
//	public void copyTreeObjectToNode(byte[] data) {
//		for(int i = 0; i < data.length; i++) {
//			node[(200*currentlyStored)+i] = data[i];
//		}
//	}
//	public int getM() {
//		return m;
//	}
//	public byte[] getNode() {
//		return node;
//	}
	public void copyMToNode(int m) throws IOException {
		Integer m2 = (Integer) (m);
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(boas);
		oos.writeObject(m2);
		byte[] data = boas.toByteArray();
		for(int i = 0; i < data.length; i++) {
			node[(200*currentlyStored)+i] = data[i];
		}
	}
	
	public void printNode() {
		for(int i = 0; i < 4096; i++) {
			System.out.println(node[i]);
		}
	}
}
