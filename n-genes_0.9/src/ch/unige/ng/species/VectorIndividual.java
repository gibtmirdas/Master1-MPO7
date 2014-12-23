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

import ch.unige.ng.species.printers.Printer;
import ch.unige.ng.tools.ArraysFormatter;
import ch.unige.ng.tools.Flushable;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.ArrayIterator;
import ch.unige.ng.tools.pool.ObjectPool;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;


/**
 * This implementation of the Individual interface is a basic linear fixed size individual
 * where all the gene are instances of the same class.
 * It uses an instance of Gene as prototype, using its newRandomInstance() method to populate the genome.
 *
 * @author Jean-Luc Falcone
 * @version Suppression of genes.
 */
public class VectorIndividual <T> extends AbstractIndividual<T> implements Flushable, Individual<T> {

    private T[] genome;          // The genome
    private GeneFactory<T> genefac;  // The factory
    private int size;            // The genome size
    private ObjectPool<VectorIndividual<T>> pooler;
    private Printer<VectorIndividual<T>> printer;
    private Initializer<VectorIndividual<T>> initializer;

    /**
     * The constructor withe the gene factory
     *
     * @param geneFac
     */
    public VectorIndividual(GeneFactory<T> geneFac, Printer<VectorIndividual<T>> printer, int indSize) {
        this();
        setGeneFactory(geneFac);
        setPrinter(printer);
        setIndSize(indSize);
        objectCreated();
    }

    /**
     * Empty constructor
     */
    public VectorIndividual() {
        initializer = null;
    }

    /**
     * Construct a new instance.
     */
    public void objectCreated() {
        genome = (T[]) Array.newInstance(genefac.getClassGene(), size);
        pooler = new ObjectPool<VectorIndividual<T>>(this);
    }

    @Forced public void setIndSize(int size) {
        this.size = size;
    }

    public void setInitializer(Initializer<VectorIndividual<T>> initializer) {
        this.initializer = initializer;        
    }

    /**
     * Copy constructor.
     *
     * @param vi The VectorIndividual to be copied
     */
    public VectorIndividual(VectorIndividual<T> vi) {
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
        VectorIndividual<T> vi = (VectorIndividual<T>) individual;
        assert size == vi.size;
        System.arraycopy(vi.genome, 0, genome, 0, size);
    }

    /**
     * This method is used to make an empty copy of the current individual. The individual has evrything but genes. When this method
     * is called, most of the type a warning: Unchecked Cast is thrown, <b>DON'T PANIC</b> it's normal.
     *
     * @return a an empty copy of the current individual
     */
    public VectorIndividual<T> emptyCopy() {
        return pooler.get();
    }

    public VectorIndividual<T> copy() {
        VectorIndividual<T> ind = pooler.get();
        ind.copy(this);
        return ind;
    }

    public void copy(int beginSrc, int length, GlobalIndividual<T> src, int beginDest) {
        System.arraycopy(((VectorIndividual<T>) src).genome, beginSrc, genome, beginDest, length);
        informUnsynchronized();
    }


    public void selfRelease() {
        super.selfRelease();
        pooler.release(this);
    }

    public void randomize() {
        if (initializer == null) {
            for (int i = 0; i < size; i++) {
                genome[i] = genefac.newGene(i);
            }
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
    @Forced public void setGeneFactory(GeneFactory<T> geneFactory) {
        genefac = geneFactory;
    }

    public void clear() {
        informUnsynchronized();
    }

    public void deepClean() {
        informUnsynchronized();
    }

    public VectorIndividual<T> newInstance() {
        return new VectorIndividual<T>(this);
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
    public void setPrinter(Printer<VectorIndividual<T>> printer) {
        this.printer = printer;
    }

    public void mutate(int i) {
        genome[i] = genefac.differentValue(i, genome[i]);
        informUnsynchronized();
    }

    public GeneFactory<T> getGeneFactory() {
        return genefac;
    }

    @Override public String toString() {
        if (genome != null)
            return (printer == null) ? ArraysFormatter.dump(genome) : printer.toString(this);
        else
            return "";
    }


    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof VectorIndividual)) {
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
        VectorIndividual ind = (VectorIndividual) obj;
        return Arrays.deepEquals(genome, ind.genome);
    }


    public int computeHashCode() {
        return Arrays.deepHashCode(genome);
    }

    public Iterator<T> iterator() {
        return ArrayIterator.getIterator(genome);
    }
}
