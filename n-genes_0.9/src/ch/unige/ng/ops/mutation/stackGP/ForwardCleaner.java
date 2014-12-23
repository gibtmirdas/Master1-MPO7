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

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.mutation.Mutator;
import ch.unige.ng.species.VariableSize;
import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.tools.Forced;

/**
 * n-genes
 * <p/>
 * This class is used to delete the operator in the program that could be considered as useless.
 * There are two modes:
 * <ul>
 * <li> <code> FITNESS_DESTRUCTIVE </code> is used when we just want to lower the size, the new individual will certainly not have the same fitness</li>
 * <li> <code> FITNESS_PRESERVATIVE </code> is used to mutate an individual and the new one will have exactly the same size
 * </ul>
 * <p/>
 * We go through each gene(operation) and we detect if the operation can run. if stack is not large enough, the gene is suppressed.
 *
 * @author Jacques Fontignie
 * @version 5 janv. 2005
 */
public class ForwardCleaner extends Mutator<VariableSize<Operation>> {


    public static enum DeletionMode {
        FITNESS_DESTRUCTIVE, FITNESS_PRESERVATIVE
    };

    private DeletionMode deletionMode;

    public ForwardCleaner() {
    }

    public ForwardCleaner(DeletionMode deletionMode) {
        this();
        this.deletionMode = deletionMode;
    }

    @Forced public void setDeletionMode(DeletionMode deletionMode) {
        this.deletionMode = deletionMode;
    }


    public void operate(VariableSize<Operation> newInd, Context context) {
        int heapSize = 0;
        switch (deletionMode) {
            case FITNESS_DESTRUCTIVE:
                for (int i = 0; i < newInd.size(); i++) {
                    Operation gene = newInd.get(i);
                    if (gene.minStackSize() > heapSize) {
                        newInd.delete(i--);
                    } else
                        heapSize += gene.modStackSize();
                }
                break;
            case FITNESS_PRESERVATIVE:
                throw new IllegalStateException("Not implemented yet");
        }

    }

}
