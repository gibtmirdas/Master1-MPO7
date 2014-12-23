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


package ch.unige.ng.timer.conditions;

import ch.unige.ng.tools.Forced;
import ch.unige.ng.populations.Population;
import ch.unige.ng.gen.Context;

/**
 * Test a fitness stagnation.
 * The evolution will be terminated when the fitness does not change
 * during a number of iteration defined. This class does not make sense
 * when used with undeterministic fitness.
 *
 * @author Jean-Luc Falcone
 * @version May 30, 2005 - 2:48:00 PM
 */
public class StableFitnessCondition implements Condition{

    private int iterations;
    private int currentIterations;
    private double lastBestFitness;

    /**
     * Empty constructor
     */
    public StableFitnessCondition() {
        currentIterations = 0;
        lastBestFitness = Double.MAX_VALUE;
    }

    /**
     * Builds a StableFitnessHalting Object
     * @param iterations the number of iterations where the fitness does not change.
     */

    public StableFitnessCondition(int iterations) {
        this();
        this.iterations = iterations;
    }

    /**
     * Sets the number of iterations to wait before considering than the evolution
     * is freezed.
     * @param iterations
     */
    @Forced public void setIterations(int iterations) {
        this.iterations=iterations;
    }
    
    public boolean canFire(Population population, Context context) {
        //Get the best individual. We look in the cache
        double currentBestFitness = population.getStatisticCache().getBest().getFitness();
        if (currentBestFitness < lastBestFitness) {
            lastBestFitness = currentBestFitness;
            currentIterations = 0;
            return false;
        }
        currentIterations++;
        if (currentIterations == iterations){            
            currentIterations = 0;
            return true;
        }
        return false;
    }
}
