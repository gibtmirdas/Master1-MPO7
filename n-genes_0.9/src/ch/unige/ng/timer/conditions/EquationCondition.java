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


package ch.unige.ng.timer.conditions;

import ch.unige.ng.populations.Population;
import ch.unige.ng.gen.Context;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.formulas.Equation;
import ch.unige.ng.tools.formulas.FormulaParser;

/**
 * This condition is fired when the condition is defined by an equation si fired.
 * @author Jacques Fontignie
 * @version 13 avr. 2005
 */
public class EquationCondition implements Condition{
    private Equation<Boolean> equation;

    /**
     * Sets the equation that will be used. You just have to give a variable and
     * this one will be considered as the step.
     * @param equation
     * @throws Exception
     */
    @Forced public void setEquation(String equation) throws Exception {
        this.equation = FormulaParser.parse(equation,Boolean.class);
        if (this.equation.getNumInputs() > 1)
            throw new IllegalArgumentException("There must be only one variable in the equation");
    }

    public boolean canFire(Population population, Context context) {
        return context.isEvolving() && equation.evaluate(population.getStep()).booleanValue();
    }
}
