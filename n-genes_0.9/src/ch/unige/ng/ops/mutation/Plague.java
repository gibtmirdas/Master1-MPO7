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
import ch.unige.ng.species.GlobalIndividual;
import ch.unige.ng.tools.Forced;

/**
 * This <b>dirty</b> class tries to reduce the size of an Individual
 * by trying successive mutations which do not diminish
 * the fitness of the individual.
 *
 * @version 2
 *          This method is now a mutator
 */
public class  Plague <S,T extends GlobalIndividual<S>> extends Mutator<T> {

    private Mutator<T> operator;
    private int limit;

    private boolean muteAll;
    private double muteRate;

    /**
     * @param operator    The operator that has be used to make the plague
     * @param limit       the number of attempts to try to increase the fitness
     * @param probability the probability of an individual to be plagued
     */
    public Plague(Mutator<T> operator, int limit, double probability) {
        this();
        setOperator(operator);
        setLimit(limit);
        setProbability(probability);
    }

    /**
     * Basic void constructor.
     */
    public Plague() {
        setProbability(0.1);
    }


    @Forced public void setProbability(double mutRate) {
        if (mutRate > 1.0 || mutRate < 0) {
            throw new IllegalArgumentException("mutRate has to be from 0 to 1. You gave me: " + mutRate);
        }
        if (mutRate == 1.0) {
            muteRate = 1.0;
            muteAll = true;
        } else {
            muteRate = mutRate;
            muteAll = false;
        }
    }


    @Forced public void setOperator(Mutator<T> _operator) {
        operator = _operator;
    }

    @Forced public void setLimit(int _limit) {
        limit = _limit;
    }

    public void operate(T t, Context context) {
        if (muteAll || context.getRng().nextBoolean(muteRate)) {
            T reduced = reduce(operator, t, context, limit);
            t.copy(reduced);
            reduced.selfRelease();
        }

    }

    /**
     * Reduce an Individual to a smaller size.
     *
     * @param op    The Mutations which will be used
     * @param ind   The Individual to be reduced.
     * @param limit The maximum number of attempt
     * @return the reduced Individual
     */
    public T reduce(Mutator<T> op, T ind, Context context, int limit) {
        int t = 0;
        T shortest = (T) ind.copy();
        int shortestSize = shortest.size();
        double fitness = ind.getFitness();
        for (t = 0; t < limit; t++) {
            T newInd = op.createMutated(shortest, context);
            if (newInd.getFitness() <= fitness && newInd.size() < shortestSize) {
                shortest.selfRelease();
                shortest = (T) newInd;
                shortestSize = shortest.size();
                fitness = shortest.getFitness();
            } else
            //We have to release the individual
                newInd.selfRelease();
        }        
        return shortest;

    }


}
