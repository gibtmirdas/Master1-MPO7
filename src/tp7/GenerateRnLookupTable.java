package tp7;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GenerateRnLookupTable {

    public static void main(String[] args) {
		int length = 10;
		int nbRows = Integer.parseInt(args[0]);
		String filename = "test_table.txt";
		if(nbRows == 400)
			filename = "learn_table.txt";
		filename = "src"+ File.separator +"lookup"+ File.separator + filename;
		System.out.println("Generate Lookup Table "+filename+" with " + args[0] +" rows");

		Random rand = new Random();
		ArrayList<Boolean> tmp;
        try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write("10 "+args[0]+"\n");
			String toWrite;
			for(int i=0; i < nbRows; i++){
				tmp = randomArray(rand, length);
				tmp.add(computeFonction(tmp));
				toWrite = arrayToString(tmp);
				if (i == nbRows-1)
					toWrite = toWrite.substring(0,toWrite.length()-1);
				writer.write(toWrite);
			}
			writer.flush();
			writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private static boolean computeFonction(ArrayList<Boolean> bool){
		return ((bool.get(0) | bool.get(1))
				& !(bool.get(2) ^ bool.get(3)))
				|
				((!bool.get(4) | bool.get(0))
						& (bool.get(5) ^ bool.get(6))
						| (bool.get(7) ^ bool.get(8))
						& (bool.get(9) ^ bool.get(1))) ;
	}

	private static ArrayList<Boolean> randomArray(Random rand, int length){
		ArrayList<Boolean> ret = new ArrayList<Boolean>();
		for (int i=0; i<length; i++)
			ret.add(rand.nextBoolean());
		return ret;
	}

	private static String arrayToString(ArrayList<Boolean> tmp){
		String str = "";
		for(Boolean b: tmp)
			str += b?"1":"0";
		return str+"\n";
	}

	public static boolean testForumla(Boolean[] booleans){
		ArrayList<Boolean> ret = new ArrayList<Boolean>();
		for(boolean b: booleans)
			ret.add(b);
		return computeFonction(ret);
	}
}
