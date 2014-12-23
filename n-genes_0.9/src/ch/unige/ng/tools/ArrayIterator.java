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

import java.util.Iterator;

//TODO perhaps we can remove elements too. This can make a problem with genomes.

/**
 * This class reprents an Iterator to an array. It contains a factory to construct the iterator.
 * It cannot be used to remove elements
 * @author Jean-Luc Falcone
 * @version May 27, 2005 - 2:52:10 PM
 */
public class ArrayIterator<T> implements Iterator<T>{

    private T[] array;
    private int index;

    private ArrayIterator(T[] _array) {
        array = _array;
        index = -1;
    }

    /**
     * This factory method wraps the array in a iterator.
     * @param array
     * @return An iterator to the array.
     */

    public static <E> ArrayIterator<E> getIterator(E[] array) {
        return new ArrayIterator<E>(array);
    }

    public boolean hasNext() {
        return (index < array.length-1);
    }

    public T next() {
        index++;
        return array[index];
    }

    public void remove() {
        throw  new IllegalStateException("This method cannot be used on an array iterator");
    }
}
