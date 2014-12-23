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


package ch.unige.ng.tools;

import java.util.StringTokenizer;

/**
 * This class parses an array given in a string separated with a given separator
 *
 * @author Jacques Fontignie
 * @version 21 juin 2005
 */
public class ArrayReader {

    /**
     *
     * @param string
     * @param separator
     * @return an array of doubles defined in string
     */
    public static double[] createDoubleArray(String string, String separator){
        StringTokenizer tokenizer = new StringTokenizer(string,separator);
        int size = tokenizer.countTokens();
        double array [] = new double[size];
        for (int i = 0; i < size; i++){
            array[i] = Double.valueOf(tokenizer.nextToken());
        }
        return array;
    }


        /**
     *
     * @param string
     * @param separator
     * @return an array of ints defined in string
     */
    public static int[] createIntArray(String string, String separator){
        StringTokenizer tokenizer = new StringTokenizer(string,separator);
        int size = tokenizer.countTokens();
        int array [] = new int[size];
        for (int i = 0; i < size; i++){
            array[i] = Integer.valueOf(tokenizer.nextToken());
        }
        return array;
    }

}
