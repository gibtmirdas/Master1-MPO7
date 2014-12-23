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


package ch.unige.ng.ops.crossover;

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.Operator;
import ch.unige.ng.ops.select.Selector;
import ch.unige.ng.populations.Group;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.populations.Population;
import ch.unige.ng.species.Individual;

/**
 * @author Jacques Fontignie
 * @version 14 mars 2005
 */
public abstract class SingleOffspringCrossover <T extends Individual> implements Operator<T>,Crossover<T> {
    private Group<T> selectionGroup;

    public SingleOffspringCrossover() {
        selectionGroup = new Group<T>();
    }


    public void operate(Population<T> pop, Selector<T> selector, Context context, int number) {
        int numTreated = 0;
        while (numTreated < number) {
            selector.select(pop, context, selectionGroup, 2);
            T mate1 = selectionGroup.remove(0);
            T mate2 = selectionGroup.remove(0);
            //The maximum number of individuals to work on is equal to number-numTreated
            crossover(mate1, mate2, pop, context);
            numTreated++;
        }
    }


    public void operate(Group<T> source, IGroup<T> destination, Selector<T> selector, Context context) {
        //TODO METHODE PEU PROPRE: REFLECHIR UN PEU PLUS
        int size = source.size();
        for (int i = 0; i < size; i++) {
            T mate1 = source.get(i);
            T mate2 = source.get((i + 1) % size);
            crossover(mate1, mate2, destination, context);
        }
    }

    public T crossover(T mate1, T mate2, Context context) {
        crossover(mate1, mate2, selectionGroup, context);
        T newInd = selectionGroup.remove(0);
        selectionGroup.clear();
        return newInd;
    }

    /**
     * This method crossover two individuals and creates two offsprings
     *
     * @param mate1   It is a reference of the individuals. 
     * @param mate2   It is a reference
     * @param pop     The population in which we have to put the individuals
     * @param context
     */
    public abstract void crossover(T mate1, T mate2, IGroup<T> pop, Context context);


}
