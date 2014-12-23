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
import ch.unige.ng.species.printers.Printer;
import ch.unige.ng.tools.ArraysFormatter;
import ch.unige.ng.tools.Flushable;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.pool.ObjectPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * This class is a VariableSize individual representing
 * a StackGP individual.
 * A ProbabilityStackGPGeneFactory must be passed to the main constructor.
 * This factory will build the StackGP operations.
 *
 * @author JL Falcone
 * @version adding a printer to print the individual
 */
public class VariableSizeIndividual<T> extends AbstractIndividual<T> implements VariableSize<T>, Flushable {


    /**
     * The genome, it is by default an ArrayList because it is a RandomAccess and it is the faster structure
     */
    private final List<T> genome;

    /**
     * The genefactory that is used to create new genes
     */
    private GeneFactory<T> geneFac;

    /**
     * The printer used to print the individual
     */
    private Printer<VariableSizeIndividual<T>> printer;

    /**
     * The initializer used to create a random individual, this object is necessary
     */
    private Initializer<VariableSizeIndividual<T>> initializer;


    /**
     * MaxSize of the genome.
     */
    private int maxSize;

    private ObjectPool<VariableSizeIndividual<T>> pool;

    /**
     * @param indSize     the default size of the individual
     * @param genefac     the geneFactoryx
     * @param fitness     the fitness function used to estimate the quality
     * @param initializer The initializer used to create a new individual
     */
    public VariableSizeIndividual(int indSize, GeneFactory<T> genefac, Fitness<T> fitness, Initializer<VariableSizeIndividual<T>> initializer) {
        this();
        setFitness(fitness);
        setGeneFactory(genefac);
        setIndSize(indSize);
        setInitializer(initializer);
        objectCreated();
    }

    /**
     * Empty constructor to be parser compliant
     */
    public VariableSizeIndividual() {
        super();
        this.initializer = null;
        genome = new ArrayList<T>();
    }

    /**
     * Sets the initializer that will be used to create a random individual
     *
     * @param initializer
     */
    @Forced
    public void setInitializer(Initializer<VariableSizeIndividual<T>> initializer) {
        this.initializer = initializer;
    }

    /**
     * Creates a new empty individual with the same characteristic as si. But it is empty.
     * The genome is not initialized.
     *
     * @param si the StackGP inidvidual to be copied.
     */
    protected VariableSizeIndividual(VariableSizeIndividual<T> si) {
        super(si);
        //pointer = new ExecutionPointer();
        pool = si.pool;
        geneFac = si.geneFac;
        genome = new ArrayList<T>();
        maxSize = si.maxSize;
        printer = si.printer;
        initializer = si.initializer;
    }

    /**
     * @return a new Instance. It creates a new instance with a constructor,
     *         this function should not be used  by other class that ObjectPool.
     * @see ObjectPool
     */
    public VariableSizeIndividual<T> newInstance() {
        return new VariableSizeIndividual<T>(this);
    }

    public VariableSizeIndividual<T> emptyCopy() {
        return pool.get();
    }

    public VariableSizeIndividual<T> copy() {
        VariableSizeIndividual<T> ind = (VariableSizeIndividual<T>) pool.get();
        ind.copy(this);
        return ind;
    }

    public void copy(int beginSrc, int length, GlobalIndividual<T> individual, int beginDest) {

        VariableSizeIndividual<T> sgp = (VariableSizeIndividual<T>) individual;
        //if The size is 0, we just add the good number of individuals
        for (int i = beginDest; i < Math.min(beginDest + length, genome.size()); i++)
            genome.remove(beginDest);
        for (int i = 0; i < length; i++) {
            if (genome.size() == maxSize) return;
            genome.add(beginDest++, sgp.genome.get(beginSrc++));
        }
        informUnsynchronized();
    }

    public void selfRelease() {
        super.selfRelease();
        pool.release(this);
    }


    /**
     * Defines the maximal size of the individual
     *
     * @param maxSize
     */
    @Forced
    public void setIndSize(int maxSize) {
        this.maxSize = maxSize;
    }


