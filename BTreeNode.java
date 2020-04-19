import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public class BTreeNode {

	private int[] childPointer;
	private int parentPointer;
	private int currentlyStored;
	private boolean isLeaf, isRoot;
	private byte[] node;
	private int m;
	private File file2;
	private int testOffset;
	private int testLength;

	//default constructor only to be used when creating Btree with no elements
	public BTreeNode(File file, int m) throws IOException {
		node = new byte[4096] ;
		this.m = m;
		isLeaf = true;
		currentlyStored = 0;

		BigInteger bigInt = BigInteger.valueOf(m);      //Test to add m to the first positon in the byte array
		byte[] d =  bigInt.toByteArray();
		for(int i = 0; i < d.length; i++) {
			node[i] = d[i];
		}

		testLength = 200;

		file2 = new File("test");  			//Testto make and write to a file
		OutputStream output = null;

		output = new FileOutputStream(file2);

		output.write(node, 0, testLength );

	}
	public File getFile() {
		return file2;
	}

	public void addObj(long val) throws IOException {
		currentlyStored++;
		BTreeObject obj = new BTreeObject(val);
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(boas);
		oos.writeObject(obj);
		byte[] data = boas.toByteArray();

		//		System.out.println("here");
		//		System.out.println(data.length);
		//		for(int i = 0; i < data.length; i++) {
		//			System.out.println(data[i]);
		//			
		//		}
		copyTreeObjectToNode(data);


	}
	public void copyTreeObjectToNode(byte[] data) throws IOException {

	}
	//	public int getM() {
	//		return m;
	//	}
	//	public byte[] getNode() {
	//		return node;
	//	}


	public void printNode() {
		for(int i = 0; i < 4096; i++) {
			System.out.println(node[i]);
		}
	}
}
