import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class will search a BTree for values specified in a query file 
 * @author Josh, Joe, Kate
 *
 */
public class GeneBankSearch {

	private static int cacheSize;		//if cache is used this will be the cache size
	private static int debugLevel = -1;		//to be used later
	private static boolean useCache = false; //false if arg[0] is 0
	private static File BTreeFile;
	private static File QueryFile;
	private static RandomAccessFile RAF;
 @SuppressWarnings("rawtypes")
	private static Cache cache;


@SuppressWarnings({ "rawtypes", "unchecked" })
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
				if(args.length == 4) {								////if cache is being used a cache, debug is in position 4 of array since cache size is not used
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

			String pwFileName ="test" +arr[0] +"_query" + arr[1] + "_result";
			PrintWriter pw = new PrintWriter(pwFileName);
			Scanner scanQ = new Scanner(QueryFile);
			String lineQ;
		

			while(scanQ.hasNext()) {
				lineQ=scanQ.nextLine();


				Long keyQ;
				if(useCache) {

					if((keyQ=((Long)(cache.search(lineQ)))) != null ) {
						String results = tree.readFileSearch(RAF, keyQ);
						if (!results.contentEquals("none")) {
							pw.println(results);
							if(debugLevel == 0) {
								System.out.println(results);
							}
						}
					}
					else {
						keyQ = convert(lineQ);
						cache.addToCache(lineQ, keyQ);
						String results = tree.readFileSearch(RAF, keyQ);
						if (!results.contentEquals("none")) {
							pw.println(results);
							if(debugLevel == 0) {
								System.out.println(results);
							}
						}						
					}
				}
				else {
					keyQ = convert(lineQ);
					String results = tree.readFileSearch(RAF, keyQ);
					if (!results.contentEquals("none")) {
						pw.println(results);
						if(debugLevel == 0) {
							System.out.println(results);
						}
					}				
				}
			}

			scanQ.close();
			pw.close();


		}catch(Exception e) {
			e.printStackTrace();
		}


		
		
	}

	/**
	 * print usage
	 */
	public static void printUsage() {
		System.out.println("java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
	}

	/**
	 * converts a string of genes to a binary long
	 * @param character
	 * @return
	 */
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
