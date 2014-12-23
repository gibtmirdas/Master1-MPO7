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
 * This interface allows to create variable sizte individuals.
 *
 * @author Jean-Luc Falcone
 * @version 17-sept-2004
 */
public interface VariableSize <T> extends Individual<T> {

    /**
     * Deletes a gene in the genome.
     *
     * @param position the position of the gene to be deleted
     */
    void delete(int position);

    /**
     * Inserts a gene at a given position.
     *
     * @param position the insertion position.
     * @param g        the gene to be inserted
     */
    void insert(int position, T g);

    /**
     * Insert an individual at a given position
     *
     * @param position      the instetion position
     * @param varIndividual the Individual to be inserted.
     */
    void insert(int position, VariableSize<T> varIndividual);

    /**
     * Add a gene at the end of the genome.
     *
     * @param g the Gene to be added
     */
    void insert(T g);


    /**
     * Removes all genes of the genome.
     */
    void clear();

    /**
     * Delete a contigous region of the genome. The start is inclusive and the end is exclusive.
     *
     * @param start the inclusive position of the first gene to be deleted
     * @param end   the position of the last gene. The last gene will not be deleted
     */
    void delete(int start, int end);

    /**
     * Create a new individual from a contiguous region of the genome.
     *
     * @param start the position of the subindividual first gene
     * @param end   the position of the last index, the last index, will not be taken
     * @return the SubIndividual
     */
    VariableSize<T> getSubIndividual(int start, int end);

    /**
     * @return The maximal size for the individual.
     */
    int getMaxSize();
}
