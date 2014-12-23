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
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.formulas.Equation;
import ch.unige.ng.tools.formulas.FormulaParser;

/**
 * This fitness calculates the sum of the equation on each gene.
 * <p/>
 * <p/>
 * If there is more than one variable, the program will select the first variable as the ith input,
 * the second as the (i-1)th,...
 *
 * @author Jacques Fontignie
 * @version 13 avr. 2005
 */
public class MultipleFormulaFitness implements Fitness<Double> {

    private Equation<Double> equation;
    private double array [];

    /**
     *
     * @param equation the equation used to evaluate the individual
     */
    public MultipleFormulaFitness(String equation) throws Exception {
        this();
        setEquation(equation);
    }

    public MultipleFormulaFitness() {
    }

    @Forced public void setEquation(String equation) throws Exception {
        this.equation = FormulaParser.parse(equation, Double.class);
        if (this.equation.getNumInputs() > 1) array = new double[this.equation.getNumInputs()];
    }


    public int getInputSize() {
        return equation.getNumInputs();
    }

    public double compute(Evaluable<Double> evaluable) {
        double value = 0;
        if (equation.getNumInputs() == 1) {
            for (int i = 0, length = evaluable.size(); i < length; i++)
                value += equation.evaluate(evaluable.get(i));
        } else {
            int numInputs = equation.getNumInputs();
            if (equation.getNumInputs() > 1)
                for (int i = numInputs - 1, length = evaluable.size(); i < length; i++) {
                    for (int j = 0; j < numInputs; j++) {
                        array[numInputs - 1 - j] = evaluable.get(i - j);
                    }
                    value += equation.evaluate(array);
                }
        }
        return value / evaluable.size();
    }


}
