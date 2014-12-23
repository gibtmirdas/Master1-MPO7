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

import ch.unige.ng.populations.Population;
import ch.unige.ng.gen.Context;
import ch.unige.ng.tools.formulas.Equation;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.gen.Context;

/**
 * This function is used to stop an evoluation when a certain condition occurs. The condition is defined by a String
 * <p/>
 * The first arg is used to determine the step, the second is used to determine the fitness.
 *
 * @author Jacques Fontignie
 * @version 14 avr. 2005
 */
public class EquationFunction implements HaltFunction {

    private Equation<Boolean> equation;

    public EquationFunction() {

    }

    @Forced public void setEquation(Equation<Boolean> equation) {
        this.equation = equation;
    }

    public boolean isTerminated(Population pop, Context context) {
        switch (equation.getNumInputs()) {
            case 1:
                return equation.evaluate(pop.getStep());
            case 2:
                return equation.evaluate(pop.getStep(),pop.getStatisticCache().getBest().getFitness());                
            default:
                throw new IllegalStateException("Number of arguments is incorrect");
        }
    }
}
