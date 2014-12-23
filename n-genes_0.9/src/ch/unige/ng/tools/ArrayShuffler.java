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

import ch.unige.ng.tools.rng.RNG;

/**
 * @author Fontignie Jacques
 * @version 6 aug. 2005
 */
public class ArrayShuffler {
    private static final int SHUFFLE_THRESHOLD = 5;

    public static final <T> void shuffle(T array[], RNG rng) {
        int size = array.length;
        if (size < SHUFFLE_THRESHOLD) {
            for (int i = size; i > 1; i--)
                swap(array, i - 1, rng.nextInt(i));
        } else {
            // Shuffle array
            for (int i = size; i > 1; i--)
                swap(array, i - 1, rng.nextInt(i));
        }
    }

    public static final <T> void shuffle(T array[], int startIndex, int endIndex, RNG rng) {
        int size = endIndex - startIndex;
        if (size < SHUFFLE_THRESHOLD) {
            for (int i = size; i > 1; i--)
                swap(array, i - 1 + startIndex, rng.nextInt(i) + startIndex);
        } else {
            // Shuffle array
            for (int i = size; i > 1; i--)
                swap(array, i - 1 + startIndex, startIndex + rng.nextInt(i));
        }
    }

    private static <T> void swap(T array[], int index1, int index2) {
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }
}
