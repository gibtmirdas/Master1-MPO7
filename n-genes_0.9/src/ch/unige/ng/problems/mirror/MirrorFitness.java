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


package ch.unige.ng.problems.mirror;

import ch.unige.ng.fitness.Fitness;
import ch.unige.ng.species.Evaluable;

/**
 * A new fitness that works  like a mirror. This is a fitness that is very bad with basicPointCrossover.
 * The number of minimas is equal to 2**(floor(n/2))
 *
 * @author Fontignie Jacques
 * @version 5 mai 2005
 */
public class MirrorFitness implements Fitness<Boolean>{

    public double compute(Evaluable<Boolean> evaluable) {
        double sum = 0;
        for (int i = 0, j = evaluable.size()-1; i< j; i++,j--)
            if (evaluable.get(i) != evaluable.get(j)) sum++;
        return sum;
    }
}
