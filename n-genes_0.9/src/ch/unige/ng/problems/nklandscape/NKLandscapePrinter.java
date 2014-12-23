/*************************************************************************
*
* Copyright (C) 2005 University of Geneva, Switzerland.
*
* This file is part of n-genes
*
* n-genes is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
* 
* n-genes is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with n-genes; if not, write to the Free Software
* Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301  USA
*
* http://cui.unige.ch/spc/tools/n-genes
* n-genes@cui.unige.ch
*
******************************************************************************/


package ch.unige.ng.problems.nklandscape;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

/**
 * @author Jacques Fontignie
 * @version 3 mai 2005
 */
public class NKLandscapePrinter {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 2) {
            System.err.println("You have to give two filename: the first design the config file and the second, " +
                    "the file in which put the dot file");
            System.exit(0);
        }
        String inputName = args[0];
        String outputName = args[1];
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(inputName));
        PrintStream output = new PrintStream(outputName);
        int k = stream.readInt();
        int[] neighbours = (int[]) stream.readObject();
        int length = neighbours.length / k;
        output.println("graph graphic{");
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < k - 1; j++)
                output.println(i + " -- " + (neighbours[i * (k - 1) + j] % length) + ";");
        }
        output.println("}");
        output.close();

    }
}
