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
 * This operator expand a random instruction of a StackGP individuals to a
 * Random branch.
 *
 * @author Jean-Luc Falcone
 * @version Sep 20, 2004 - 4:36:07 PM
 */
public class Promoter extends Mutator<VariableSize<Operation>> {
    private int maxSize;

    /**
     *
     * @param maxSize the maximum size of the generated branch
     */
    public Promoter(int maxSize){
        this();
        setMaxSize(maxSize);
    }

    public Promoter() {
        super();
    }

    @Forced public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void operate(VariableSize<Operation> newInd, Context context) {
        int mutPos = 0;
        if (newInd.size() > 1) {
            mutPos = context.getRng().nextInt(newInd.size() - 1);
        }
        int pushesLeft = 1;
        newInd.delete(mutPos);
        int count = 0;
        do {
            Operation gene = newInd.getGeneFactory().newGene(mutPos);
            newInd.insert(mutPos, gene);
            pushesLeft -= gene.modStackSize();
        } while (pushesLeft != 0 && ++count < maxSize);
    }
}
