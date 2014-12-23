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

/**
 * @author Fontignie Jacques
 * @version 6 aug. 2005
 */
public interface Permutable<T> extends GlobalIndividual<T> {

    /**
     * This method shuffles the individual from startIndex to endIndex.
     * @param startIndex
     * @param endIndex
     * @param context
     */
    public void shuffle(int startIndex, int endIndex, Context context);


    /**
     * Invert the subsequences from startIndex to endIndex
     * @param startIndex
     * @param endIndex
     */
    public void invert(int startIndex, int endIndex);


    /**
     * Swap two values at index1 and index2
     * @param index1
     * @param index2
     */
    public void swap(int index1, int index2);


    /**
     *
     * @return if the individual is really consistent
     */
    public boolean checkConsistency();


    /**
     * If the individual is not consistent, this method can be called to automatically repair the genome.
     * To make it become a permutation
     */
    public void repair();


    /**
     * @return the gene factory
     */
    PermutableGeneFactory<T> getGeneFactory();
}
