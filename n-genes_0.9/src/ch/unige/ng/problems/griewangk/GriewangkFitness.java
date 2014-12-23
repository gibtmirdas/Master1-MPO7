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


package ch.unige.ng.problems.griewangk;

import ch.unige.ng.fitness.Fitness;
import ch.unige.ng.species.Evaluable;
import static java.lang.Math.*;

/**
 * Calculates the fitness of the individual determined by the griewangk function:
 *
 * $$ 1 + \sum_{i=1}^{n}{x_i^2} - \prod_{i=1}^{n}{cos(x_i/\sqrt{i}}$$
 *
 * @author Jacques Fontignie
 * @version 21 avr. 2005
 */
public class GriewangkFitness implements Fitness<Double>{

    public double compute(Evaluable<Double> evaluable) {
        double sum = 0;
        double product = 1;
        for (int i = 0, length = evaluable.size(); i< length;i++){
            double x = evaluable.get(i);
            sum +=(x*x)/4000.;
            product *= cos(x/sqrt(i+1));
        }
        return 1 + sum - product;       
    }
}
