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

import ch.unige.ng.gen.Context;
import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.tools.Flushable;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.rng.RNG;
import ch.unige.ng.fitness.GPFitness;

import java.util.List;
import java.lang.reflect.Array;

/**
 * n-genes
 * <p/>
 * This class is used to determine which gene has to be selected randomly.
 * <p/>
 * The method is used to select each gene (tree or atomic) with the same probability
 *
 * @author Jacques Fontignie
 * @version 4 janv. 2005
 */
public class SimpleStackGPGeneFactory <T> extends AbstractStackGPGeneFactory<T> implements Flushable {

    private Operation<T>[] terminals;
    private Operation<T>[] operations;
    private RNG rng;

    public SimpleStackGPGeneFactory(Context context, List<Operation<T>> operations, GPFitness<Operation<T>> fitness) throws Exception {
        this();
        setContext(context);
        for (Operation<T> op : operations)
            addOperation(op);
        setFitness(fitness);
        objectCreated();
    }

    public SimpleStackGPGeneFactory() {
    }

    public Operation<T> newOperation() {
        return operations[rng.nextInt(operations.length)];
    }

    @Forced public void setContext(Context _ctx) {
        rng = _ctx.getRng();
    }

    public Operation<T> randomGaussianValue(int position, double mean, double std) {
        throw new RuntimeException("Cet appel de methode n'a pas vraiment de sens...");
    }

    public Operation<T> differentValue(int position, Operation<T> operation) {
        Operation<T> newOp;
        do {
            newOp = newOperation();
        } while (operation == newOp);
        return newOp;
    }

    public Operation<T> getTerminal() {
        return terminals[rng.nextInt(terminals.length)];
    }

    public Operation<T> getNonTerminal() {
        return operations[rng.nextInt(operations.length)];
    }

    public void objectCreated() throws Exception {
        List<Operation<T>> terminal = getTerminalOperations();
        List<Operation<T>> nonTerminal = getNonTerminalOperations();
        int numTerminals = terminal.size();
        int numNonTerminals = nonTerminal.size();
        terminals = (Operation<T>[]) Array.newInstance(Operation.class,numTerminals);
        operations = (Operation<T>[]) Array.newInstance(Operation.class,numTerminals + numNonTerminals);
        for (int i = 0; i < numTerminals; i++) {
            terminals[i] = terminal.get(i);
            operations[i] = terminal.get(i);
        }
        for (int i = 0, index = numTerminals; i < numNonTerminals; i++,index++)
            operations[index] = nonTerminal.get(i);
    }

}
