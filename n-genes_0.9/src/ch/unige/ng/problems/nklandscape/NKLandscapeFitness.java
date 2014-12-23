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


package ch.unige.ng.problems.nklandscape;

import ch.unige.ng.fitness.Fitness;
import ch.unige.ng.gen.Context;
import ch.unige.ng.species.Evaluable;
import ch.unige.ng.species.Individual;
import ch.unige.ng.tools.Flushable;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.rng.RNG;
import static ch.unige.ng.species.booleans.BooleanGeneFactory.*;

/**
 * This fitness calculates the fitness of a NK-landscape problem.
 *
 * The problem works on: {0,1}**k -> {0,1}
 *
 * There is only one lookup table, of size 2**k. For each gene, we determine a k-1 neigbours to look at. To compute
 * a gene, we get the index in the lookup table. The index is calculated like this: n[i] n[i*k+1] n[i*k+2] n[i*k+3]...
 *
 * The goal is to obtain a maximum of ones.
 *
 * @author Jacques Fontignie
 * @version 3 mai 2005
 */
public class NKLandscapeFitness implements Fitness<Boolean>,Flushable{

    private int k;
    private boolean lut [];
    private int neighbours [];
    private RNG rng;
    private int indSize;

    /**
     * Sets the number of neighbours
     * @param k
     */
    @Forced public void setK(int k){
        this.k = k;
    }

    /**
     * Sets the context
     * @param context
     */
    @Forced public void setContext(Context context) {
        this.rng = context.getRng();
    }


    /**
     * Sets a prototype to determine the size of the individuals
     * @param ind
     */
    @Forced public void setPrototype(Individual<Boolean> ind) {
        indSize = ind.size();
    }

    public void objectCreated() throws Exception {
        int size = 1<<k;
        lut   = new boolean[size];
        for (int i = 0; i < size; i++){
            lut[i] = (rng.nextBoolean())? TRUE: FALSE;
        }
        neighbours = new int[indSize*(k-1)];
        for (int i = 0; i < neighbours.length; i++)
            neighbours[i] = rng.nextInt(indSize);
    }

    public double compute(Evaluable<Boolean> evaluable) {
        double sum = 0;
        for (int i = 0; i < indSize; i++){
            Boolean val = estimate(evaluable,i);
            if (val == FALSE) sum++;
        }
        return sum;
    }

    private Boolean estimate(Evaluable<Boolean> evaluable, int current) {
        int index = (evaluable.get(current).booleanValue())? 1: 0;
        for (int i = 0; i < k-1; i++){
            int currentIndex = neighbours[current*(k-1)+i];
            index = index*2 + ((evaluable.get(currentIndex).booleanValue())? 1:0);
        }
        return lut[index];
    }
}
