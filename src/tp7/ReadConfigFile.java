package tp7;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by thomas on 12/28/14.
 */
public abstract class ReadConfigFile {

    public static ArrayList<Boolean[]> read(String configFile){
        ArrayList<Boolean[]> lookupTable = new ArrayList<Boolean[]>();;
        int m,n=0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(configFile));

            try {
                String currentLine = reader.readLine();
                m = Integer.parseInt(currentLine.split(" ")[0]);
                n = Integer.parseInt(currentLine.split(" ")[1]);
                while((currentLine = reader.readLine()) != null){
                    if (currentLine.length() != m+1)
                        throw new InputMismatchException("Config file error");
                    lookupTable.add(parseLine(currentLine));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(lookupTable.size() != n)
            throw new InputMismatchException("Config file error");

        return lookupTable;
    }

    private static Boolean[] parseLine(String line){
        Boolean[] ret = new Boolean[line.length()];
        for(int i=0; i<line.length(); i++){
            ret[i] = line.charAt(i) == '1';
        }
        return ret;
    }
}
