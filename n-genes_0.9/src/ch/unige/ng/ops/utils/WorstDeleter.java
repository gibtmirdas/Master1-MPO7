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
import ch.unige.ng.ops.select.Selector;
import ch.unige.ng.populations.Population;
import ch.unige.ng.species.Individual;
import ch.unige.ng.timer.GeneticListener;
import ch.unige.ng.tools.Forced;

/**
 * Suprress a certain numebr of worst individual and put some that have a better fitness.
 *
 * @author Jacques Fontignie
 * @version 1 juin 2005
 */
public class WorstDeleter <T extends Individual> implements GeneticListener<T> {

    private int numToDelete;

    public WorstDeleter() {

    }

    public WorstDeleter(int numToDelete){
        setNumToDelete(numToDelete);
    }

    @Forced public void setNumToDelete(int numToDelete) {
        this.numToDelete = numToDelete;
    }

    public void fireEvent(Population<T> population, Selector<T> selector, Context context) {
        population.sort();
        //Add the best
        for (int i = 0, length = population.size() - numToDelete; i < length; i++)
            population.add((T) population.get(i).copy());
        //Add some rselected individuals
        for (int i = 0; i < numToDelete; i++)
            population.add((T) selector.select(population, context).copy());
        population.commit();
    }
}
