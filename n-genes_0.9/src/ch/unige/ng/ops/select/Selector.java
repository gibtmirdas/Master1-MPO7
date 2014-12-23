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


package ch.unige.ng.ops.select;

import ch.unige.ng.gen.Context;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.species.GlobalIndividual;

/**
 * The selectors are important component of.ng. They return
 * Individual(s) of the population based on certain rules. The
 * Selectors do not copy the return Individual, so an implementer
 * of operators must take care of defensive copying.
 *
 * @author JL Falcone
 * @version The selector returns no more an array, but an array is given in parameter.
 */
public interface Selector <T extends GlobalIndividual> {

    /**
     * Select an Individual from a population.
     * <p/>
     * <strong> Very important</strong> The function has to return the individual itself.. Classes using it have
     * to create copies
     *
     * @param p       the population.
     * @param context the context of the population
     * @return the selected Individual
     */
    public T select(IGroup<T> p, Context context);

    /**
     * Select a number of Individual from a OldPopulation
     * <p/>
     * <strong> Very important</strong>The function has to return the individual itself.. Classes using it have
     * to create copies
     *
     * @param p        the population.
     * @param context  The context of the population
     * @param destination is the population, in which selected individuals will not be put. These elements have to be
     *                 refereneces of p
     * @param number   The number of individuals we want to select
     */
    public void select(IGroup<T> p, Context context, IGroup<T> destination, int number);

    /**
     * This method is called to select an individual in the iterable.
     *
     * @param array
     * @param context
     */
    public T select(T[] array, Context context);

}
