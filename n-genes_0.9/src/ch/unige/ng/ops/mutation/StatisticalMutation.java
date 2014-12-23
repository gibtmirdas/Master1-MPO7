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

import ch.unige.ng.species.Individual;
import ch.unige.ng.statistics.Statistic;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.gen.Context;
import ch.unige.ng.populations.Population;

/**
 * This class is a mutation that just keep in memory the quality of the mutation.
 * It can be used as a statistic to see the quality of the mutation used.
 * The result will be:
 *
 * @author Jacques Fontignie
 * @version 15 juin 2005
 */
public class StatisticalMutation <T extends Individual> extends Mutator<T> implements Statistic<Double, T> {

    private Mutator<T> mutator;
    private double sum;
    private double nums;

    public StatisticalMutation() {
        sum = 0;
        nums = 0;
    }

    /**
     * Sets the mutator used
     *
     * @param mutator
     */
    public StatisticalMutation(Mutator<T> mutator) {
        this();
        setMutator(mutator);
    }

    /**
     * Sets the mutator used
     *
     * @param mutator
     */
    @Forced public void setMutator(Mutator<T> mutator) {
        this.mutator = mutator;
    }


    public void operate(T mate2, Context context) {
        double before = mate2.getFitness();
        mutator.operate(mate2, context);
        double after = mate2.getFitness();
        double result = (before - after) / (before);
        if (result < -3 || Double.isNaN(result)) result = -3;
        sum += result;
        nums++;
    }

    public Double evaluate(Population<T> population) {
        if (nums == 0) return 0.;
        double result = sum / nums * 100;
        sum = 0;
        nums = 0;
        return result;
    }

    public String getName() {
        return "% mutation quality";
    }
}
