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

import ch.unige.ng.fitness.GPFitness;
import ch.unige.ng.species.GeneFactory;
import ch.unige.ng.species.Individual;
import ch.unige.ng.species.Initializer;
import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.tools.Forced;

/**
 * This class creates a well-formed stackGP individual, in Weel-formed, If there is no loop, you can be
 * certain that the individual will be well-formed. The algorithm is took from the GROW method invented by KOZA
 * <p/>
 * There are some conditions: no loop and the number of output for each node is equal to 1.
 * <p/>
 * The algorithm works like this:
 * <ol>
 * <li> Starting from the root of the tree, every node is randomly chosen as either a function or a terminal</li>
 * <li> If the terminal is chosen, a randomly terminal is chosen</li>
 * <li>If the node is a function a random function is chosen. for every node of the function's children,
 * the algorithm start again</li>
 * </ol>
 *
 * @author Jacques Fontignie
 * @version 2 juin 2005
 */
public class GrowInitializer <T extends Operation> implements Initializer<Individual<T>> {
    private int maxDepth;
    private int counter;

    /**
     * The maxDepth on which the initializer can go
     * @param maxDepth
     */
    public GrowInitializer(int maxDepth) {
        this();
        setMaxDepth(maxDepth);
    }

    public GrowInitializer() {
    }

    @Forced public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    /**
     * This will initialize an individual. The algorithm used is the full metho given by Koza.
     * This method is not synchronized. There will be bugs if you do not care.
     *
     * @param individual
     */
    public void initialize(Individual<T> individual) {
        //get the good depth.
        createRootNode(individual, maxDepth);
    }

    private void createRootNode(Individual<T> individual, int maxDepth) {
        counter = 0;
        for (int i = 0, numRoots = ((GPFitness) individual.getFitnessFunction()).getOutputSize();
             i < numRoots;
             i++) {
            T newGene = individual.getGeneFactory().newGene(counter);
            //If it is a terminal, there is no more elements to add and numToAdd, will be equal to 0
            int numToAdd = newGene.minStackSize();
            for (int j = 0; j < numToAdd; j++) {
                createNode(individual, 2, maxDepth);
            }
            //Put the gene after. Then we have postfix
            individual.set(newGene, counter++);
        }
    }

    /**
     * @param genefac
     * @param position
     * @return terminal node.
     */
    private T createTerminal(GeneFactory<T> genefac, int position) {
        while (true) {
            T newGene = genefac.newGene(position);
            if (newGene.minStackSize() == 0)
                return newGene;
        }
    }

    private void createNode(Individual<T> individual, int currentDepth, int maxDepth) {
        if (currentDepth < maxDepth - 1) {
            T newGene = individual.getGeneFactory().newGene(counter);
            int minStack = newGene.minStackSize();
            for (int i = 0; i < minStack; i++) {
                createNode(individual, currentDepth + 1, maxDepth);
            }
            individual.set(newGene, counter++);
        } else {
            //We have to create a terminal to avoid being to deep.
            individual.set(createTerminal(individual.getGeneFactory(), counter), counter);
            counter++;
        }
    }
}
