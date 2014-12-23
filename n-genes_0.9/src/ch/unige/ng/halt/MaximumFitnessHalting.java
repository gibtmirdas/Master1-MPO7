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


package ch.unige.ng.halt;

import ch.unige.ng.gen.Context;
import ch.unige.ng.populations.Population;
import ch.unige.ng.tools.Forced;

/**
 * This function is used to stop when a maximum fitness is reached
 *
 * @author Jacques Fontignie
 * @version 17 d?c. 2004
 */
public class MaximumFitnessHalting implements HaltFunction {

    /**
     * If the fitness goes under this threshold, the the halting function returns <code> true </code>
     */
    private double threshold;

    public MaximumFitnessHalting(){}

    /**
     *
     * @param threshold the threshold at which the best solution is considered as found
     */
    public MaximumFitnessHalting(double threshold){
        setThreshold(threshold);
    }

    /**
     * Sets the thershold at which the fitness is considered as sufficently good.
     * @param threshold
     */
    @Forced public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public boolean isTerminated(Population population, Context context) {
        double value = population.getStatisticCache().getBest().getFitness();
        return value <= threshold;        
    }
}
