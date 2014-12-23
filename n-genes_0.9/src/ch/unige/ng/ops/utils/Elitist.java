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

package ch.unige.ng.ops.utils;

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.guided.WorstBiggerSelector;
import ch.unige.ng.ops.select.Selector;
import ch.unige.ng.populations.Group;
import ch.unige.ng.populations.Population;
import ch.unige.ng.species.VariableSizeIndividual;
import ch.unige.ng.timer.GeneticListener;
import ch.unige.ng.tools.Forced;

import java.lang.reflect.Array;

/**
 * This class takes the original population and remove the worst bigger and add a selected indiviudal from the best
 *
 * @author Fontignie Jacques
 * @version 1 juil. 2005
 */
public class Elitist <T extends VariableSizeIndividual> implements GeneticListener<T> {
    private int size;
    private WorstBiggerSelector<T> biggerSelector;
    private T worst[];
    private Group<T> best;

    public Elitist() {
        biggerSelector = new WorstBiggerSelector<T>(0.1);
        best = new Group<T>();
    }

    public Elitist(int size) {
        this();
        setSize(size);
    }

    /**
     * The number of best individuals to insert.
     * @param size
     */
    @Forced public void setSize(int size) {
        this.size = size;
        worst = (T[]) Array.newInstance(VariableSizeIndividual.class,size);
    }


    public void fireEvent(Population<T> population, Selector<T> selector, Context context) {
        biggerSelector.select(population, context, worst, size, null);
        selector.select(population, context, best, size);
        for (int i = 0, length = population.size(); i < length; i++) {
            T ind = population.get(i);
            boolean canInsert = true;
            for (int j = 0; j < size; j++) {
                if (ind.equals(worst[j])) {
                    worst[j] = null;
                    canInsert = false;
                    break;
                }
            }
            //If it is possible to insert, we insert a copy
            if (canInsert)
                population.add((T) ind.copy());
            else
            //Else we insert a better individual.
                population.add((T) best.remove(0).copy());
        }

        population.commit();
        System.out.println(population.size());
    }
}
