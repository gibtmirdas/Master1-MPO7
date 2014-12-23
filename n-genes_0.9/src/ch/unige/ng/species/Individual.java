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
 * This interface represent a common base for each linear Individual.
 * The individual have to be either with a Gene prototype pattern
 * or with a Gene factory.
 * <p/>
 * It is not a good idea to create a new Individual using the new keyWord. You should use the method emptyCopy.
 * The goal is to handle the memory in an optimized way.
 * <p/>
 * This class should be used with the interface Poolable.
 *
 * @author Jean-Luc Falcone
 * @version An individual is no more iterable.
 * @see ch.unige.ng.tools.pool.Poolable
 */
public interface Individual<T> extends GlobalIndividual<T> {

    /**
     * @return the gene factory
     */
    GeneFactory<T> getGeneFactory();

    /**
     * Mutates the gene at position i. Most of times. It is a call to a geneFactory
     *
     * @param i
     */
    void mutate(int i);    

}
