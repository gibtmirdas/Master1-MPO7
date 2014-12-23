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


package ch.unige.ng.species.initializers.stackGP;

import ch.unige.ng.species.GeneFactory;
import ch.unige.ng.species.Individual;
import ch.unige.ng.species.Initializer;
import ch.unige.ng.species.VariableSize;
import ch.unige.ng.species.stackGP.operations.Operation;

/**
 * Initializes an individual that uses gp function
 *
 * TODO ERREUR DANS CETTE CLASSE, ne crèe pas necessairement un individu bien forme
 * @author Fontignie Jacques
 * @version 16 mai 2005
 */
public class StackGPInitializer <T extends Operation> implements Initializer<Individual<T>> {

    public StackGPInitializer(){}


    public void initialize(Individual<T> individual) {
        int numOutputs = 0;
        GeneFactory<T> geneFac = individual.getGeneFactory();
        //get the expected length of the individual
        int length = (individual instanceof VariableSize)? ((VariableSize) individual).getMaxSize() : individual.size();
        //We create an individual randomly, but we will be sure that each gene can be evaluated.
        for (int i = 0; i < length; i++) {
            int newOutput;
            int modStack;
            T newGene;
            do {
                newGene = geneFac.newGene(i);
                modStack = newGene.modStackSize();
                newOutput = numOutputs + modStack;
            } while (newOutput < 0 || numOutputs < newGene.minStackSize());
            numOutputs += modStack;
            individual.set(newGene, i);
        }
    }

}
