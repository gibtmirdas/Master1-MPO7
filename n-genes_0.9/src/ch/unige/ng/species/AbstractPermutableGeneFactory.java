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

package ch.unige.ng.species;

import ch.unige.ng.gen.Context;
import ch.unige.ng.tools.ArrayShuffler;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author Fontignie Jacques
 * @version 6 aug. 2005
 */
public abstract class AbstractPermutableGeneFactory<T> implements PermutableGeneFactory<T> {

    private T genes [];
    private T temporary [];
    private int size;
    private Context context;

    public AbstractPermutableGeneFactory() {
    }


    public AbstractPermutableGeneFactory(Context context) {
        setContext(context);
    }

    public void setContext(Context _ctx) {
        this.context = _ctx;
    }

    protected void setGenes(List<T> list) {
        size = list.size();
        genes = (T[]) Array.newInstance(getClassGene(), size);
        temporary = (T[]) Array.newInstance(getClassGene(), size);
        for (int i = 0; i < size; i++)
            genes[i] = list.get(i);
        sort(genes);
    }

    public T[] getAvailableGenes() {
        return genes;
    }

    public int getSize() {
        return size;
    }

    public void shuffleGenes(T array []) {
        System.arraycopy(genes, 0, array, 0, size);
        ArrayShuffler.shuffle(array, context.getRng());
    }

    public boolean checkConsistency(Evaluable<T> evaluable) {
        final int length = evaluable.size();
        for (int i = 0; i < length; i++)
            temporary[i] = evaluable.get(i);
        sort(temporary);
        for (int i = 0; i < length; i++) {
            if (!temporary[i].equals(genes[i]))
                return false;
        }
        return true;
    }

    /**
     * Sort the array in an order.
     *
     * @param temporary
     */
    protected abstract void sort(T[] temporary);

}
