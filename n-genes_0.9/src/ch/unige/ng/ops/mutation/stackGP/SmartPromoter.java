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
import ch.unige.ng.species.stackGP.factories.IStackGPGeneFactory;
import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.tools.Forced;

/**
 * This operator expand a random instruction of a StackGP individuals to a
 * Random branch.
 * <p/>
 * It works like the mutator operator defined in koza (1992) p.106
 * <p/>
 * The algorithm only works with these points:
 * <ol>
 * <li> There must be at least one terminal </li>
 * <li> A terminal must have these properties: minStackSize = 0, modStackSize=1</li>
 * <li> A gene must push at least 1 element</li>
 * <li> A terminal can be at every position. There is no rule </li>
 * </ol>
 * <p/>
 * This operator makes that the stackGP formalism is no more Turing complete: the rotate
 * method cannot work.
 *
 * @author Jacques Fontignie
 * @version 17 mai 2005
 */
public class SmartPromoter extends Mutator<VariableSize<Operation>> {

    private int maxSize;

    private Operation array [];

    public SmartPromoter(int maxSize) {
        this();
        setMaxSize(maxSize);
    }

    public SmartPromoter() {
        super();
    }

    @Forced public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        array = new Operation[maxSize];
    }

    public void operate(VariableSize<Operation> newInd, Context context) {
        //delete a branch.
        if (newInd.size() <= 1)
            return;
        int mutPos = context.getRng().nextInt(newInd.size());
        int position = deleteBranch(newInd, mutPos);


        IStackGPGeneFactory genefac = (IStackGPGeneFactory) newInd.getGeneFactory();

        int currentPosition = position + 1;
        int current;
        int numToAdd = 1; //Number of genes to add at the beginning: only 1.

        //We create the good number to avoid create impossible individuals
        int size = Math.min(maxSize, newInd.getMaxSize() - newInd.size());
        for (current = 0; current < size; current++) {
            //Create a gene
            Operation newGene = (Operation) genefac.newGene(currentPosition++);
            //There are still numToAdd genes to add to the list
            numToAdd += newGene.minStackSize() - 1;

            if (numToAdd == 0) {
                array[current] = newGene;
                break;
            }
            if (current + numToAdd >= size) {
                numToAdd -= newGene.minStackSize() - 1;
                //Put the terminals
                for (int length = current + numToAdd - 1; current <= length; current++)
                    array[current] = genefac.getTerminal();
                //substract 1 because of the last loop
                current--;
                break;
            }
            array[current] = newGene;


        }
        //We can now add the genes
        for (int i = 0; i <= current; i++) {
            newInd.insert(position, array[i]);
        }
    }

    private int deleteBranch(VariableSize<Operation> newInd, int mutPos) {

        //Get the gene and its stats
        Operation gene = newInd.get(mutPos);
        int minStackSize = gene.minStackSize();

        //First we have do delete all the genes that are depending on the current.
        //It only works if the number of elements added by each gene is equal to 1.
        int toSuppress = minStackSize;
        int position = mutPos - 1;
        while (toSuppress > 0 && position >= 0) {
            //The current is considered as supressed.
            //We get the number of elements necessary to calculate the current operation and we supress it.
            toSuppress = toSuppress - 1 + newInd.get(position--).minStackSize();
        }

        //Now we delete the selected genes.

        newInd.delete(position + 1, mutPos + 1);
        return position + 1;
    }
}