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


package ch.unige.ng.fitness.utils;

import ch.unige.ng.species.Evaluable;
import ch.unige.ng.tools.Forced;

/**
 * This class uses a NormalizableFitness and will transform the fitness between [0..1]
 *
 * @author Jacques Fontignie
 * @version 3 janv. 2005
 */
public class NormalizeFitness<T> implements NormalizableFitness<T> {

    /**
     * The fitness that has to be normalized
     */
    private NormalizableFitness<T> fitness;

    public NormalizableFitness<T> getFitness() {
        return fitness;
    }

    /**
     * Gives the normalized fitness
     *
     * @param fitness
     */
    @Forced public void setFitness(NormalizableFitness<T> fitness) {
        this.fitness = fitness;
    }

    public double getWorstFitness() {
        return 1;
    }

    public double getBestFitness() {
        return 0;
    }

    public double compute(Evaluable<T> ind) {
        double value = fitness.compute(ind);
        double result = (fitness.getBestFitness() - value) / (fitness.getBestFitness() - fitness.getWorstFitness());
        return result;
    }
}
