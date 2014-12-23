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

import java.util.Comparator;

/**
 *
 * This interface gives the methods that must be inherited by groups. A group is a list of individual
 *
 * @author Jacques Fontignie
 * @version 11 mars 2005
 * @see Population
 * @see Group
 */
public interface IGroup <T extends GlobalIndividual> extends Iterable<T> {


    public static final Comparator<GlobalIndividual> FITNESS_COMPARATOR = new Comparator<GlobalIndividual>() {
        public int compare(GlobalIndividual t1, GlobalIndividual t2) {
            if (t1 == t2)
                return 0;
            if (t1.getFitness() == t2.getFitness())
                return 0;
            if (t1.getFitness() > t2.getFitness())
                return +1;
            if (t1.getFitness() < t2.getFitness())
                return -1;
            throw new IllegalStateException();
        }
    };

      /**
     * Allows the user to retrieve an Individual of the population.
     * The object returned is a reference of the object. If it is wanted to use it in a new
     * generation, it <b> has to be copied </b>
     *
     * @param i the position of the wanted Individual in the OldPopulation
     * @return an Individual.
     */
    T get(int i);

    /**
     * Returns the population size
     *
     * @return the Population size
     */
    public int size();

    /**
     * Add an Individual to the population, the change are commited only after
     * a call to the method nextStep.
     *
     * @param individual
     */
    public void add(T individual);

    /**
     * Add an array of Individual to the OldPopulation.
     *
     * @param indArray The Individuals to be added.
     */
    public void add(T[] indArray);    

    void addAll(IGroup<T> population);

    /**
     * Removes a reference from the population and returns the element
     * <p/>
     * This element will not be self-released. You have to self release it if you know you will no more use it.
     *
     * @param index
     * @return the individual at the position index
     */
    T remove(int index);

    /**
     * @return is the population is empty
     */
    boolean isEmpty();

    /**
     * Sorts the population using the comparator
     *
     * @param comparator
     */
    void sort(Comparator<T> comparator);

    /**
     * Sorts the population with the best individual at position 0.
     */
    void sort();
}
