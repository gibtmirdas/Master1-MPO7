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
 * Stupid fitness that calculate the maxOnes with double: If the value is bigger than 0.5. The value is considered as a 1
 * else the value is considered as a 0. 
 *
 * @author Jacques Fontignie
 * @version 10 mars 2005
 */
public class DoubleMaxOnes implements Fitness<Double> {

    public double compute(Evaluable<Double> ind) {
        int sum = 0;
        for (int i = 0; i< ind.size(); i++) {
            if (ind.get(i).doubleValue() < 0.5)
                sum++;
        }
        return sum;
    }
    
}
