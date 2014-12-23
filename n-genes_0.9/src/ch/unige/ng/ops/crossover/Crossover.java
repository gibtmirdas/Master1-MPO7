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


package ch.unige.ng.ops.crossover;

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.Operator;
import ch.unige.ng.species.GlobalIndividual;

/**
 * This interface forces a method that is a crossover to create a new individual given two parents.
 *
 * @author Jacques Fontignie
 * @version 2 juin 2005
 */
public interface Crossover<T extends GlobalIndividual> extends Operator<T>{

    /**
     *
     * This method makes a crossover between two mates, only one offspring is created,
     * classes that generate more than one have to select one randomly from the list.
     *
     * @param mate1
     * @param mate2
     * @param context
     * @return an individual that is the child of mate1 and mate2, only one individual has to be created
     */
    public T crossover(T mate1, T mate2, Context context);
}
