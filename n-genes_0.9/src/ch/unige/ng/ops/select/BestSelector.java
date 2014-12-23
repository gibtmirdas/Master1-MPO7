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
 * An elitist Selector which choose the Individuals
 * according to their fitness value.
 *
 * @author JL Falcone
 */
public class BestSelector<T extends GlobalIndividual> implements Selector<T>{

    public T select(IGroup<T> p) {
        T bestInd = null;
        double bestFitness = Double.MAX_VALUE;
        for (T ind : p) {
            double fit = ind.getFitness();
            if (fit < bestFitness) {
                bestInd = ind;
                bestFitness = fit;
            }
        }
        return bestInd;
    }


    public T select(IGroup<T> p, Context context) {
        return select(p);
    }

    public void select(IGroup<T> population, Context context, IGroup<T> destination, int number) {
        //To change body of implemented methods use File | Settings | File Templates.
        population.sort();
        for (int i = 0; i < number; i++)
            destination.add(population.get(0));
    }

    public T select(T[] individuals, Context context) {
        T best = null;
        double bestFitness = Double.MAX_VALUE;
        for (T ind : individuals) {
            if (ind.getFitness() < bestFitness) {
                bestFitness = ind.getFitness();
                best = ind;
            }
        }
        return best;
    }
}
