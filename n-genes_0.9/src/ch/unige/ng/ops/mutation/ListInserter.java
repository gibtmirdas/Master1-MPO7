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


package ch.unige.ng.ops.mutation;

import ch.unige.ng.gen.Context;
import ch.unige.ng.species.VariableSize;
import ch.unige.ng.tools.Forced;

/**
 * This Mutator insert a random List in a VariableSize Individual.
 * Ther random List is generated from the GeneProtoype of the
 * by an elongation technique. After adding the first gene, this
 * Mutator against a probability if it's lower it will elongate
 * on more Gene, if it's smaller, it will stop.
 *
 * @author Jean-Luc Falcone
 * @version Sep 17, 2004 - 3:53:59 PM
 */
public class ListInserter <S,T extends VariableSize<S>> extends Mutator<T> {

    private double stopProbability;


    /**
     * Unique Construcor
     *
     * @param stopProb Probability to stop the elongation of the subList
     */
    public ListInserter(double stopProb) {
        stopProbability = stopProb;
    }

    /**
     * Empty Constructor for Parser compliance
     */
    public ListInserter() {

    }

    /**
     * Defines the probability to continue: equals 1-(probability to stop)
     *
     * @param probability
     */
    @Forced public void setProbability(double probability) {
        stopProbability = 1 - probability;
    }

    public void operate(T ind, Context context) {
        //TODO A TESTER
        T subList = (T) ind.emptyCopy();        
        subList.insert(subList.getGeneFactory().newGene(subList.size()));
        while (context.getRng().nextBoolean(stopProbability)) {            
            subList.insert(subList.getGeneFactory().newGene(subList.size()));
        }
        int insertPosition = 0;
        if (ind.size() > 1) {
            insertPosition = context.getRng().nextInt(ind.size() - 1);
        }
        ind.insert(insertPosition, subList);
        subList.selfRelease();
    }
}
