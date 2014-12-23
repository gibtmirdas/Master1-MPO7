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


package ch.unige.ng.ops;

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.select.Selector;
import ch.unige.ng.populations.Group;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.populations.Population;
import ch.unige.ng.species.GlobalIndividual;

/**
 * This Interface describe the common features of all GP/GA operator.
 *
 * @author Jean-Luc Falcone
 * @version 16-dec-2004
 */
public interface Operator <T extends GlobalIndividual> {

    /**
     * This method process a list Individual.
     * This Method choose a list of  Individuals with the selector from the first population
     * and add processed <B>copies</B> in the second.
     *
     * @param pop      the Population
     * @param selector selector
     * @param number   number of Individual to process
     * @param context  The context of the operations. Some operators have to know a random generator number, the current generation, ...
     *                 All these values are put in this object
     */
    public void operate(Population<T> pop, Selector<T> selector, Context context, int number);


    /**
     * This method has to operate the source and put it into destination. Each individual has to be modified.
     * To be sure that there will be no memory leaks, you have to call the remove function. The number of individuals in the destination
     * has to be the same as in the destination.
     * <p/>
     * This function is quite equivalent to operate(Population,Selector,Context,number), but it is used if we want
     * by example to make a pipe. The output individuals are the inputs of the next Operator.
     * <p/>
     * It is really important that you do not self release the elements in the source, It can be in the default population.
     * <p/>
     * You should not create new individual. The idea is to transform the individuals that are in source. 
     *
     *
     * @param source      The population in which we want to pop elements. It is copies of the default population
     * @param destination The population in which the new individuals are put It is the new population.
     * @param selector    The selector used to select the individuals
     * @param context     The context.
     */
    public void operate(Group<T> source, IGroup<T> destination, Selector<T> selector, Context context);

}
