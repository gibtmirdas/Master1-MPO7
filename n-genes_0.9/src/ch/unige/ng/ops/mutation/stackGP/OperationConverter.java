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

import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.species.VariableSize;
import ch.unige.ng.species.GeneFactory;
import ch.unige.ng.ops.mutation.Mutator;
import ch.unige.ng.gen.Context;

/**
 * Replaces a gene by an other that has the same particularities:
 * the same minStackSize and the same modStackSize.
 *
 * @author Jacques Fontignie
 * @version 18 mai 2005
 */
public class OperationConverter extends Mutator<VariableSize<Operation>> {

    public void operate(VariableSize<Operation> variableSize, Context context) {
        int size = variableSize.size();
        if (size == 0)
            return;
        //Get the position of the mutation
        int mutPos = context.getRng().nextInt(size);
        Operation gene = variableSize.get(mutPos);
        int minStack = gene.minStackSize();
        int modStack = gene.modStackSize();
        Operation newOp;
        GeneFactory<Operation> genefac = variableSize.getGeneFactory();
        //Create a gene until the properties are the same as in gene
        do {
            newOp = genefac.newGene(mutPos);
            if (minStack == newOp.minStackSize() && modStack == newOp.modStackSize())
                break;
        } while (true);
        //Change the gene
        variableSize.set(newOp,mutPos);
    }
}
