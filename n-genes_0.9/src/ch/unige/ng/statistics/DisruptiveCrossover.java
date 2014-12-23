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


package ch.unige.ng.statistics;

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.crossover.DoubleOffspringCrossOver;
import ch.unige.ng.populations.Group;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.populations.Population;
import ch.unige.ng.species.GlobalIndividual;
import ch.unige.ng.tools.Forced;

import java.util.Arrays;

/**
 * This class is used to see the statistics of a crosover. It creates a certain number of bins, and prints the
 * histogram of the modifications.
 *
 * It calculates the fitness of the created individual and test it against the old ones.
 *
 * @author Fontignie Jacques
 * @version 4 juin 2005
 */
public class DisruptiveCrossover<S,T extends GlobalIndividual<S>> extends DoubleOffspringCrossOver<T> implements Statistic<int[],T>{

    private int numBins;
    private DoubleOffspringCrossOver<T> crossover;
    private int bins[];
    private boolean clean;
    private Group<T> group;

    /**
     *
     * @param numBins The number of bins that are allowed for the statistic
     * @param crossover
     */
    public DisruptiveCrossover(int numBins, DoubleOffspringCrossOver<T> crossover){
        this();
        setCrossover(crossover);
        setNumBins(numBins);
    }


    public DisruptiveCrossover(){
        group = new Group<T>();
    }

    @Forced public void setCrossover(DoubleOffspringCrossOver<T> crossover) {
        this.crossover = crossover;
    }

    @Forced public void setNumBins(int numBins) {
        this.numBins = numBins;
        bins = new int[numBins];
        clean = true;
    }

    public void twoOffspringCrossover(T mate1, T mate2, IGroup<T> population, Context context) {
        if (clean)
            clearBins();
        double meanFitness = (mate1.getFitness()+mate2.getFitness())/2;
        crossover.twoOffspringCrossover(mate1,mate2,group,context);
        T offspring1 = group.remove(0);
        T offspring2 = group.remove(0);
        double fitness1 = offspring1.getFitness();
        double fitness2 = offspring2.getFitness();
        insertIntoBins(fitness1,meanFitness);
        insertIntoBins(fitness2,meanFitness);
        population.add(offspring1);
        population.add(offspring2);
    }

    private void insertIntoBins(double fitness1, double meanFitness) {
        //deltaF varies between -Inf:1
        double dF = (meanFitness-fitness1)/meanFitness;
        
        //We only consider: -1:1 if it is smaller, then we put it into -1...
        int index = (int) ((dF+1)/2*numBins-1);
        if (index < 0) index = 0;
        bins[index]++;
    }

    private void clearBins() {
        Arrays.fill(bins,0);
        clean = false;
    }

    public void oneOffspringCrossover(T mate1, T mate2, IGroup<T>population, Context context) {
        if (clean)
            clearBins();
        double meanFitness = (mate1.getFitness()+mate2.getFitness())/2;
        crossover.twoOffspringCrossover(mate1,mate2,group,context);
        T offspring1 = group.remove(0);
        double fitness1 = offspring1.getFitness();
        insertIntoBins(fitness1,meanFitness);
        population.add(offspring1);
    }

    public int[] evaluate(Population<T> population) {
        clean = true;
        return bins;
    }

    public String getName() {
        return "Disruptive crossover";
    }


}
