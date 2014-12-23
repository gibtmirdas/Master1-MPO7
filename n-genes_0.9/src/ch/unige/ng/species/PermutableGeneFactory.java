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

/**
 * @author Fontignie Jacques
 * @version 6 aug. 2005
 */
public interface PermutableGeneFactory<T> extends GlobalGeneFactory<T> {

    /**
     *
     * @return the available genes
     */
    T[] getAvailableGenes();

    /**
     *
     * @return the number of available genes
     */
    int getSize();

    /**

     *
     * The array has to be the good length, this is the only condition. It will insert the genes into a random
     * order.
     */
    void shuffleGenes(T [] array);

    /**
     *
     * @param evaluable
     * @return if the genome is consistent: each gene appears only one time.
     */
    boolean checkConsistency(Evaluable<T> evaluable);
}
