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

import ch.unige.ng.gen.Context;
import ch.unige.ng.species.Evaluable;
import ch.unige.ng.species.stackGP.DataStack;
import ch.unige.ng.species.stackGP.evaluations.IEvaluator;
import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.formulas.Equation;
import ch.unige.ng.tools.formulas.FormulaParser;
import static java.lang.Math.abs;

/**
 * This class calculates the fitness of an individual. The equation is given to regress is given into a string with the
 * same syntax as java.
 *
 * @author Jacques Fontignie
 * @version 11 avr. 2005
 */
public class FormulaFitness implements GPFitness<Operation> {

    private double min;
    private double max;
    private int numTests;


    private DataStack<Double> dts;
    private Context ctx;
    private IEvaluator evaluator;
    private Double input [][];
    private double output[];
    private Equation<Double> equation;

    /**
     * The constructor to create a fitness
     * @param evaluator the evaluator of the individual
     * @param context the context of the problem
     * @param numTests how much tests have to be done to evaluate an individual
     * @param equation what is the equation
     * @param min what is the min value of the inputs
     * @param max what is the max value of the intputs
     * @throws Exception
     * @see FormulaParser
     */
    public FormulaFitness(IEvaluator evaluator,Context context, int numTests,String equation,double min, double max) throws Exception {
        setEvaluator(evaluator);
        setContext(context);
        setNumTests(numTests);
        setEquation(equation);
        setMin(min);
        setMax(max);
        objectCreated();
    }

    public FormulaFitness() {
        dts = new DataStack<Double>();
    }

    @Forced public void setEvaluator(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Forced public void setContext(Context context) {
        ctx = context;
    }

    @Forced public void setNumTests(int numTests) {
        this.numTests = numTests;
    }

    @Forced public void setEquation(String equation) throws Exception {
        this.equation = FormulaParser.parse(equation, Double.class);
    }

    @Forced public void setMin(double min) {
        this.min = min;
    }

    @Forced public void setMax(double max) {
        this.max = max;
    }


    private double func(double[] input) {
        return equation.evaluate(input);
    }

    public int getInputSize() {
        return equation.getNumInputs();
    }

    public int getOutputSize() {
        return 1;
    }

    public double compute(Evaluable<Operation> evaluable) {
        double sum = 0;
        for (int i = 0; i < numTests; i++) {
            dts.clear();
            dts.setInputs(input[i]);
            evaluator.evaluate(dts, evaluable);
            if (dts.size() == 0) return MAX_VALUE;
            sum += abs(dts.pop() - output[i]);
            if (!(sum < MAX_VALUE)){
                return MAX_VALUE;
            }
        }
        return sum/numTests;
    }


    public void objectCreated() throws Exception {
        int numInput = equation.getNumInputs();
        input = new Double[numTests][numInput];
        output = new double[numTests];
        for (int i = 0; i < numTests; i++) {
            double array [] = new double[numInput];
            for (int j = 0; j < numInput; j++) {
                array[j] = ctx.getRng().nextDouble() * (max-min) + min;
                input[i][j] = array[j];
            }
            output[i] = func(array);
        }
    }

}
