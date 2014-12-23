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


package ch.unige.ng.ops.guided;

import ch.unige.ng.gen.Context;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.species.Individual;

/**
 * The goal of this class is to work like a selector, but the class has an argument in input, to know how to select
 * next individuals
 *
 * @author Jacques Fontignie
 * @version 28 avr. 2005
 */
public interface GuidedSelector<T extends Individual> {

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
     * @param evaluator
     */
    public void select(IGroup<T> p, Context context, T [] destination, int number, SelectionEvaluator<T> evaluator);
}
