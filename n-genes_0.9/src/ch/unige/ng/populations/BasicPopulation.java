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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * @author Jacques Fontignie
 * @version 5 avr. 2005
 */
public class BasicPopulation <T extends GlobalIndividual> implements Population<T> {
    private ArrayList<T> currentPopulation;
    private ArrayList<T> creatingPopulation;


    // An object storing computed measurements
    private StatisticCache<T> sCache;

    // The Current Iteration
    private int step;

    public BasicPopulation() {
    }


    /**
     * Initialize and populate the population. The prototype described above will be used to
     * create the new Individuals.
     *
     * @param size      the population size
     * @param prototype the prototype used to fill the population
     */
    public void initialize(int size, T prototype) {
        currentPopulation = new ArrayList<T>(size);
        creatingPopulation = new ArrayList<T>(size);
        sCache = new StatisticCache<T>(this);
        step = 0;
        for (int i = 0; i < size; i++) {
            T newInd = (T) prototype.emptyCopy();
            newInd.randomize();
            currentPopulation.add(newInd);
        }
    }


    /**
     * Allows the user to retrieve an Individual of the population.
     * The object returned is a reference of the object. If it is wanted to use it in a new
     * generation, it <b> has to be copied </b>
     *
     * @param i the position of the wanted Individual in the OldPopulation
     * @return an Individual.
     */
    public T get(int i) {
        T newInd = (T) currentPopulation.get(i);
        return newInd;
    }

    /**
     * Returns the population size
     *
     * @return the Population size
     */
    public int size() {
        return currentPopulation.size();
    }

    /**
     * @return the creatingPopulation size
     */
    public int nextSize() {
        return creatingPopulation.size();
    }

    /**
     * Add an Individual to the population, the change are commited only after
     * a call to the method nextStep.
     *
     * @param individual
     */
    public void add(T individual) {
        creatingPopulation.add(individual);
        assert individual != null;
    }

    /**
     * Add an array of Individual to the OldPopulation.
     *
     * @param indArray The Individuals to be added.
     */
    public void add(T[] indArray) {
        for (T ind : indArray) {
            creatingPopulation.add(ind);
        }
    }

    /**
     * This method is equivalent to the nextStep, but this does not change the value step.
     * The idea is that it is possible to make some work on the population, like cleaning
     * the population, or applying an hill climbing
     */
    public void commit() {
        ArrayList<T> tempList = currentPopulation;
        currentPopulation = creatingPopulation;
        creatingPopulation = tempList;        
        sCache.invalidate();
        freeNextPopulation();
    }

    /**
     * This method frees the next population
     */
    private void freeNextPopulation() {
        for (T ind : creatingPopulation) {
            ind.selfRelease();
        }
        creatingPopulation.clear();
    }

    /**
     * Increments the iteration number and commits all changes to the population
     *
     * @return the new iteration number
     */
    public int nextStep() {
        ArrayList<T> tempList = currentPopulation;
        currentPopulation = creatingPopulation;
        creatingPopulation = tempList;
        freeNextPopulation();
        step++;
        return step;
    }

    /**
     * @return the current iteration number
     */
    public int getStep() {
        return step;
    }

    /**
     * @return the associated statistic cache
     */
    public StatisticCache<T> getStatisticCache() {
        return sCache;
    }


    /**
     * Format the population as String and send it to a PrintWriter.
     * The toString() method of the Individuals is called and the fitness is added.
     *
     * @param pw
     */
    public void dump(PrintWriter pw) {
        for (T vib : currentPopulation) {
            pw.println(vib + ": " + vib.getFitness());
        }
    }

    /**
     * Return an iterator of the OldPopulation.  DO NOT MODIFY THE ITERATOR
     *
     * @return The iterator.
     */
    public Iterator<T> iterator() {
        return currentPopulation.iterator();
    }

    /**
     * Sort the Individuals in the populations according to their fitness. The best is the first
     */
    public void sort() {        
        Collections.sort(currentPopulation,
                /* A comparator based on the fitness */
                FITNESS_COMPARATOR);
    }

    public void sort(Comparator<T> comparator) {
        Collections.sort(currentPopulation, comparator);
    }

    public void addAll(IGroup<T> iPopulation) {
        for (T ind : iPopulation)
            add(ind);
    }


    public T remove(int index) {
        sCache.invalidate();
        return currentPopulation.remove(index);
    }

    public boolean isEmpty() {
        return currentPopulation.isEmpty();
    }
}
