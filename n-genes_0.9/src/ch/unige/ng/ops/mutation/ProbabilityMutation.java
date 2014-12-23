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
import ch.unige.ng.tools.rng.RNG;
import ch.unige.ng.ops.mutation.Mutator;
import ch.unige.ng.gen.Context;

/**
 * This class is used to transform each gene with a certain probability.
 *
 * @author Jacques Fontignie
 * @version 8 mars 2005
 */
public class ProbabilityMutation <T extends Individual> extends Mutator<T> {

    private double probability;

    public ProbabilityMutation(){
        
    }

    public ProbabilityMutation(double probability){
        setProbability(probability);
    }

    @Forced public void setProbability(double probability) {
        this.probability = probability;
        if (probability <= 0 || probability >= 1)
            throw new RuntimeException("The probability has to be between 0 and 1 (not incl.)");
    }

    public void operate(T newInd, Context context) {
        RNG rng = context.getRng();
        for (int i = newInd.size() - 1; i >= 0; i--)
            if (rng.nextBoolean(probability))
                newInd.mutate(i);
    }
}
