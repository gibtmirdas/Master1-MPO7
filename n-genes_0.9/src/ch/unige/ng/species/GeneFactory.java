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
 * A Genefactory determines the structure of an individual. An individual is a container and the gene factory
 * creates the genes that are put into. If you want to implement this class, you have to be aware of some points:
 * <ol>
 * <li> Two genes that are the same have to return the same hashCode, if this condition is not respected,
 * it is possible that no speedup can be generated with the CachingPopulation</li>
 * </ol>
 *
 * @author Jacques Fontignie
 * @version Each creation depend now on the position of the gene. We can now create some geneFactory that create a newgene
 *          depending on the position.
 */
public interface GeneFactory<T> extends GlobalGeneFactory<T> {

    /**
     * @return a new Gene randomly chosen that will be at position position. Some geneFactories can
     *         create a newGene depending on the position.
     */
    T newGene(int position);

    /**
     * The gene chosen at completely at Random
     * using a gaussian distribution.
     * This method does not make sense with any implementation, so the
     * should not implement it, when it's not appropriate.
     *
     * @param position the Posiition of the gene that has to be created
     * @param mean     the gaussian distribution average
     * @param std      the gaussian distribution standard deviation
     */
    T randomGaussianValue(int position, double mean, double std);

    /**
     * @param position the position at which the gene has to be modified. This is important because it is possible
     *                 that some mutation are depenedent of the position
     * @param gene
     * @return a gene different of gene randomly chosen
     */
    T differentValue(int position, T gene);

}
