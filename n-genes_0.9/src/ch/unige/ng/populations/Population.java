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


package ch.unige.ng.populations;

import ch.unige.ng.species.GlobalIndividual;

import java.io.PrintWriter;

/**
 * @author Jacques Fontignie
 * @version 5 avr. 2005
 */
public interface Population<T extends GlobalIndividual> extends IGroup<T> {
    /**
     * Initialize and populate the population. The prototype described above will be used to
     * create the new Individuals.
     *
     * @param size the population size
     * @param prototype the prototype used
     */
    public void initialize(int size, T prototype);


    /**
     * @return the creatingPopulation size
     */
    public int nextSize();

    /**
     * This method is equivalent to the nextStep, but this does not change the value step.
     * The idea is that it is possible to make some work on the population, like cleaning
     * the population, or applying an hill climbing
     */
    public void commit();


    /**
     * Increments the iteration number and commits all changes to the population
     *
     * @return the new iteration number
     */
    public int nextStep();

    /**
     * @return the current iteration number
     */
    public int getStep();

    /**
     * @return the associated statistic cache
     */
    public StatisticCache<T> getStatisticCache();


    /**
     * Format the population as String and send it to a PrintWriter.
     * The toString() method of the Individuals is called and the fitness is added.
     *
     * @param pw
     */
    public void dump(PrintWriter pw);    
}
