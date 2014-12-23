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
import ch.unige.ng.species.VariableSize;
import ch.unige.ng.tools.CachingObject;
import ch.unige.ng.tools.Forced;

import java.io.PrintWriter;
import java.util.*;

/**
 * This class defines a population with some caching functions. A cache size is given. The population will keep into
 * memory all the individuals, until there is no more space available.
 * <p/>
 * When individuals are added, the object check if an equivalent object is in the cache. If it does, the object
 * (in memory) will be put in memory.
 * This way can have two advantages:
 * <ul>
 * <li> The same individual is present only one time: there are multiple references on it.</li>
 * <li> We can hope that an individual that is in the cache has allready calculated its fitness. The algorithm
 * can maybe go much faster. </li>
 * </ul>
 * <p/>
 * The problem is that this cache only works with deterministic fitness. If you want to calculate each time the fitness,
 * this object will not be usefull
 * <p/>
 * The cache will grow if the number of individual are bigger than the limited size. The goal is that the cache has
 * to contain at least the number of individuals in the current and in the next population and some other values. It avoid some border
 * effects. The goal is to be sure that there is at least max(2*currentPop,currentPop+creatingPop)*4./3. This idea is used to be sure
 * that the cache will still work with a increasing population
 *
 * @author Jacques Fontignie
 * @version 5 avr. 2005
 *          <p/>
 *          TODO GERER LE PROBLEME DE TAILLE variable
 */
public class CachingPopulation <T extends GlobalIndividual> implements CachingObject,Population<T> {
    private List<T> currentPopulation;
    private List<T> creatingPopulation;

    /**
     * The minimal size of the cache, we could at least have MIN_SIZE_THRESHOLD of the population size.
     * It is the currentPopulation + the creatingPopulation + some rest to
     * enhance the cache hits
     */
    private static final double MIN_SIZE_THRESHOLD = 10. / 3;

    /**
     * When a clean function is called, the cache will be deleted to the 2/3 of the maximal size
     */
    private static final double CACHE_SIZE_DELETION = 3. / 4;

    /**
     * After a cache cleaning, if the size is still too big the cache will be entirely deleted
     */
    private static final double CACHE_TOO_BIG = 4. / 5;

    /**
     * A hashmap containing the indiviaduals
     */
    private HashMap<T, ContainerCache<T>> cachedIndividuals;

    /**
     * The number of cacheHit that we had
     */
    private int cacheHit;

    /**
     * The maximum number of individuals
     */
    private int maxSize;


    // An object storing computed measurements
    private StatisticCache<T> sCache;

    // The Current Iteration
    private int step;
    private long cacheSize;

    /**
     * The constructor
     *
     * @param cacheSize determined by a string.
     * @see CachingPopulation#setCacheSize(String)
     */
    public CachingPopulation(String cacheSize) {
        this();
        setCacheSize(cacheSize);
    }

    public CachingPopulation() {
        cacheHit = 0;
    }

