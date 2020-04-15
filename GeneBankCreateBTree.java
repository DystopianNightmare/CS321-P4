import java.io.FileReader;
import java.util.Scanner;

/**
 * Main driver file to read file and parse data
 * @author josh
 *
 */
public class GeneBankCreateBTree {

	private static Integer degree; 		//degree to be used
	//	private static BufferedReader br;
	private static int cacheSize;
	private static int debugLevel;		//to be used later
	private static boolean useCache = false;
	private static int sequenceLength;

	public static void main(String[] args) {

		//use a try-catch to parse all arguments. incorrect param will print usage
		try {
			//use caching or not
			if(Integer.parseInt(args[0]) == 1) {
				useCache = true;
				cacheSize = Integer.parseInt(args[4]);
				if(args[5] != null) {								//if cache is being used a cache, debug is in position 5 of args
					debugLevel = Integer.parseInt(args[5]);
				}
			}else {
				if(args.length == 5) {								////if cache is being used a cache, debug is in position 4 of array since cache size is not used
					debugLevel = Integer.parseInt(args[4]);
				}
			}

			//parse degree
			degree = Integer.parseInt(args[1]);	
			//get sequence length
			sequenceLength = Integer.parseInt(args[3]);
			if(sequenceLength < 0 || sequenceLength > 31) {
				throw new Exception();
			}
			//parse debug level
		}catch(Exception e) {
			printUsage();
		}

		//get gbk file and parse
		String fileName = args[2];
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
							if(line.charAt(i) == 'a' || line.charAt(i) == 'c' || line.charAt(i) == 'g' || line.charAt(i) == 't') {
								character += line.charAt(i);
							}
						}
						line = scan.nextLine();
					}

					for(int i = 0; i < character.length()-sequenceLength+1;i++) {
						String s = "";
						for(int j = 0; j < sequenceLength; j++) {
							if(character.charAt(i+j) == 'a') {
								s += "00";
							}
							if(character.charAt(i+j) == 't') {
								s += "11";
							}
							if(character.charAt(i+j) == 'c') {
								s += "01";
							}
							if(character.charAt(i+j) == 'g') {
								s += "10";
							}
						}
						//OKAY! We now have s which we will use
						// s is the proper length and only holds acgt represented as a string of 1's and 0's
						
						Long key = Long.parseLong(s);
						BTreeObject treeO = new BTreeObject(key);
						
					}

				}

			}
			scan.close();
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}
	public static void printUsage() {
		System.out.println("java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
	}
}
