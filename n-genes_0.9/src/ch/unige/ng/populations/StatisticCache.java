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

package ch.unige.ng.populations;

import ch.unige.ng.species.GlobalIndividual;
import ch.unige.ng.statistics.Statistic;

import java.util.HashMap;

/**
 * Cache object storing different measurements on a population to
 * to speed up the statistic computations.
 *
 * @author Jean-Luc Falcone
 * @version Jan 3, 2005 - 3:14:00 PM
 */

public class StatisticCache<S extends GlobalIndividual> {

    private Population<S> pop;   // the population from which the measurements are done

    private HashMap<String, StatisticContainer<?>> hashMap;

    private int lastComputedIteration;   // used to determine is everything is up to date

    /* List of computed scores */
    private double sum;
    private double sum2; //squaredSum
    private S best;

    /**
     * Construct the cache.
     *
     * @param _pop The population to cache
     */
    public StatisticCache(Population<S> _pop) {
        pop = _pop;
        lastComputedIteration = -1;
        hashMap = new HashMap<String, StatisticContainer<?>>();
    }

    public <T> void addStatistic(Statistic<T,?> statistic) {
        StatisticContainer s = hashMap.put(statistic.getName(), new StatisticContainer<T>(statistic));
        //We just test that we have the same class
        assert s == null || s.getStatistic().getClass().equals(statistic.getClass());
    }

    /**
     * @param name the name of the statistic
     * @return the evaluation of the statistic of name name
     */
    public <T> T getStatistic(String name) {
        StatisticContainer statisticContainer = hashMap.get(name);
        if (pop.getStep() == statisticContainer.getLastCalculation()) {
            return (T) statisticContainer.getObject();
        }
        //We have to getNumVisited the statistc
        Statistic statistic = statisticContainer.getStatistic();
        T value = (T) statistic.evaluate(pop);
        statisticContainer.setObject(value);
        statisticContainer.setLastCalculation(pop.getStep());
        return value;
    }


    public double getSum() {
        if (lastComputedIteration != pop.getStep()) {
            computeScores();
        }
        return sum;
    }

    public double getSum2() {
        if (lastComputedIteration != pop.getStep()) {
            computeScores();
        }
        return sum2;
    }

    public S getBest() {
        if (lastComputedIteration != pop.getStep()) {
            computeScores();
        }
        return best;
    }

    private void computeScores() {
        if (best != null)
        //we have to release the best individual
            best.selfRelease();
        double bestFitness = Double.MAX_VALUE;
        sum = 0;
        sum2 = 0;
        for (S individual : pop) {
            double fitness = individual.getFitness();
            if (fitness < bestFitness) {
                bestFitness = fitness;
                best = individual;
            }
            sum += fitness;
            sum2 += fitness * fitness;
        }
        best = (S) best.copy();
        lastComputedIteration = pop.getStep();
    }

    /**
     * Invalidate the cache
     */
    public void invalidate() {
        lastComputedIteration = -1;
    }


    private class StatisticContainer<T> {
        private T object;
        private int lastCalculation;
        private Statistic<T,?> statistic;

        public StatisticContainer(Statistic<T,?> _statistic) {
            lastCalculation = -1;
            statistic = _statistic;
        }

        public T getObject() {
            return object;
        }

        public void setObject(T object) {
            this.object = object;
        }

        public int getLastCalculation() {
            return lastCalculation;
        }

        public void setLastCalculation(int lastCalculation) {
            this.lastCalculation = lastCalculation;
        }

        public Statistic<T,?> getStatistic() {
            return statistic;
        }

        public void setStatistic(Statistic<T,?> statistic) {
            this.statistic = statistic;
        }
    }

}