    /**
     * Copy the new individual, it is an deep copy, the new indivial will have exactly
     *
     * @param individual
     */
    public void copy(GlobalIndividual<T> individual) {
        super.copy(individual);
        VariableSizeIndividual<T> si = (VariableSizeIndividual<T>) individual;
        copyGenome(si.genome);
        informUnsynchronized();
    }

    private void copyGenome(List<T> _genome) {
        genome.clear();
        for (T gene : _genome)
            genome.add(gene);
    }

    /**
     * @return genome's size
     */
    public int size() {
        return genome.size();
    }


    public T get(int i) {
        return genome.get(i);
    }

    /**
     * Change the gene at position position
     *
     * @param newGene
     * @param position
     */
    public void set(T newGene, int position) {
        if (genome.size() == position)
            genome.add(newGene);
        else {
            genome.set(position, newGene);
        }
        informUnsynchronized();
    }

    /**
     * Randomize the array
     */
    public void randomize() {
        initializer.initialize(this);
        informUnsynchronized();
    }

    /**
     * This function sets the new gene factory to the individual
     *
     * @param geneFactory
     */
    @Forced
    public void setGeneFactory(GeneFactory<T> geneFactory) {
        geneFac = geneFactory;
    }

    /**
     * Sets the printer for the toString method
     *
     * @param printer
     */
    public void setPrinter(Printer<VariableSizeIndividual<T>> printer) {
        this.printer = printer;
    }

    public GeneFactory<T> getGeneFactory() {
        return geneFac;
    }

    public int computeHashCode() {
        return genome.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof VariableSizeIndividual)) {
            //Check if the object is an evaluable and test it
            if (obj instanceof Evaluable) {
                Evaluable eval = (Evaluable) obj;
                if (size() != eval.size())
                    return false;
                for (int i = 0, length = size(); i < length; i++) {
                    if (!genome.get(i).equals(eval.get(i)))
                        return false;
                }
                return true;
            }
            return false;
        }
        VariableSizeIndividual ind = (VariableSizeIndividual) obj;
        return genome.equals(ind.genome);
    }

    public String toString() {
        return (printer == null) ? ArraysFormatter.dump(genome) : printer.toString(this);
    }

    /**
     * Delete the attribute at the position position. It could be possible to put a NOP a at the place
     *
     * @param position
     */
    public void delete(int position) {
        genome.remove(position);
        informUnsynchronized();
    }

    private final void basicInsert(int position, T g){
       if (position > size())
            return;
        genome.add(position, g);
        if (size() > maxSize) {
            genome.remove(position);
        }
    }

    public void insert(int position, T g) {
        basicInsert(position,g);
        informUnsynchronized();
    }

    public void insert(int position, VariableSize<T> varIndividual) {
        for (int i = 0; i < varIndividual.size() && size() < maxSize; i++) {
            basicInsert(position + i, varIndividual.get(i));
        }
        //We remove the last genes if the object is too big
        if (size() > maxSize)
            basicDelete(maxSize, size());
        informUnsynchronized();
    }

    public void insert(T g) {
        genome.add(g);
        if (size() > maxSize) {
            basicDelete(maxSize, size());
        }
        informUnsynchronized();
    }

    public void deepClean() {
        clear();
    }

    public void clear() {
        genome.clear();
        informUnsynchronized();
    }

    public final void basicDelete(int start,int end){
        for (int i = start; i < end; i++) {
            delete(start);
        }
    }

    public void delete(int start, int end) {
        basicDelete(start,end);
        informUnsynchronized();
    }


    public VariableSizeIndividual<T> getSubIndividual(int start, int end) {
        VariableSizeIndividual<T> subInd = (VariableSizeIndividual<T>) emptyCopy();
        for (int i = start; i < end; i++)
            subInd.insert(i - start, get(i));
        return subInd;
    }


    public void objectCreated() {
        pool = new ObjectPool<VariableSizeIndividual<T>>(this);
    }

    public void mutate(int i) {
        genome.set(i, geneFac.differentValue(i, genome.get(i)));
        informUnsynchronized();
    }

    /**
     * @return an iterator of the genome, but this iterator is unmodifiable
     */
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(genome).iterator();
    }

    public int getMaxSize() {
        return maxSize;
    }
}
