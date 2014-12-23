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


package ch.unige.ng.fitness;

import ch.unige.ng.species.Evaluable;


/**
 * This interface modelize a fitness function.
 * <b> By convention, the fitness has to be positive or zero.
 * The lower is the best. </b>
 *
 * @author Jean-Luc Falcone
 * @version 20-sept-2004
 */
public interface Fitness <T> {

    /**
     * The same as lilgp and ecj
     */
    public static final double MAX_VALUE = 1.0E15;

    /**
     * This method getNumVisited an Individual and return the fitness.
     *
     * @param ind The Individual to be evaluated
     * @return the value of the fitness function.
     */
    public double compute(Evaluable<T> ind);

}
