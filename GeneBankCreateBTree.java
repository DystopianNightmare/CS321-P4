import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main driver file to read file and parse data
 * @author josh
 *
 */
public class GeneBankCreateBTree {

	private static Integer t; 		//degree to be used -- this is the value for degree
	//	private static BufferedReader br;
	private static int cacheSize;		//if cache is used this will be the cache size
	private static int debugLevel = 3;		//to be used later
	private static boolean useCache = false; //false if arg[0] is 0
	private static int sequenceLength;		//this is the k value
	private static  String nameOfTree; // this will be the name of the binary file
	private BTree tree;
	private static Cache cache;


	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {

		//use a try-catch to parse all arguments. incorrect param will print usage
		try {
			//use caching or not
			if(Integer.parseInt(args[0]) == 1) {
				useCache = true;
				cacheSize = Integer.parseInt(args[4]);
				cache = new Cache(cacheSize);

				if(args.length == 6) {								//if cache is being used a cache, debug is in position 5 of args
					debugLevel = Integer.parseInt(args[5]);
				}
			}else {
				if(args.length == 5) {								////if cache is being used a cache, debug is in position 4 of array since cache size is not used
					debugLevel = Integer.parseInt(args[4]);
				}
			}

			//parse degree
			t = Integer.parseInt(args[1]);	
			if(t == 0 ) {
				t=31;		//optimal degree
			}
			//get sequence length
			sequenceLength = Integer.parseInt(args[3]);
			if(sequenceLength < 0 || sequenceLength > 31) {
				throw new Exception();
			}
			//parse debug level
		}catch(Exception e) {
			e.printStackTrace();
			printUsage();
		}

		//get gbk file
		String fileName = args[2];

		//Get name of Binary file and make a new BTree

		nameOfTree = (fileName + ".btree.data." + sequenceLength  );	//This is the name of the binary file

		BTree tree = new BTree(nameOfTree,t,sequenceLength);

		try {
			Scanner scan = new Scanner(new FileReader(fileName));
			String line;

			while(scan.hasNextLine()) {
				line = scan.nextLine();

				while(line.startsWith("ORIGIN")) {
					String character = "";
					line =  scan.nextLine();

					while(!line.startsWith("//")) {
						for(int i = 0; i < line.length(); i++) {
							if(line.charAt(i) == 'a' || line.charAt(i) == 'c' || line.charAt(i) == 'g' || line.charAt(i) == 't' || line.charAt(i) == 'n'|| line.charAt(i) == 'A'|| line.charAt(i) == 'C'|| line.charAt(i) == 'G'|| line.charAt(i) == 'T') {
								character += line.charAt(i);
							}
						}
						line = scan.nextLine();
					}
					//make string of values
					for(int i = 0; i < character.length()-sequenceLength+1;i++) {
						String s = "";
						for(int j = 0; j < sequenceLength; j++) {
							s += character.charAt((i+j));

						}
					
						if(!s.contains("n")) {
							Long tmp;
							if(useCache) {
								if((tmp=((Long)(cache.search(s)))) != null ) {
									tree.BTreeInsert(tmp);
									
								}else {
									long key = convert(s);
									String temp = tree.keyToGene((tree.padZero(key, sequenceLength*2)));
									cache.addToCache(s,key);
									tree.BTreeInsert(key);
									
								}
							}else {
								long key = convert(s);
								String temp = tree.keyToGene((tree.padZero(key, sequenceLength*2)));
								tree.BTreeInsert(key);
							}
						}
					}
				}
			}
			scan.close();
			tree.dumpTree(sequenceLength*2);
			
		} catch ( Exception e) {
			e.printStackTrace();
		}

		if(debugLevel == 0) {
			System.err.println("No Status Messages. Please follow this usage example: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
		}
		if(debugLevel == 1) {
			tree.dumpTree(sequenceLength*2);
		}
		

	}
	public static void printUsage() {
		System.out.println("java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
	}
	public static long convert(String character) {

		String s = "";
		for(int j = 0; j < character.length(); j++) {


			if(character.charAt(j) == 'a' || character.charAt(j) == 'A') {
				s += "00";
			}
			if(character.charAt(j) == 't' || character.charAt(j) == 'T') {
				s += "11";
			}
			if(character.charAt(j) == 'c' || character.charAt(j) == 'C') {
				s += "01";
			}
			if(character.charAt(j) == 'g' || character.charAt(j) == 'G') {
				s += "10";
			}
			if(character.charAt(j) == 'n' || character.charAt(j) == 'N') {
				s += "n";


			}
		}

		Long key = Long.parseLong(s);
		return key;	

	}
}