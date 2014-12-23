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


package ch.unige.ng.tools.utils;

import java.util.Arrays;
import java.util.Comparator;


/**
 * Static class calculating the median value of an array.
 * <strong> Warning this class is not synchronized </strong>
 *
 * //TODO MODIFIER CETTE CLASSE DE MANIERE A NE PAS AVOIR A FAIRE UN QUICKSORT
 *
 * @author Jacques Fontignie
 * @version 27 mai 2005
 */
public class Median {
    private static int[] intArray = new int[0];
    private static double[] doubleArray = new double[0];
    private static Comparator[] comparatorArray = new Comparator[0];

    /**
     * @param array
     * @return the median value in the array.
     */
    public static int median(int[] array) {

        int size = array.length;
        if (intArray.length < size)
            intArray = new int[size];
        System.arraycopy(array, 0, intArray, 0, size);
        Arrays.sort(intArray, 0, array.length);
        return intArray[size >> 1];
    }

    public static double median(double[] array) {
        int size = array.length;
        if (doubleArray.length < size)
            doubleArray = new double[size];
        System.arraycopy(array, 0, doubleArray, 0, size);
        Arrays.sort(doubleArray, 0, array.length);
        return doubleArray[size >> 1];
    }

    public static <T extends Comparator> T median(T[] array) {
        int size = array.length;
        if (comparatorArray.length < size)
            comparatorArray = new Comparator[size];
        System.arraycopy(array, 0, comparatorArray, 0, size);
        Arrays.sort(comparatorArray, 0, array.length);
        return (T) comparatorArray[size >> 1];
    }
}
