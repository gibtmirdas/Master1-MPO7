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

import java.io.*;
import java.util.Random;

/**
 * This class writes a new lookup table into a file for the nk-landscape problem
 *
 * @author Jacques Fontignie
 * @version 3 mai 2005
 */
public class NKlandscapeWriter {

    public static void main(String[] args) throws IOException {
        if (args.length != 3){
            System.err.println("Number of inputs must be equal to 2. The first is the file in which the file will be written, " +
                    "the second is the n the third is k");
            System.exit(0);
        }
        String fileName = args[0];
        int n = Integer.valueOf(args[1]);
        int k = Integer.valueOf(args[2]);
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fileName));
        stream.writeInt(k);
        Random random = new Random(0);
        int neighbours [] = new int[n*(k-1)];
        for (int i = 0; i < neighbours.length; i++)
            neighbours[i] = random.nextInt(n);
        stream.writeObject(neighbours);
        int size = 1 << k;
        boolean lut [] = new boolean[size];
        for (int i = 0; i < size; i++)
            lut[i] = random.nextBoolean();
        stream.writeObject(lut);
        stream.close();
    }
}
