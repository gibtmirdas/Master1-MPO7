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

import ch.unige.ng.tools.pool.Copiable;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.fitness.Fitness;

/**
 * @author Fontignie Jacques
 * @version 6 aug. 2005
 */
public interface GlobalIndividual<T> extends Copiable<GlobalIndividual<T>>, Evaluable<T>, Iterable<T> {

    /**
     * Sets the fitness function used by the individual
     *
     * @param fitness
     */
    @Forced
    void setFitness(Fitness<T> fitness);

    /**
     * Compute and return a fitness value.
     * Use the fitness function associated with the Individual
     *
     * @return the fitness value.
     */
    double getFitness();

    /**
     * @return the fitness function used
     */
    public Fitness<T> getFitnessFunction();


    /**
     * This function makes a copy of genes between individuals
     *
     * @param beginSrc  the index in the soruce
     * @param length    the length of genes that have to be copied
     * @param src       the individual in which values are copied
     * @param beginDest the index in the destination
     */
    void copy(int beginSrc, int length, GlobalIndividual<T> src, int beginDest);

    /**
     * Set a new Gene at position <i>i</i>.
     *
     * @param newGene  the new Gene
     * @param position the insertion position.
     */
    void set(T newGene, int position);


    /**
     * By default individuals have to be empty. When applying the randomize method, individuals fill themself.
     * If they are not empty then the result will be that the individual will be randomized.
     */
    void randomize();

    /**
     * @param o
     * @return if o is equals with the current object
     */
    @Override
    public boolean equals(Object o);

    /**
     * @return the hashCode of the object
     */
    public int hashCode();

    /**
     * @return if the hashcode and the fitness are allready cached.
     */
    boolean isUpToDate();

    /**
     * clear the genome.
     */
    void clear();

    /**
     * This function is used to inform the class that it is no more used. The object hsa to check that it
     * has not allready been released.
     *
     * @see ch.unige.ng.tools.pool.Poolable
     */
    void selfRelease();

    /**
     * @return the gene factory
     */
    GlobalGeneFactory<T> getGeneFactory();

}
