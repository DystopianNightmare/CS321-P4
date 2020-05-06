import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneBankSearch {

	private static Integer t; 		//degree to be used -- this is the value for degree
	//	private static BufferedReader br;
	private static int cacheSize;		//if cache is used this will be the cache size
	private static int debugLevel;		//to be used later
	private static boolean useCache = false; //false if arg[0] is 0
	private static RandomAccessFile BTreeFile;
	private static File QueryFile;


	public static void main(String[] args) {

		//use a try-catch to parse all arguments. incorrect param will print usage
		try {
			//use caching or not
			if(Integer.parseInt(args[0]) == 1) {
				useCache = true;
				cacheSize = Integer.parseInt(args[3]);
				if(args[4] != null) {								//if cache is being used a cache, debug is in position 5 of args
					debugLevel = Integer.parseInt(args[4]);
				}
			}else {
				if(args.length == 5) {								////if cache is being used a cache, debug is in position 4 of array since cache size is not used
					debugLevel = Integer.parseInt(args[4]);
				}
			}
			//make files from args
			BTreeFile = new RandomAccessFile(args[1], "rw");
			QueryFile = new File(args[2]);

			Pattern p = Pattern.compile("-?\\d+");
			Matcher m = p.matcher(args[1]);
			String[] arr = new String[5];
			int j = 0;
			while(m.find()) {
				arr[j] = m.group();
				j++;
			}

			
			BTree searchingTree = new BTree();
			PrintWriter pw = new PrintWriter("test" +arr[0] +"_query" + arr[1] + "_result");
			Scanner scanQ = new Scanner(QueryFile);
			String lineQ;
			String lineB;

			while(scanQ.hasNext()) {
				lineQ=scanQ.nextLine();
				
				
				
				///  **** HELP **** i have no idea. I need lineQ to be a binary key with cache *******
				// Cache implemented below 
				long keyQ = convert(lineQ);
//				Long tmp;
//				if(useCache) {
//					if((tmp=((Long)(cache.search(s)))) != null ) {
//						tree.BTreeInsert(tmp);
//						
//					}else {
//						long key = convert(s);
//						String temp = tree.keyToGene((tree.padZero(key, sequenceLength*2)));
//						cache.addToCache(s,key);
//						tree.BTreeInsert(key);
//						
//					}
//				}else {
//					long key = convert(lineQ);
//					String temp = tree.keyToGene((tree.padZero(key, sequenceLength*2)));
//					tree.BTreeInsert(key);
//				}
//				
				// ^^ cache ... ?????
				
				System.out.println(searchingTree.readFileSearch(BTreeFile, keyQ));
				pw.println(searchingTree.readFileSearch(BTreeFile, keyQ));
				
				
				
				
				
				
//				
//				Scanner scanB = new Scanner(BTreeFile);
//				lineB = scanB.nextLine();
//				lineQ=scanQ.nextLine().toUpperCase();
//				while(scanB.hasNextLine()) {
//					if(lineB.contains(lineQ)) {
//						System.out.println(lineB);
//						pw.println(lineB);
//					}
//
//					lineB=scanB.nextLine();
//				}
//				scanB.close();
			}
			scanQ.close();
			pw.close();

		}catch(Exception e) {
			e.printStackTrace();
		}


		if(debugLevel == 0) {
			System.out.println(" print the query output to STDOUT");
		}
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
	
	
	
	
	public static void printUsage() {
		System.out.println("java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
	}
}