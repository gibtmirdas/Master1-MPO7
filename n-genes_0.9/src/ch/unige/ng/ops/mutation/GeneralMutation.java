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
 * @author Jacques Fontignie
 * @version 11 janv. 2005
 */
public class GeneralMutation <S,T extends VariableSize<S>> extends Mutator<T> {

    private double deletionProbability;
    private double insertionProbability;
    private double mutationProbability;
    private double sum = 0;

    /**
     *
     * @param insertion the probability to insert a new gene
     * @param deletion the probability to delete a gene
     * @param mutation the probability to mute a gene
     */
    public GeneralMutation(double insertion,double deletion, double mutation){
        this();
        setInsertionProbability(insertion);
        setDeletionProbability(deletion);
        setMutationProbability(mutation);
    }

    public GeneralMutation(){}

    public void setDeletionProbability(double deletionProbability) {
        this.deletionProbability = deletionProbability;
        calculateSum();
    }

    private void calculateSum() {
        sum = deletionProbability + insertionProbability + mutationProbability;
        if (sum > 1) throw new IllegalStateException("Probabilities are too big");
    }

    @Forced public void setInsertionProbability(double insertionProbability) {
        this.insertionProbability = insertionProbability;
        calculateSum();
    }

    @Forced public void setMutationProbability(double modificationProbability) {
        this.mutationProbability = modificationProbability;
        calculateSum();
    }

    public void operate(T newInd, Context context) {
        double random = context.getRng().nextDouble();
        if (random <= sum)
            mutate(newInd, random, context);
    }

    private void mutate(T individual, double random, Context context) {
        int indSize = individual.size();
        if (random < insertionProbability) {
            int position = context.getRng().nextInt(indSize + 1);
            individual.insert(position,individual.getGeneFactory().newGene(position));
            return;
        }
        if (indSize == 0) return;
        random -= insertionProbability;
        if (random < deletionProbability) {
            int position = context.getRng().nextInt(indSize);
            individual.delete(position);
            return;
        }


        int position = context.getRng().nextInt(indSize);
        individual.mutate(position);
    }

}
