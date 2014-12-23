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
import ch.unige.ng.tools.Forced;

/**
 * The k-tournament is a classical Selector, which draws at
 * random k Individuals from the OldPopulation and choose the one
 * whith the best fitness value.
 *
 * @author JL Falcone
 */
public class KTournamentSelector<T extends GlobalIndividual> implements Selector<T> {

    private int k;        //the size of the tournament

    /**
     * Construct the selector with an rng and the size of
     * the tournament.
     *
     * @param _k The size of the tournament
     */
    public KTournamentSelector(int _k) {
        k = _k;
    }

    /**
     * The empty constructor to be parser compliant
     */
    public KTournamentSelector() {

    }

    /**
     * This method is used to select the size of the tournament
     *
     * @param size
     */
    @Forced
            public void setSize(int size) {
        k = size;
    }

    /**
     * Gives the size of the Tournament
     *
     * @return k
     */
    public int getK() {
        return k;
    }

    public T select(IGroup<T> p, Context context) {
        int size = p.size();
        int indexBest = context.getRng().nextInt(size);
        T bestIndividual = p.get(indexBest);
        double bestFitness = bestIndividual.getFitness();
        for (int i = 0; i < k; i++) {
            int index = context.getRng().nextInt(size);
            T ind = p.get(index);
            double fit = ind.getFitness();
            if (fit < bestFitness) {
                bestFitness = fit;
                indexBest = index;
                bestIndividual = ind;
            }
        }
        return bestIndividual;
    }

    public void select(IGroup<T> p, Context context, IGroup<T> destination, int number) {
        assert number > 0;
        for (int i = 0; i < number; i++) {
            destination.add(select(p, context));
        }
    }

    public T select(T[] individuals, Context context) {
        int indexBest = context.getRng().nextInt(individuals.length);
        double bestFitness = individuals[indexBest].getFitness();
        for (int i = 0; i < k; i++) {
            int index = context.getRng().nextInt(individuals.length);
            T ind = individuals[index];
            double fit = ind.getFitness();
            if (fit < bestFitness) {
                bestFitness = fit;
                indexBest = index;
            }
        }
        return individuals[indexBest];
    }

 

}
