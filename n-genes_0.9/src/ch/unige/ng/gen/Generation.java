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


package ch.unige.ng.gen;

import ch.unige.ng.populations.Population;
import ch.unige.ng.species.GlobalIndividual;

/**
 * @author JL Falcone
 */
public interface Generation <T extends GlobalIndividual> {

    /**
     * This method is called to inform that this is a new generation and there is some work to do on it
     *
     * @param pop     the population
     * @param context the context of the algorithm
     */
    void calculate(Population<T> pop, Context context);


    /**
     * This method is after an iteration to inform classes that the population is in a new state and that
     * some statistics by example can be calculated. This method should not change the population. The goal is to keep
     * statistics and to be sure that they will not be changed.
     *
     * @param pop
     * @param context
     */
    void refresh(Population<T> pop, Context context);
}
