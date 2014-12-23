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


package ch.unige.ng.problems.quartic;

import ch.unige.ng.fitness.Fitness;
import ch.unige.ng.gen.Context;
import ch.unige.ng.species.Evaluable;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.rng.RNG;

/**
 * Return the value of the random quartic function:
 *
 * f(x) = sum_{i=1..n}{i*x_i^4 + gauss()}
 *
 * @author Jacques Fontignie
 * @version 13 avr. 2005
 */
public class RandomQuarticFitness implements Fitness<Double> {
    private RNG rng;

    @Forced public void setContext(Context context) {
        rng = context.getRng();
    }

    public double compute(Evaluable<Double> evaluable) {
        double value = 0;
        for (int i = 0, length = evaluable.size(); i < length; i++) {
            double gene = evaluable.get(i);
            value += gene * gene * gene * gene * (i+1) + rng.nextGaussian();
        }
        return value;
    }

}
