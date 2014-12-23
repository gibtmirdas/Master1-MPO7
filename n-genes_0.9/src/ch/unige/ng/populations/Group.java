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

import java.util.*;

/**
 * This class is not a real population but this is used to make some work,
 * like  adding individuals or popping some...
 *
 * @author Jacques Fontignie
 * @version 11 mars 2005
 */
public class Group <T extends GlobalIndividual> implements IGroup<T> {
    private List<T> population;

    public Group() {
        population = new ArrayList<T>();
    }


    /**
     * This method clears all the individuals in the list, these are selfReleased
     */
    public void clear() {
        for (T ind : population)
            ind.selfRelease();
        population.clear();
    }

    public T get(int i) {
        return population.get(i);
    }

    public int size() {
        return population.size();
    }

    public void add(T t) {
        population.add(t);
    }

    public void add(T[] ts) {
        for (T t : ts)
            population.add(t);
    }

    public Iterator<T> iterator() {
        return population.iterator();
    }

    public void addAll(IGroup<T> iPopulation) {
        for (T ind : iPopulation)
            add(ind);
    }

    public T remove(int index) {
        return population.remove(index);
    }

    public boolean isEmpty() {
        return population.isEmpty();
    }

    public void remove(T mate) {
       population.remove(mate);
    }

    public void sort(Comparator<T> comparator) {
        Collections.sort(population, comparator);
    }

    public void sort() {
        Collections.sort(population,
                /* An comparator based on the fitness */
                FITNESS_COMPARATOR);
    }

    /**
     * Removes all elements. But does not clean the individuals
     */
    public void removeAll() {
        population.clear();
    }

    /**
     * This method makes a copy of each individual. The goal is to avoid keeping references
     */
    public void selfCopy() {
        int size = population.size();
        for (int i = 0; i < size; i++)
            population.add((T) population.remove(0).copy());
    }
}
