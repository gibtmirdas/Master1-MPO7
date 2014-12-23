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

import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.species.stackGP.operations.Push;
import ch.unige.ng.fitness.GPFitness;
import ch.unige.ng.tools.Forced;

import java.util.List;
import java.util.ArrayList;

/**
 * n-genes
 * <p/>
 * This class builds StackGPGene according to allowed operations.
 * The use of a FlyWeight implementation reduce the memory footprint
 * and the garbage collection pauses.
 * The generic type <B>T</B> is the type of the program variables.
 *
 * @author Jean-Luc Falcone
 * @author Jacques Fontignie
 * @version 4 janv. 2005
 * @see Operation
 */
public abstract class AbstractStackGPGeneFactory <T> implements IStackGPGeneFactory<T> {

    private List<Operation<T>> nonTerminals;
    private List<Operation<T>> terminals;

    public AbstractStackGPGeneFactory() {
        nonTerminals = new ArrayList<Operation<T>>();
        terminals = new ArrayList<Operation<T>>();
    }

    public Class getClassGene() {
        return Operation.class;
    }

    public Operation<T> newGene(int position) {
        return newOperation();
    }

    public Operation<T> differentValue(int position,Operation<T> operation) {
        Operation<T> newOp;
        do {
            newOp = newOperation();
        } while (operation == newOp);
        return newOp;
    }

    @Forced public final void addOperation(Operation<T> operation) {
        if (operation.minStackSize() == 0){
            terminals.add(operation);
        } else
            nonTerminals.add(operation);
    }

    @Forced public final void setFitness(GPFitness<Operation<T>> fit) {
        int numInputs = fit.getInputSize();
        for (int i = 0; i < numInputs; i++){
            terminals.add(new Push<T>(i));
        }
    }

    protected List<Operation<T>> getTerminalOperations(){
        return terminals;
    }

    protected List<Operation<T>> getNonTerminalOperations(){
        return nonTerminals;
    }



}
