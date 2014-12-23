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

import ch.unige.ng.species.Individual;
import ch.unige.ng.species.Initializer;
import ch.unige.ng.species.GeneFactory;
import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.gen.Context;
import ch.unige.ng.fitness.GPFitness;

/**
 * This class creates a well-formed stackGP individual, in Weel-formed, If there is no loop, you can be
 * certain that the individual will be well-formed. The algorithm is took from the FULL method invented by KOZA
 * <p/>
 * There are some conditions: no loop and the number of output for each node is equal to 1.
 * <p/>
 * The algorithm works like this:
 * <ul>
 * <li> Select a depth for the tree </li>
 * <ul>
 * <li> For each depth, we create recursively a non terminal node</li>
 * <li> If it is the max depth, a terminal node is created</li>
 * </ul>
 * </ul>
 *
 * @author Jacques Fontignie
 * @version 2 juin 2005
 */
public class FullInitializer <T extends Operation> implements Initializer<Individual<T>> {
    private int minDepth;
    private int maxDepth;
    private Context context;
    private int counter;

    /**
     * @param minDepth the minimum depth of the individual
     * @param maxDepth the maximal depth of the population
     * @param context  the context used
     */
    public FullInitializer(int minDepth, int maxDepth, Context context) {
        this();
        setMinDepth(minDepth);
        setMaxDepth(maxDepth);
        setContext(context);
    }

    public FullInitializer() {
    }

    /**
     * @param context The context used, necessary to determine the number of nodes.
     */
    @Forced public void setContext(Context context) {
        this.context = context;
    }

    @Forced public void setMinDepth(int minDepth) {
        this.minDepth = minDepth;
        if (minDepth <= 1) {
            throw new IllegalArgumentException("The minimum depth has to be greater than 1");
        }
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
        int depth = context.getRng().nextInt(maxDepth - minDepth + 1) + minDepth;
        createRootNode(individual, depth);
    }

    /**
     * Create all the root nodes and create recursively the children
     *
     * @param individual
     * @param maxDepth
     */
    private void createRootNode(Individual<T> individual, int maxDepth) {
        counter = 0;

        //Create the good number of roots
        for (int i = 0, numRoots = ((GPFitness) individual.getFitnessFunction()).getOutputSize();
             i < numRoots;
             i++) {

            //Get a non terminal gene
            T newGene = createNonTerminal(individual.getGeneFactory(), counter);
            //How much nodes do we have to add
            int numToAdd = newGene.minStackSize();
            for (int j = 0; j < numToAdd; j++) {
                createNode(individual, 2, maxDepth);
            }
            //Put the gene after. Then we have postfix
            individual.set(newGene, counter++);
        }

    }

    /**
    *
     * @param genefac
     * @param position
     * @return a non terminal node
     */
    private T createNonTerminal(GeneFactory<T> genefac, int position) {
        while (true) {
            T newGene = genefac.newGene(position);
            if (newGene.minStackSize() != 0)
                return newGene;
        }
    }

    /**
     *
     * @param genefac
     * @param position
     * @return a terminal node
     */
    private T createTerminal(GeneFactory<T> genefac, int position) {
        while (true) {
            T newGene = genefac.newGene(position);
            if (newGene.minStackSize() == 0)
                return newGene;
        }
    }

    /**
     * Creates a node 
     * @param individual
     * @param currentDepth
     * @param maxDepth
     */
    private void createNode(Individual<T> individual, int currentDepth, int maxDepth) {
        if (currentDepth < maxDepth - 1) {
            T newGene = createNonTerminal(individual.getGeneFactory(), counter);
            int minStack = newGene.minStackSize();
            for (int i = 0; i < minStack; i++) {
                createNode(individual, currentDepth + 1, maxDepth);
            }
            individual.set(newGene, counter++);
        } else {
            individual.set(createTerminal(individual.getGeneFactory(), counter), counter);
            counter++;
        }
    }
}
