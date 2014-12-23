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

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: falcone
 * Date: Aug 23, 2004
 * Time: 5:04:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArraysFormatter {

    public static String dump(boolean[] arr) {        
        StringBuffer sb = new StringBuffer("[ ");
        for(boolean b: arr) {
            if (b) {
                sb.append("1 ");
            } else {
                sb.append("0 ");
            }
        }
        sb.append("]");
        return sb.toString();
    }


    public static String dump(Object[] arr) {
        StringBuffer sb = new StringBuffer("[ ");
        for(Object o: arr) {
            sb.append(o);
            sb. append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    public static String dump(Collection<?> collection) {
        StringBuffer sb = new StringBuffer("[ ");
        for(Object o: collection) {
            sb.append(o);
            sb. append(" ");
        }
        sb.append("]");
        return sb.toString();
    }


    public static String dump(double[] arr) {
       StringBuffer sb = new StringBuffer("[ ");
        for(double d: arr) {
            sb.append(d);
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    public static String dump(int[] arr) {
       StringBuffer sb = new StringBuffer("[ ");
        for(double d: arr) {
            sb.append(d);
            sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}
