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

package ch.unige.ng.fitness.cache;

import ch.unige.ng.species.Evaluable;
import ch.unige.ng.tools.ArrayIterator;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Fontignie Jacques
 * @version 28 juil. 2005
 */
class TemporaryEvaluable<T> implements Evaluable<T> {
    private final T array [];
    private final int size;
    private final int hashCode;

    public TemporaryEvaluable(Evaluable<T> source){
        size = source.size();
        //Unchecked cast, but impossible to avoid due to basckward compatibility
        array = (T[]) new Object[size];
        for (int i = 0; i < size; i++)
            array[i] = source.get(i);
        hashCode = Arrays.deepHashCode(array);
    }


    public T get(int i) {
        return array[i];
    }

    public int size() {
        return size;
    }

    public Iterator<T> iterator() {
        return ArrayIterator.getIterator(array);
    }

    @Override public boolean equals(Object obj) {
        if (obj instanceof Evaluable){
            Evaluable eval = (Evaluable) obj;
            if (eval.size() != size) return false;
            for (int i = 0; i < size; i++){
                if (!eval.get(i).equals(array[i]))
                    return false;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        return hashCode;
    }
}
