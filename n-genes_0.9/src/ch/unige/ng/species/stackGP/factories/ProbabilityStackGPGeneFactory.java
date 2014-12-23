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


package ch.unige.ng.species.stackGP.factories;

import ch.unige.ng.fitness.GPFitness;
import ch.unige.ng.gen.Context;
import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.tools.Flushable;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.rng.RNG;

import java.util.List;
import java.lang.reflect.Array;

/**
 * This class defines
 */
public class ProbabilityStackGPGeneFactory <T> extends AbstractStackGPGeneFactory<T> implements Flushable {

    private Operation<T>[] nonTerminalList;
    private Operation<T>[] atomicList;
    private int inputNum;
    private RNG rng;
    private double terminalProbability = 0.1;

    /**
     * @param terminalProbability what is the probability of a terminal object to appear
     * @param context             the context of the problem, principaly to use a random number generator
     * @param operations          the list of operators used
     * @param fitness             the fitness function used, to know the number of inputs and outputs
     */
    public ProbabilityStackGPGeneFactory(double terminalProbability, Context context, List<Operation<T>> operations,
                                         GPFitness<Operation<T>> fitness) throws Exception {
        this();
        setTerminalProbability(terminalProbability);
        setContext(context);
        for (Operation<T> op : operations)
            addOperation(op);
        setFitness(fitness);
        objectCreated();
    }

    public ProbabilityStackGPGeneFactory() {
    }


    @Forced public void setTerminalProbability(double terminalProbability) {
        this.terminalProbability = terminalProbability;
    }


    public Operation<T> newOperation() {
        double sum = (nonTerminalList.length + inputNum * terminalProbability);
        double value = (rng.nextDouble() * sum);
        if (value >= inputNum * terminalProbability) {
            int index = (int) ((value - inputNum * terminalProbability));
            return nonTerminalList[index];
        } else {
            int index = (int) (value / terminalProbability);
            return atomicList[index];
        }
    }

    @Forced public void setContext(Context _ctx) {
        rng = _ctx.getRng();
    }

    public Operation<T> randomGaussianValue(int position, double mean, double std) {
        throw new RuntimeException("Cet appel de methode n'a pas vraiment de sens...");
    }


    public Operation<T> getTerminal() {
        return atomicList[rng.nextInt(atomicList.length)];
    }

    public Operation<T> getNonTerminal() {
        return nonTerminalList[rng.nextInt(nonTerminalList.length)];
    }

    public void objectCreated() throws Exception {
        List<Operation<T>> terminals = getTerminalOperations();
        inputNum = terminals.size();
        atomicList = (Operation<T>[]) Array.newInstance(Operation.class,inputNum);
        for (int i = 0; i < inputNum; i++)
            atomicList[i] = terminals.get(i);
        List<Operation<T>> nonTerminals = getNonTerminalOperations();
        nonTerminalList = (Operation<T>[]) Array.newInstance(Operation.class,nonTerminals.size());
        for (int i = 0, length = nonTerminals.size(); i < length; i++)
            nonTerminalList[i] = nonTerminals.get(i);
    }

}
