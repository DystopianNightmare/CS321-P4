import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneBankSearch {

	private static Integer t; 		//degree to be used -- this is the value for degree
	//	private static BufferedReader br;
	private static int cacheSize;		//if cache is used this will be the cache size
	private static int debugLevel;		//to be used later
	private static boolean useCache = false; //false if arg[0] is 0
	private static File BTreeFile;
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
			
			PrintWriter pw = new PrintWriter(arr[0] +"_query" + arr[1] + "_result");
			Scanner scanQ = new Scanner(QueryFile);
			String lineQ;
			String lineB;
			
			while(scanQ.hasNext()) {
				Scanner scanB = new Scanner(BTreeFile);
				lineB = scanB.nextLine();
				lineQ=scanQ.nextLine().toUpperCase();
				while(scanB.hasNextLine()) {
					if(lineB.contains(lineQ)) {
						System.out.println(lineB);
						pw.println(lineB);
					}

					lineB=scanB.nextLine();
				}
				scanB.close();
			}
			scanQ.close();
			pw.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}




	public static void printUsage() {
		System.out.println("java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
	}
}
