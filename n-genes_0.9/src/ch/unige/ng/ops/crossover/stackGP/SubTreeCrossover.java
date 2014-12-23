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


package ch.unige.ng.ops.crossover.stackGP;

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.crossover.DoubleOffspringCrossOver;
import ch.unige.ng.ops.mutation.stackGP.BackwardCleaner;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.species.VariableSize;
import ch.unige.ng.species.stackGP.operations.Operation;

/**
 * This crossober Swap two subtrees. It is a standard in genetic programming.
 * @version 3 juil. 2005
 */
public class SubTreeCrossover extends DoubleOffspringCrossOver<VariableSize<Operation>> {

    private BackwardCleaner cleaner;
    private boolean cleaning;

    public SubTreeCrossover(boolean cleaning) {
        this.cleaning = cleaning;
    }

    /**
     * Do we have to clean the genome before crossover.
     *
     * @param cleaning
     */
    public void setCleaning(boolean cleaning) {
        this.cleaning = cleaning;
    }

    /**
     * Empty constructor for parser compliance
     */
    public SubTreeCrossover() {
        cleaner = new BackwardCleaner();
        cleaning = true;
    }

    public void twoOffspringCrossover(VariableSize<Operation> mate1, VariableSize<Operation> mate2, IGroup<VariableSize<Operation>> population, Context context) {
        VariableSize<Operation> child1 = (VariableSize<Operation>) mate1.copy();
        VariableSize<Operation> child2 = (VariableSize<Operation>) mate2.copy();

        //Force the cleaning
        if (cleaning) {
            cleaner.operate(child1, context);
            cleaner.operate(child2, context);
        }

        if (child2.size() < 2 || child1.size() < 2) {
            population.add(child2);
            population.add(child1);
            return;
        }

        int beginChild1, endChild1;
        int beginChild2, endChild2;

        //Loop until a good sized individual is found
        do {
            endChild1 = context.getRng().nextInt(child1.size() );
            beginChild1 = getBeginPosition(child1, endChild1);
            endChild2 = context.getRng().nextInt(child2.size() );
            beginChild2 = getBeginPosition(child2, endChild2);
        } while (child2.size() - (endChild2 - beginChild2 + 1) + (endChild1 - beginChild1 + 1) > child2.getMaxSize() &&
                child1.size() - (endChild1 - beginChild1 + 1) + (endChild2 - beginChild2 + 1) > child1.getMaxSize());

        //Get the two subtrees that will have to be swaped
        VariableSize<Operation> subtree1 = child1.getSubIndividual(beginChild1, endChild1 + 1);
        VariableSize<Operation> subtree2 = child2.getSubIndividual(beginChild2, endChild2 + 1);
        //Delete the subtree in the parent
        child1.delete(beginChild1, endChild1 + 1);
        child2.delete(beginChild2, endChild2 + 1);
        //Insert the subrtreee
        child2.insert(beginChild2, subtree1);
        child1.insert(beginChild1, subtree2);

        //We have to relresae all to be sure that no useles individuals will be garbaged
        subtree1.selfRelease();
        subtree2.selfRelease();

        population.add(child2);
        population.add(child1);
    }

    public void oneOffspringCrossover(VariableSize<Operation> mate1, VariableSize<Operation> mate2, IGroup<VariableSize<Operation>> population, Context context) {
        VariableSize<Operation> m = (VariableSize<Operation>) mate1.copy();
        VariableSize<Operation> child = (VariableSize<Operation>) mate2.copy();

        //Force the cleaning
        if (cleaning) {
            cleaner.operate(m, context);
            cleaner.operate(child, context);
        }
        /* Tester s'ils sont ridicules */
        if (child.size() < 2 || m.size() < 2) {
            population.add(child);
            return;
        }

        int beginMate, endMate;
        int beginChild, endChild;

        //Loop until a good sized individual is found
        do {
            endMate = context.getRng().nextInt(m.size() );
            beginMate = getBeginPosition(m, endMate);
            endChild = context.getRng().nextInt(child.size() );
            beginChild = getBeginPosition(child, endChild);
        } while (child.size() - (endChild - beginChild + 1) + (endMate - beginMate + 1) > child.getMaxSize());

        child.delete(beginChild, endChild + 1);
        VariableSize<Operation> s = m.getSubIndividual(beginMate, endMate + 1);
        child.insert(beginChild, s);

        s.selfRelease();
        m.selfRelease();
        /* On retourne f */
        population.add(child);
    }

    /**
     * @param mate
     * @param end
     * @return the begin index of the subtree
     */
    private int getBeginPosition(VariableSize<Operation> mate, int end) {
        int backSteps = (mate.get(end)).minStackSize();
        int start = end;
        while (backSteps > 0 && start > 0) {
            start--;
            backSteps -= (mate.get(start)).modStackSize();
        }
        return start;
    }

    public void crossover(VariableSize<Operation> mate1,
                          VariableSize<Operation> mate2,
                          IGroup<VariableSize<Operation>> population, Context context) {

    }
}
