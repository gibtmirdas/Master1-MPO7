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


package ch.unige.ng.ops.mutation.stackGP;

import ch.unige.ng.fitness.GPFitness;
import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.mutation.Mutator;
import ch.unige.ng.species.VariableSize;
import ch.unige.ng.species.stackGP.operations.Operation;

/**
 * n-genes
 * <p/>
 * Clean the genome keeping only the sufficients instruction
 * to produce the results.
 * <p/>
 * The idea is to suppress all the operation that will not be in the result. The stack at the end of the evaluation can
 * have a certain size. The goal is to avoid some problem like
 * <p/>
 * <pre>
 * x y mul  z div  x y mul
 * </pre>
 * And we want only one value at the peak. The result will be
 * <pre>
 * x y mul
 * </pre>
 *
 * @author Jacques Fontignie
 * @version 5 janv. 2005
 */
public class BackwardCleaner extends Mutator<VariableSize<Operation>> {
    private ForwardCleaner cleaner;


    public BackwardCleaner() {
        cleaner = new ForwardCleaner(ForwardCleaner.DeletionMode.FITNESS_DESTRUCTIVE);
    }
   
    /**
     * Clean the genome keeping only the sufficients instruction
     * to produce the results.
     */
    public void operate(VariableSize<Operation> individual, Context context) {
        cleaner.operate(individual, context);
        if (individual.size() == 0) {
            return;
        }
        int backSteps = ((GPFitness) individual.getFitnessFunction()).getOutputSize();
        int instr = individual.size() - 1;
        while (backSteps > 0) {
            backSteps -= individual.get(instr).modStackSize();
            instr--;
        }
        individual.delete(0, instr + 1);
    }
}
