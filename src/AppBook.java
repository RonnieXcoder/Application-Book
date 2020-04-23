
/////////////////////////////////////////////////////////////////////////////////
//
//	AppBook.java
//	@author Eugene Bereza 21.04.2020 
//  
//	description: Application Book Management Fundamentals
//
/////////////////////////////////////////////////////////////////////////////////

import java.nio.charset.Charset;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*; 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


 
public class AppBook {
	
    private static final String FILE_NAME = "appbookDB.bin";
    private static final String OUT_FILE_NAME = "out_appbookDB.bin";
    
	private static String curStrbest_bid = null; 
	private static String curStrbest_ask = null; 
	private static String bestBid = null;
	private static String bestAsk = null;
	private static List<String> bid =  new ArrayList<String>();
	private static List<String> ask =  new ArrayList<String>();
	private static List<String> output =  new ArrayList<String>();

    
    private static void ShowMainMenu()
    {
    	System.out.println("Press 1 to add data and process \'" + FILE_NAME + "\' file (Type exit to stop collect data)");
    	System.out.println("Press 2 to process \'" + FILE_NAME + "\' file and write result to \'" + OUT_FILE_NAME + "\'");
    }
    
    public static void main(String[] args) throws Exception {
    	
    	ShowMainMenu();
    	
    	Scanner in = new Scanner(System.in);
        int num = in.nextInt();
       
       
        
        if(num == 1)
        {
        	CollectData();
        }
        else if(num == 2)
        {
        	ProcessFile();
        	WriteToFile();
        }
    	
    };
    
    
    private static void CollectData() throws IOException
    {
    	Scanner in = new Scanner(System.in);
    	ArrayList<String> lines = new ArrayList<String>();
    	
    	String line = null;
  
    	
    	while(true) {
    		
    		System.out.print(">");
    		
    		line = in.nextLine();
    		
    		if(line.equals("exit")) 
    			break;
    		
    		lines.add(line);
    	
    	}
    	
    	Path out = Paths.get(FILE_NAME);
    	
    	try {
			Files.write(out, lines ,Charset.defaultCharset());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    			
    	ProcessFile();
    	WriteToFile();
    	
    }
    
    public static int GetBestBidIndex()
    {
		Iterator<String> itr_bid =  bid.iterator(); 
		String subStr = null; 
		
		int parse_int = 0;
		int best_bid = 0;
		int bidIndex = 0;
		int bestbid_Index = 0;
		
		 while (itr_bid.hasNext())
	     {
			    curStrbest_bid = itr_bid.next();
			    subStr = curStrbest_bid.substring(2);
	        	subStr = subStr.substring(0, subStr.indexOf(","));
	        	parse_int = Integer.parseInt(subStr);
	        	
	        	
	        	
				if(best_bid < parse_int)
	        	{
	        		best_bid = parse_int;
	        			
	        		bestBid = curStrbest_bid; 
	        		
	        		bestbid_Index = bidIndex;
	        		
	        	}
				
				bidIndex++;
	        		
	     }
		 
	
		 
		 return bestbid_Index;
    }
    
    public static int GetBestAskIndex()
    {
    	Iterator<String> itr_ask = ask.iterator(); 
		String subStr = null; 
		
		
		int parse_int = 0;
		int best_ask = 0;
		int askIndex = 1;
		int bestask_Index = 0;
		
		String tStrbest_ask = itr_ask.next();
	    subStr = tStrbest_ask.substring(2);
    	subStr = subStr.substring(0, subStr.indexOf(","));
    	parse_int = Integer.parseInt(subStr);
    	bestAsk = tStrbest_ask;
    	best_ask = parse_int;
		
    	while (itr_ask.hasNext())
	     {
			    curStrbest_ask = itr_ask.next();
			    subStr = curStrbest_ask.substring(2);
	        	subStr = subStr.substring(0, subStr.indexOf(","));
	        	parse_int = Integer.parseInt(subStr);
	        	
	        	
	        	
	        	if(best_ask > parse_int)
	        	{
	        		best_ask = parse_int;
	        	
	        		bestAsk = curStrbest_ask; 
	        		tStrbest_ask = curStrbest_ask; 
	        		
	        		bestask_Index = askIndex;

	        	}
	        	
	        	askIndex++;

	     }
		
		return bestask_Index;
    }
    	
       
    private static void  ProcessFile()
    {
    	
    	int decValue = 0;
    	
    	String currentString = null;

    	           	List<String> l = null;
    	
		try {
			l = Files.readAllLines(Paths.get(FILE_NAME));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        
        Iterator<String> itr = l.iterator(); 
        
        while (itr.hasNext())
        {
          
        	currentString = itr.next();
        	
        	if(currentString.startsWith("u")) 
        	{
        		
        		if(currentString.substring(currentString.length() - 4).equals(",bid"))
        			bid.add(currentString);
        		else
        			ask.add(currentString);
        		

        	}
        	else if(currentString.startsWith("q,best_bid"))
        	{
        		
        		curStrbest_bid = bid.get(GetBestBidIndex());
        		
        		String line = curStrbest_bid.substring(curStrbest_bid.indexOf(",") + 1, curStrbest_bid.lastIndexOf(","));
        		 
        		System.out.println(line);
        		 
        		output.add(line);
        		 
        	}
        	else if(currentString.startsWith("q,best_ask"))
        	{
        		
        		curStrbest_ask = ask.get(GetBestAskIndex());
        		
        		String line = curStrbest_ask.substring(curStrbest_ask.indexOf(",") + 1, curStrbest_ask.lastIndexOf(","));
        		 
        		System.out.println(line);
        		
        		output.add(line);
        		 
        	}
        	else if(currentString.startsWith("o,sell"))
        	{
        		
        		int bbidIntex = GetBestBidIndex();
        		 
        		bestBid = bid.get(bbidIntex);
        		   		
        		String cutStr = null;
        		
        		int first, last;
        		
        		last = bestBid.indexOf(",bid");

        		cutStr = bestBid.substring(0, last);
        		
        		first = cutStr.lastIndexOf(",") + 1;
        		
        		int value = Integer.parseInt(cutStr.substring(first));
        		
        		decValue = Integer.parseInt(currentString.substring(currentString.lastIndexOf(",") + 1));	
        		
        		value = value - decValue;
        		
        		curStrbest_bid = bestBid.substring(0, first) + value + ",bid";
        		        		
        		bid.set(bbidIntex, curStrbest_bid);
        		
        	}
        	else if(currentString.startsWith("q,size,"))
        	{
        		String size = currentString.substring(currentString.lastIndexOf(",") + 1);
        		        		
        		Iterator<String> itr_bid = bid.iterator(); 
        		Iterator<String> itr_ask = ask.iterator();
        		String subStr = null; 
        		int parse_int = 0;
        		
        		 while (itr_bid.hasNext())
        	     {
        			    curStrbest_bid = itr_bid.next();
        			    subStr = curStrbest_bid.substring(2);
        	        	subStr = subStr.substring(0, subStr.indexOf(","));
        	        	parse_int = Integer.parseInt(subStr);
        	        	
        	        	if(parse_int == Integer.parseInt(size))
        	        	{
        	           		subStr = curStrbest_bid.substring(curStrbest_bid.indexOf(","));
        	           		subStr = subStr.substring(0, subStr.lastIndexOf(","));
        	           		subStr = subStr.substring(subStr.lastIndexOf(",") + 1);
        	           		System.out.println(subStr);
        	        		output.add(subStr);
        	        	}
        	        	
        	     
        	        		
        	     }
        		 
        		 while (itr_ask.hasNext())
        	     {
        			    curStrbest_ask = itr_ask.next();
        			    subStr = curStrbest_ask.substring(2);
        	        	subStr = subStr.substring(0, subStr.indexOf(","));
        	        	parse_int = Integer.parseInt(subStr);
        	        	
        	        	if(parse_int == Integer.parseInt(size))
        	        	{
        	           		subStr = curStrbest_ask.substring(curStrbest_ask.indexOf(","));
        	           		subStr = subStr.substring(0, subStr.lastIndexOf(","));
        	           		subStr = subStr.substring(subStr.lastIndexOf(",") + 1);
        	        		System.out.println(subStr);
        	        		output.add(subStr);
        	        	}
        	        	
        	     
        	        		
        	     }
        	
        		 
        		
        	}
        	else if(currentString.startsWith("o,buy"))
        	{
        		
        		int baskIntex = GetBestAskIndex();
        		
        		bestAsk = ask.get(baskIntex);
        		
        		String cutStr = null;
        		
        		int first, last;
        		
        		last = bestAsk.indexOf(",ask");
        		
        		cutStr = bestAsk.substring(0, last);
        		
        		first = cutStr.lastIndexOf(",") + 1;
        		
        		int value = Integer.parseInt(cutStr.substring(first));
        		
        		decValue = Integer.parseInt(currentString.substring(currentString.lastIndexOf(",") + 1));	
        		
        		value = value - decValue;
        
        		//System.out.println(bestAsk.substring(0, first) + value + ",bid");	
        		
        		curStrbest_ask = bestAsk.substring(0, first) + value + ",ask";
        		
        		ask.set(baskIntex, curStrbest_ask);
        		
        		
        		
        	}	
        	
          
        }

    }
    
    private static void WriteToFile() throws IOException
    {
    	FileWriter writer = null; 
		
		writer = new FileWriter(OUT_FILE_NAME);
				
		for(String str: output) {
    	 
			writer.write(str + System.lineSeparator());
			
		}
		
    
		writer.close();
		
    }

    		

}