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

import ch.unige.ng.fitness.Fitness;

/**
 * A normalizable fitness that knows the best Fitness value (should be zero) and the worst fitness
 * (should be greater than 0). It can after that be used with NormalizeFitness that will change the value between 0 and 1.
 *
 * @author Jacques Fontignie
 * @version 3 janv. 2005
 * @see NormalizeFitness
 */
public interface NormalizableFitness<T> extends Fitness<T> {

    /**
     * @return the minValue of the fitness
     */
    public double getWorstFitness();

    /**
     * @return the maxValue of the fitness
     */
    public double getBestFitness();
}
