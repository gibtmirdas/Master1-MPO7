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


package ch.unige.ng.ops.mutation;

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.Operator;
import ch.unige.ng.ops.select.Selector;
import ch.unige.ng.populations.Group;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.populations.Population;
import ch.unige.ng.species.GlobalIndividual;

/**
 * Common base for all the operator that process an Individual
 * independently of the others.
 *
 * @author Jean-Luc Falcone
 * @version 17-sept-2004
 */
public abstract class Mutator <T extends GlobalIndividual> implements Operator<T> {
    private Group<T> selectionGroup;

    public Mutator() {
        selectionGroup = new Group<T>();
    }

    /**
     * This method process a single Individual <B>copy</B>
     * It is important not to forget to copy the Individual
     * before the mutation.
     *
     * @param ind Individual to process
     * @return mutated copy
     */
    public final T createMutated(T ind, Context context) {
        T newInd = (T) ind.copy();
        operate(newInd, context);
        return newInd;
    }

    /**
     * operates a mutation on the individual. This one has to be modified
     *
     * @param ind
     * @param context
     */
    public abstract void operate(T ind, Context context);

    /**
     * This method process a list Individual.
     * This Method choose a list of  Individuals with the selector from the first population
     * and add processed <B>copies</B> in the second.
     *
     * @param pop      the Population
     * @param selector selector
     * @param context  The current context
     * @param number   number of Individual to process
     */
    public void operate(Population<T> pop, Selector<T> selector, Context context, int number) {
        selector.select(pop, context, selectionGroup, number);
        for (T ind : selectionGroup) {
            pop.add(createMutated(ind, context));
        }
        selectionGroup.removeAll();
    }

    public void operate(Group<T> source, IGroup<T> destination, Selector<T> selector, Context context) {
        assert source != destination;
        while (!source.isEmpty()) {
            T ind = source.remove(0);
            operate(ind, context);
            destination.add(ind);
        }
    }

}
