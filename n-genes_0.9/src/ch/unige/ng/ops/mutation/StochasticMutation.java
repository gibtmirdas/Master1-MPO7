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
import ch.unige.ng.species.Individual;
import ch.unige.ng.tools.Forced;

/**
 * Mutate an individual with a certain rate.
 *
 * @author Jacques Fontignie
 * @version 19 avr. 2005
 */
public class StochasticMutation <T extends Individual> extends Mutator<T> {

    private double mutRate;
    private boolean muteAll;
    private Mutator<T> mutator;

    public StochasticMutation() {
    }

    /**
     * The mutation rate of the algorithm.  Each individual has a certain probability to be mutated
     *
     * @param muteRate
     * @param mutator  The mutator that is used
     */
    public StochasticMutation(double muteRate, Mutator<T> mutator) {
        this();
        setMuteRate(muteRate);
        setMutator(mutator);        
    }

    @Forced public void setMuteRate(double mutRate) {
        this.mutRate = mutRate;
        if (mutRate == 1.) muteAll = true;
    }

    @Forced public void setMutator(Mutator<T> mutator) {
        this.mutator = mutator;
    }

    public void operate(T t, Context context) {
        if (muteAll)
            mutator.operate(t, context);
        else if (context.getRng().nextBoolean(mutRate))
            mutator.operate(t, context);
    }
}
