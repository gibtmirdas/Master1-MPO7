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


package ch.unige.ng.statistics;

import ch.unige.ng.populations.Population;
import ch.unige.ng.species.GlobalIndividual;

/**
 * n-genes
 * <p/>
 * This interface gives the definition of a statistic that is done on a population
 *
 * @author Jacques Fontignie
 * @version 16 dec. 2004
 */
public interface Statistic <T,S extends GlobalIndividual> {


    /**
     * @param population
     * @return an evaluation of the oldPopulationion
     */
    public T evaluate(Population<S> population);

    /**
     * @return The name of the statistic
     */
    public String getName();
}
