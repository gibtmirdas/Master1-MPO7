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

import java.util.Arrays;

import static java.lang.Math.sqrt;
import static java.lang.Math.min;

/**
 * The goal of this class is to go a little bit faster than ProbabilityMutation. It is fast the same.
 * You give a probability and the algorithm mutate each gene with a certain probability.
 * But the algorithm is like this.
 * <p/>
 * <ol>
 * <li> We calculate the number of genes to be modified= probability*sizeIndividual</li>
 * <li> We select randomly a gene not allready mutated that will be mutated. </li>
 * </ol>
 * <p/>
 * It goes faster because the expected number of to the random generator is equal to: probability*(1-probability)sizeIndividual +1
 * versus: sizeIndividual in the default algorithm.
 * <p/>
 * We can consider this algorithm is faster than the default if the probability is small.
 * <p/>
 * It is not exactly the same if the individual size is small.
 *
 * @author Jacques Fontignie
 * @version 8 mars 2005
 * @see ProbabilityMutation
 */
public class FastProbabilityMutation <T extends Individual> extends Mutator<T> {
    private double probability;
    private boolean array [];

    public FastProbabilityMutation(){
        array = new boolean[0];
    }

    public FastProbabilityMutation(double probability){
        this();
        setProbability(probability);
    }

    @Forced public void setProbability(double probability) {
        this.probability = probability;
        if (probability <= 0 || probability >= 1)
            throw new RuntimeException("The probability has to be between 0 and 1 (not incl.)");
    }

    public void operate(T newInd, Context context) {
        RNG rng = context.getRng();
        int size = newInd.size();
        if (array.length < size)
            array = new boolean[size];
        Arrays.fill(array, false);
        double mean = size * probability;
        double sigma = sqrt(mean * (1 - probability));
        int toMutate = (int) min(rng.nextGaussian() * sigma + mean, size);
        for (int i = 0; i < toMutate; i++) {
            int value = rng.nextInt(size);
            if (array[value])
                i--;
            else {
                newInd.mutate(rng.nextInt(size));
                array[value] = true;
            }
        }
    }
}
