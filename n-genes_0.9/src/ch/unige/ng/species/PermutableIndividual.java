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

import ch.unige.ng.gen.Context;
import ch.unige.ng.species.printers.Printer;
import ch.unige.ng.tools.ArrayIterator;
import ch.unige.ng.tools.ArrayShuffler;
import ch.unige.ng.tools.ArraysFormatter;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.pool.ObjectPool;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Fontignie Jacques
 * @version 6 aug. 2005
 */
public class PermutableIndividual<T> extends AbstractIndividual<T> implements Permutable<T> {

    private T[] genome;          // The genome
    private PermutableGeneFactory<T> genefac;  // The factory
    private int size;            // The genome size
    private ObjectPool<PermutableIndividual<T>> pooler;
    private Printer<PermutableIndividual<T>> printer;
    private Initializer<PermutableIndividual<T>> initializer;


    /**
     * The constructor withe the gene factory
     *
     * @param geneFac
     */
    public PermutableIndividual(PermutableGeneFactory<T> geneFac, Printer<PermutableIndividual<T>> printer) {
        this();
        setGeneFactory(geneFac);
        setPrinter(printer);
        objectCreated();
    }

    /**
     * Empty constructor
     */
    public PermutableIndividual() {
        initializer = null;
    }

    /**
     * Construct a new instance.
     */
    public void objectCreated() {
        size = genefac.getSize();
        genome = (T[]) Array.newInstance(genefac.getClassGene(), size);
        pooler = new ObjectPool<PermutableIndividual<T>>(this);
    }

    public void setInitializer(Initializer<PermutableIndividual<T>> initializer) {
        this.initializer = initializer;
    }

    /**
     * Copy constructor.
     *
     * @param vi The PermutableIndividual to be copied
     */
    public PermutableIndividual(PermutableIndividual<T> vi) {
        super(vi);
        genefac = vi.genefac;
        size = vi.size;
        //Creating the genome
        genome = (T[]) Array.newInstance(vi.genefac.getClassGene(), vi.genome.length);
        pooler = vi.pooler;
        printer = vi.printer;
        initializer = vi.initializer;
    }

    public void copy(GlobalIndividual<T> individual) {
        super.copy(individual);
        PermutableIndividual<T> vi = (PermutableIndividual<T>) individual;
        assert size == vi.size;
        System.arraycopy(vi.genome, 0, genome, 0, size);
    }

    /**
     * This method is used to make an empty copy of the current individual. The individual has evrything but genes. When this method
     * is called, most of the type a warning: Unchecked Cast is thrown, <b>DON'T PANIC</b> it's normal.
     *
     * @return a an empty copy of the current individual
     */
    public PermutableIndividual<T> emptyCopy() {
        return pooler.get();
    }

    public PermutableIndividual<T> copy() {
        PermutableIndividual<T> ind = pooler.get();
        ind.copy(this);
        return ind;
    }

    public void copy(int beginSrc, int length, GlobalIndividual<T> src, int beginDest) {
        System.arraycopy(((PermutableIndividual<T>) src).genome, beginSrc, genome, beginDest, length);
        informUnsynchronized();
    }


    public void selfRelease() {
        super.selfRelease();
        pooler.release(this);
    }

    public void randomize() {
        if (initializer == null) {
            genefac.shuffleGenes(genome);
        } else {
            initializer.initialize(this);
        }
        informUnsynchronized();
    }

    /**
     * This function sets the new gene factory to the individual
     *
     * @param geneFactory
     */
    @Forced
    public void setGeneFactory(PermutableGeneFactory<T> geneFactory) {
        genefac = geneFactory;
    }

    public void clear() {
        informUnsynchronized();
    }

    public void deepClean() {
        informUnsynchronized();
    }

    public PermutableIndividual<T> newInstance() {
        return new PermutableIndividual<T>(this);
    }

    public int size() {
        return size;
    }

    public T get(int i) {
        return genome[i];
    }

    public void set(T newGene, int position) {
        genome[position] = newGene;
        informUnsynchronized();
    }

    /**
     * Sets the printer to display this individual to string
     *
     * @param printer
     */
    public void setPrinter(Printer<PermutableIndividual<T>> printer) {
        this.printer = printer;
    }


    public PermutableGeneFactory<T> getGeneFactory() {
        return genefac;
    }

    @Override
    public String toString() {
        return (printer == null) ? ArraysFormatter.dump(genome) : printer.toString(this);
    }


    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PermutableIndividual)) {
            //Check if the object is an evaluable and test it
            if (obj instanceof Evaluable) {
                Evaluable eval = (Evaluable) obj;
                if (size() != eval.size())
                    return false;
                for (int i = 0, length = size(); i < length; i++) {
                    if (!genome[i].equals(eval.get(i)))
                        return false;
                }
                return true;
            }
            return false;
        }
        PermutableIndividual ind = (PermutableIndividual) obj;
        return Arrays.deepEquals(genome, ind.genome);
    }


    public void shuffle(int startIndex, int endIndex, Context context) {
        ArrayShuffler.shuffle(genome, startIndex, endIndex, context.getRng());
        informUnsynchronized();
    }

    public void invert(int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            swap2(i, endIndex - 1 - i);
        }
        informUnsynchronized();
    }

    public void swap(int index1, int index2) {
        swap2(index1, index2);
        informUnsynchronized();
    }

    private void swap2(int index1, int index2) {
        T temp = genome[index1];
        genome[index1] = genome[index2];
        genome[index2] = temp;
    }

    /**
     * Checks if there are no values that are two times in the genome.
     * <p/>
     * The algorithm to check the consistency has a complexity O(nlog(n)), and it suppose that the objects have a
     * consistent hashCode: <b> no different object has the same hashCode and equals object have the same hashCode.</b>
     *
     * @return if the genome is consistent
     */
    public boolean checkConsistency() {
        return genefac.checkConsistency(this);
    }

    private int [] present;

    /**
     * The algorithm runs in o(n*n) to repair the genome
     */
    public void repair() {
        if (present == null)
            present = new int[size];
        if (checkConsistency()) return;
        //Fille the array: no gene is present
        for (int i = 0; i < size; i++)
            present[i] = 0;
        T genes [] = genefac.getAvailableGenes();
        //Fill the present array to know what gene is not present
        for (int i = 0; i < size; i++) {
            T gene = genes[i];
            for (int j = 0; j < size; j++) {
                if (gene.equals(genome[i])) {
                    present[i] ++;
                    break;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            if (present[i] == 0) {
                for (int j = 0; j < size; j++) {
                    if (present[j] > 1) {
                        int index = 0;
                        for (int k = 0; k < size; k++) {
                            if (genes[j] == genome[k]) {
                                index = k;
                                break;
                            }
                        }
                        genome[index] = genes[i];
                    }
                }
            }
        }
        informUnsynchronized();
    }


    public int computeHashCode() {
        return Arrays.deepHashCode(genome);
    }

    public Iterator<T> iterator() {
        return ArrayIterator.getIterator(genome);
    }
}
