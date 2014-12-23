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
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.species.Individual;
import ch.unige.ng.tools.rng.RNG;

/**
 * This crossover works on each gene.
 *
 * @author Jacques Fontignie
 * @version 4 mai 2005
 */
public class UniformCrossover <S,T extends Individual<S>> extends DoubleOffspringCrossOver<T> {


    public void twoOffspringCrossover(T mate1, T mate2, IGroup<T> population, Context context) {
        int indSize = mate1.size();
        if (indSize != mate2.size()) {
            throw new IllegalArgumentException("The two mates have different sizes... " + indSize + " vs. " + mate2.size());
        }
        RNG rng = context.getRng();
        T offspring1 = (T) mate1.emptyCopy();
        T offspring2 = (T) mate2.emptyCopy();
        for (int i = 0; i < indSize; i++) {
            if (rng.nextBoolean()) {
                offspring1.set(mate1.get(i), i);
                offspring2.set(mate2.get(i), i);
            } else {
                offspring2.set(mate1.get(i), i);
                offspring1.set(mate2.get(i), i);
            }
        }
        population.add(offspring1);
        population.add(offspring2);
    }

    public void oneOffspringCrossover(T mate1, T mate2, IGroup<T> population, Context context) {
        int indSize = mate1.size();
        if (indSize != mate2.size()) {
            throw new IllegalArgumentException("The two mates have different sizes... " + indSize + " vs. " + mate2.size());
        }
        RNG rng = context.getRng();
        T offspring1 = (T) mate1.emptyCopy();
        for (int i = 0; i < indSize; i++) {
            if (rng.nextBoolean()) {
                offspring1.set(mate1.get(i), i);                
            } else {
                offspring1.set(mate2.get(i), i);
            }
        }
        population.add(offspring1);
    }
}