    /**
     * The cache Size used. The value has to be in the following form:
     * <i> [0-9]+[KMG] </i>
     * <p/>
     * If you want to give 10 MegaBytes to in the cache, you have to write 10M
     *
     * @param cacheSize
     */
    @Forced public void setCacheSize(String cacheSize) throws IllegalArgumentException {
        String line = cacheSize.toUpperCase();
        int size = line.length();
        char power = line.charAt(size - 1);
        long mulCoeff = 1;
        switch (power) {
            case 'G':
                mulCoeff *= 1024;                
            case 'M':
                mulCoeff *= 1024;
            case 'K':
                mulCoeff *= 1024;
                break;
            default:
                throw new IllegalArgumentException("The cache size has to be in format: [0-9]+[KMG]");
        }
        long numUnits = Integer.valueOf(line.substring(0, size - 1));
        if (numUnits <= 0) throw  new IllegalArgumentException("The number has to be greater than 0");
        this.cacheSize = numUnits * mulCoeff;
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
        Runtime rt = Runtime.getRuntime();
        long begin = rt.totalMemory() - rt.freeMemory();
        HashMap<T, ContainerCache<T>> temp = new HashMap<T, ContainerCache<T>>();
        int currentNodes = 0;
        for (int i = 0; i < size; i++) {
            T newInd = (T) prototype.emptyCopy();
            newInd.randomize();
            currentNodes += newInd.size();
            if (temp.containsKey(newInd)) {
                //we get the object in the cache
                ContainerCache<T> cc = temp.get(newInd);
                //There is a new object pointing on the individual
                cc.addReference();
                //we add the object in the population.
                currentPopulation.add(cc.getObject());
            } else {
                temp.put(newInd, new ContainerCache<T>(newInd));
                currentPopulation.add(newInd);
            }
        }
        long end = rt.totalMemory() - rt.freeMemory();
        //The observed size of the individuals

        long observedSize;
        if (prototype instanceof VariableSize) {
            //Get The maximal size
            int maxIndividualSize = ((VariableSize) prototype).getMaxSize();
            //Calculate the observedSize. It is calculated in sort that it gives the size that it would have
            // if the individuals were full
            observedSize = (((end - begin) / size) * (maxIndividualSize * size)) / (currentNodes);
        } else
            observedSize = (end - begin) / size;
        observedSize = (observedSize > 1) ? observedSize : 1;
        maxSize = (int) (cacheSize / observedSize);
        if (maxSize < size * MIN_SIZE_THRESHOLD)
            throw new IllegalArgumentException("You have defined a too small cache: we can only cache " +
                    maxSize + " individuals");


        //We create now the individual with the good size.
        cachedIndividuals = new HashMap<T, ContainerCache<T>>(maxSize);
        cachedIndividuals.putAll(temp);
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
        return currentPopulation.get(i);
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
     * Add an array of Individual to the OldPopulation.
     *
     * @param indArray The Individuals to be added.
     */
    public void add(T[] indArray) {
        for (T ind : indArray) {
            add(ind);
        }
    }

    /**
     * This method is equivalent to the nextStep, but this does not change the value step.
     * The idea is that it is possible to make some work on the population, like cleaning
     * the population, or applying an hill climbing
     */
    public void commit() {
        List<T> tempList = currentPopulation;
        currentPopulation = creatingPopulation;
        creatingPopulation = tempList;
        sCache.invalidate();
        freeNextPopulation();
    }

    /**
     * Add an Individual to the population, the change are commited only after
     * a call to the method nextStep.
     *
     * @param individual
     */
    public void add(T individual) {
        assert individual != null;
        creatingPopulation.add(createIndividual(individual));
    }

    private T createIndividual(T individual) {
        if (cachedIndividuals.containsKey(individual)) {
            ContainerCache<T> cc = cachedIndividuals.get(individual);
            cacheHit++;
            cc.addReference();
            T newInd = cc.getObject();
            individual.selfRelease();
            return newInd;
        } else {
            addToCache(individual);
            return individual;
        }
    }

    private void addToCache(T individual) {
        //If we have the cache that is full, we have to make a garbage of the elements
        if (cachedIndividuals.size() > maxSize) {
            cleanCache();
        }
        cachedIndividuals.put(individual, new ContainerCache<T>(individual));
    }

    private List<T> tempList = new ArrayList<T>();

    private void cleanCache() {
        for (ContainerCache<T> cc : cachedIndividuals.values()) {
            if (cc.getReference() == 0) {
                tempList.add(cc.getObject());
            }
        }
        int size = cachedIndividuals.size();
        //we want to suppress until there is only endSize elements.
        int endSize = (int) (maxSize * CACHE_SIZE_DELETION);
        for (T ind : tempList) {
            cachedIndividuals.remove(ind);
            ind.selfRelease();
            if (--size == endSize) break;
        }
        tempList.clear();
        //If there is still no place in the cache, we will supress everything.
        //We do not allow to have: size > maxSize, because it is a  quite long operation
        if (size > maxSize * CACHE_TOO_BIG)
            cachedIndividuals.clear();
    }


    /**
     * This method frees the next population
     */
    private void freeNextPopulation() {
        for (T ind : creatingPopulation) {
            //Warning an individual is maybe no more into the cache but can be in the population
            if (cachedIndividuals.containsKey(ind)) {
                ContainerCache<T> cc = cachedIndividuals.get(ind);
                //we remove a reference
                if (cc.getReference() > 0)
                    cc.removeReference();
            } else if (!currentPopulation.contains(ind))
                ind.selfRelease();
        }
        creatingPopulation.clear();
    }

    public int getCacheHit() {
        return cacheHit;
    }

    /**
     * Increments the iteration number and commits all changes to the population
     *
     * @return the new iteration number
     */
    public int nextStep() {
        List<T> tempList = currentPopulation;
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
     * Return an iterator of the OldPopulation.
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
                /* An comparator based on the fitness */
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
        T ind = currentPopulation.remove(index);
        //If the key is in the map or in the current or the next population, we have to return a copy and not the original
        if (cachedIndividuals.containsKey(ind) || currentPopulation.contains(ind) || creatingPopulation.contains(ind)) {
            if (cachedIndividuals.containsKey(ind))
                cachedIndividuals.get(ind).removeReference();
            return (T) ind.copy();
        } else {
            //We return the individual itself, because there exist no individuals like this one.
            return ind;
        }
    }

    public boolean isEmpty() {
        return currentPopulation.isEmpty();
    }

    public int getCacheSize() {
        return cachedIndividuals.size();
    }

    /**
     * This class is used to determine to get the last use of an individial if there is no more reference on it and
     * the individual was never used we can supress it from the cache
     * <p/>
     * There is no use to implement this class as a poolable object because its size is small enough to be
     * garbaged faster.
     */
    private class ContainerCache <T> {
        private T object;
        private int reference;

        public ContainerCache(T individual) {
            this.object = individual;
            reference = 1;
        }

        public T getObject() {
            return object;
        }


        public void addReference() {
            reference++;
        }

        public void removeReference() {
            reference--;
        }

        /**
         * @return the number of reference on the object.
         */
        public int getReference() {
            return reference;
        }
    }
}
