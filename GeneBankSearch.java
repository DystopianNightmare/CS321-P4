import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneBankSearch {

	private static Integer t; 		//degree to be used -- this is the value for degree
	//	private static BufferedReader br;
	private static int cacheSize;		//if cache is used this will be the cache size
	private static int debugLevel = -1;		//to be used later
	private static boolean useCache = false; //false if arg[0] is 0
	private static File BTreeFile;
	private static File QueryFile;
	private static RandomAccessFile RAF;
	private static Cache cache;
	private static long time;

	public static void main(String[] args) {
		
		
		//use a try-catch to parse all arguments. incorrect param will print usage
		try {
			//use caching or not
			if(Integer.parseInt(args[0]) == 1) {
				useCache = true;
				cacheSize = Integer.parseInt(args[3]);
				cache = new Cache(cacheSize);
				if(args.length ==5) {								//if cache is being used a cache, debug is in position 5 of args
					debugLevel = Integer.parseInt(args[4]);
				}
			}else {
				if(args.length == 5) {								////if cache is being used a cache, debug is in position 4 of array since cache size is not used
					debugLevel = Integer.parseInt(args[3]);
				}
			}
			//make files from args
			BTreeFile = new File(args[1]);
			QueryFile = new File(args[2]);

			Pattern p = Pattern.compile("-?\\d+");
			Matcher m = p.matcher(args[1]);
			String[] arr = new String[5];
			int j = 0;
			while(m.find()) {
				arr[j] = m.group();
				j++;
			}
			BTree tree = new BTree();

			RAF = new RandomAccessFile(BTreeFile,"r");
			File tmpFile = tree.buildTree(RAF);


			String pwFileName ="test" +arr[0] +"_query" + arr[1] + "_result";
			PrintWriter pw = new PrintWriter(pwFileName);
			Scanner scanQ = new Scanner(QueryFile);
			String lineQ;
			String lineTmp;
			Long tmp;


			while(scanQ.hasNext()) {
				Scanner scanTmp = new Scanner(tmpFile);
				lineQ=scanQ.nextLine();

				if(useCache) {
					boolean found = false;
					while(scanTmp.hasNextLine() && !found) {
						lineTmp=scanTmp.nextLine();
						int i = 0;
						while(lineTmp.charAt(i) != ':') {
							i++;
						}
						String end = (String) lineTmp.substring(i, lineTmp.length());
						String temp = (String) cache.search(lineTmp.substring(0, i));

						if(temp != null ) {
							if(temp.equals(lineQ)) {
								found = true;
								pw.println(lineQ.toLowerCase() + lineTmp.substring(i, lineTmp.length()));
								
							}
						}else {
							long key = convert(lineQ);
							String string = Long.toString(key);
							if(lineTmp.substring(0, i).contentEquals(string)) {
								found = true;
								cache.addToCache(lineTmp.substring(0, i),lineQ);
								pw.println(lineQ.toLowerCase() + lineTmp.substring(i, lineTmp.length()));
								
							}
						}
					}
				}else {
					long key = convert(lineQ);
					String string = Long.toString(key);
					while(scanTmp.hasNextLine()) {
						lineTmp=scanTmp.nextLine();
						int i = 0;
						while(lineTmp.charAt(i) != ':') {
							i++;
						}
						if(lineTmp.substring(0, i).contentEquals(string)) {
							pw.println(lineQ.toLowerCase() + lineTmp.subSequence(i, lineTmp.length()));
						}
					}
					scanTmp.close();
				}
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

	public static void printUsage() {
		System.out.println("java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
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
