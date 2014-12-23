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


package ch.unige.ng.species;

import ch.unige.ng.fitness.Fitness;
import ch.unige.ng.fitness.UndeterministicFitness;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.pool.Poolable;


/**
 * n-genes
 *
 * @author Jacques Fontignie
 * @version 5 janv. 2005
 */
public abstract class AbstractIndividual <T> implements GlobalIndividual<T>, Poolable<AbstractIndividual<T>> {
    private Fitness<T> fitness;
    private boolean computedFitness = false;
    private boolean computedHashCode = false;
    private double fitnessCache;
    private int hashCodeCache;
    private boolean deterministic = true;
    private boolean released;

    /**
     * Constructor used by the sub classes. Do not try to use it.
     *
     * @param individual
     */
    protected AbstractIndividual(AbstractIndividual<T> individual) {
        fitness = individual.fitness;
        deterministic = individual.deterministic;
        released = true;
    }


    public AbstractIndividual() {
    }


    public void selfRelease() {
        if (released)
            throw new RuntimeException("You are releasing an object that has allready been released");
        released = true;
    }

    public void pull() {
        if (!released)
            throw new RuntimeException("You are pulling a class that has allready been used. This causes " +
                    "some border effects.");
        released = false;
    }

    /**
     * Copies the datas that are in individual. This method as to be subclassed
     *
     * @param individual
     */
    public void copy(GlobalIndividual<T> individual) {
        AbstractIndividual<T> abstractIndividual = (AbstractIndividual<T>) individual;
        computedFitness = abstractIndividual.computedFitness;
        computedHashCode = abstractIndividual.computedHashCode;
        hashCodeCache = abstractIndividual.hashCodeCache;
        fitnessCache = abstractIndividual.fitnessCache;
    }

    public final double getFitness() {
        if (!computedFitness) {
            fitnessCache = fitness.compute(this);
            //Sets the fitness as computed if we are in determistic mode, false otherwise
            computedFitness = deterministic;
        }
        return fitnessCache;
    }

    @Forced public void setFitness(Fitness<T> fitness) {
        this.fitness = fitness;
        deterministic = !(fitness instanceof UndeterministicFitness);
        computedFitness = false;
    }

    public Fitness<T> getFitnessFunction() {
        return fitness;
    }

    /**
     * @return the hashCode value
     */
    public final int hashCode() {
        if (!computedHashCode) {
            hashCodeCache = computeHashCode();
            computedHashCode = true;
        }
        return hashCodeCache;
    }

    /**
     * @return if the hashCode value.
     */
    public abstract int computeHashCode();


    /**
     * This method is used by subclasses to inform that the cache is unsynchronized with the individual
     */
    protected final void informUnsynchronized() {
        computedFitness = false;
        computedHashCode = false;
    }

    public final boolean isUpToDate() {
        return computedHashCode && (computedFitness || !deterministic);
    }
}
