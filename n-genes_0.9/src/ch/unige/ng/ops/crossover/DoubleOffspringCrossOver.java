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
import ch.unige.ng.species.GlobalIndividual;

/**
 * Common base for all the operator that process two Individuals
 * to make one new Individual.
 * <p/>
 * This class has to be extended by subclasses that crossover two elements.
 *
 * @author Jean-Luc Falcone
 * @version 17-sept-2004
 */
public abstract class DoubleOffspringCrossOver <T extends GlobalIndividual> implements Operator<T>, Crossover<T> {

    private final Group<T> selectionGroup;

    public DoubleOffspringCrossOver() {
        selectionGroup = new Group<T>();
    }

    public void operate(Population<T> pop, Selector<T> selector, Context context, int number) {
        int numTreated = 0;
        while (numTreated < number) {
            selector.select(pop, context, selectionGroup, 2);
            T mate1 = selectionGroup.remove(0);
            T mate2 = selectionGroup.remove(0);
            //The maximum number of individuals to work on is equal to number-numTreated
            if ((number - numTreated) > 1) {
                twoOffspringCrossover(mate1, mate2, pop, context);
                numTreated += 2;
            } else {
                oneOffspringCrossover(mate1, mate2, pop, context);
                numTreated++;
            }
        }
    }


    public void operate(Group<T> source, IGroup<T> destination, Selector<T> selector, Context context) {
        //TODO METHODE PEU PROPRE: REFLECHIR UN PEU PLUS
        int size = source.size();
        for (int i = 0; i < size; i += 2) {
            T mate1 = source.get(i);
            T mate2 = source.get((i + 1) % size);
            if (i == size - 1) {
                oneOffspringCrossover(mate1, mate2, destination, context);
            } else {
                twoOffspringCrossover(mate1, mate2, destination, context);
            }
        }
    }

    public T crossover(T mate1, T mate2, Context context) {
        oneOffspringCrossover(mate1, mate2, selectionGroup, context);
        T newInd = selectionGroup.remove(0);
        selectionGroup.clear();
        return newInd;
    }

    /**
     * This method crossover two individuals and creates two offsprings. These two offsprings have to be created put into
     * destination
     *
     * @param mate1       It is a reference of the individuals
     * @param mate2       It is a reference
     * @param destination The population in which new individuals will be put.
     * @param context
     */
    public abstract void twoOffspringCrossover(T mate1, T mate2, IGroup<T> destination, Context context);

    /**
     * This method crossover two individuals and creates one offspring the offspring is put into mate1
     *
     * @param mate1       It is a reference of the individuals, children has to be created
     * @param mate2       It is a reference
     * @param destination the population in which the new individual will be put
     * @param context
     */
    public abstract void oneOffspringCrossover(T mate1, T mate2, IGroup<T> destination, Context context);
}
