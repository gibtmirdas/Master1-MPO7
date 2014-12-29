package tp7;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateRnLookupTable {

    public static void main(String[] args) {
        System.out.println("Generate Lookup Table:" + args[0]);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(args[0]));



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
