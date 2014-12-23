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


package ch.unige.ng.species.stackGP.evaluations;

import ch.unige.ng.species.stackGP.ExecutionPointer;
import ch.unige.ng.species.stackGP.DataStack;
import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.species.Evaluable;
import ch.unige.ng.tools.Forced;

/**
 * @author Jacques Fontignie
 * @version 21 mars 2005
 */
public class VerboseEvaluator implements IEvaluator{


    /**
     * The execution pointer
     */
    private ExecutionPointer pointer;

    private int iterationMax;

    public VerboseEvaluator() {
        pointer = new ExecutionPointer();
    }

    @Forced public void setIterationMax(int iterationMax) {
        this.iterationMax = iterationMax;
    }

    /**
     * Evaluate the Individual on a stack passed on parameter.
     * Each Instruction will be sequentially performed on the stack .
     *
     * @param stack the working stack.
     * @return The number of steps
     */
    public <S,T extends Operation<S>> int evaluate(DataStack<S> stack, Evaluable<T> evaluable) {
       int numSteps = 0;
        pointer.init();
        System.out.println(stack);
        while ((pointer.getHead() < evaluable.size() && pointer.getHead() >= 0) && numSteps++ < iterationMax) {
            T gene = evaluable.get(pointer.getHead());
            System.out.println(gene);
            if (stack.size() >= gene.minStackSize())
                gene.exec(stack, pointer);
            pointer.next();
            System.out.println(stack);
        }
        return numSteps;
    }
}
