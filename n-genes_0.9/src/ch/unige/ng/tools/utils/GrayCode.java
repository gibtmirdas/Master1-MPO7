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

import ch.unige.ng.species.Evaluable;


/**
 * This class gives some method that permit to go from binary code to gray code or from gray to binary...
 *
 * @author Jacques Fontignie
 * @version 14 avr. 2005
 */
public class GrayCode {

    /**
     * Transforms the gray value that is defined from array[begin] to array[begin+length-1] into the associated
     * int value
     *
     * @param array  the array
     * @param begin  the begin index of the code
     * @param length the length of the array
     * @return the int value of the substring array[begin:begin+length]
     */
    public static int grayToInt(boolean array [], int begin, int length) {

        int result = array[begin++] ? 1 : 0;
        int last = result;
        for (int i = length - 2; i >= 0; i--) {
            last ^= (array[begin++]) ? 1 : 0;
            result = (result << 1) + last;
        }
        return result;
    }

    /**
     * Transforms the gray value that is defined from array[begin] to array[begin+length-1] into the associated
     * int value
     *
     * @param evaluable contains the booleans
     * @param begin     the begin index of the code
     * @param length    the length of the array
     * @return the int value of the substring evaluable[begin:begin+length]
     */
    public static int grayToInt(Evaluable<Boolean> evaluable, int begin, int length) {

        int result = evaluable.get(begin++).booleanValue() ? 1 : 0;
        int last = result;
        for (int i = length - 2; i >= 0; i--) {
            last ^= evaluable.get(begin++).booleanValue() ? 1 : 0;
            result = (result << 1) + last;
        }
        return result;
    }
}
