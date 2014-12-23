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


package ch.unige.ng.problems.sum;

import ch.unige.ng.fitness.Fitness;
import ch.unige.ng.species.Evaluable;
import ch.unige.ng.species.IntervalGeneFactory;

/**
 * Sum is a simple example, implementing the
 * very simple sum problem (fitness = sum over vector).
 * This is a generalization of the common DoubleMaxOnes problem
 * (fitness = number of 1's in vector).
 *
 * @author Jacques Fontignie
 * @version 13 avr. 2005
 */
public class SumFitness implements Fitness<Integer>{
    private IntervalGeneFactory<Integer> genefac;

    public void setGeneFactory(IntervalGeneFactory<Integer> genefac){
        this.genefac = genefac;
    }

    public double compute(Evaluable<Integer> evaluable) {
        double sum = 0;
        int maxValue = evaluable.size()*genefac.getMax();
        for (int i = 0, length = evaluable.size(); i < length; i++)
            sum += evaluable.get(i);
        return maxValue-sum;
    }
}
