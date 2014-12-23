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


package ch.unige.ng.ops.utils;

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.Operator;
import ch.unige.ng.ops.select.Selector;
import ch.unige.ng.populations.Group;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.populations.Population;
import ch.unige.ng.species.Individual;
import ch.unige.ng.tools.ArrayReader;
import ch.unige.ng.tools.ArraysFormatter;
import ch.unige.ng.tools.Flushable;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.rng.RNG;

import java.util.LinkedList;
import java.util.List;
import java.lang.reflect.Array;

/**
 * This class combine different Operators.
 * The Operators are choosen at random according to a probability table.
 *
 * @author Jean-Luc Falcone
 * @version Sep 22, 2004 - 11:11:00 AM
 */
public class Breeder <T extends Individual> implements Operator<T>, Flushable {

    private Operator<T>[] operators;
    private double[] probs;

    private int numSelected [];
    private double[] currentProb;

    private List<Operator<T>> listOperator;
    public static final String SEPARATOR = ";";

    private Group<T> group;


    /**
     * Unique Constructor.
     * The user has to provide an Operator Array and a double array representing the
     * probabilities of each Operator. The consistency of the arguments is checked.
     *
     * @param _operators The Operator array.
     * @param _probs     The probability array.
     */
    public Breeder(List<Operator<T>> _operators, double[] _probs) {
        this();
        listOperator = _operators;
        probs = _probs;
        objectCreated();
    }

    /**
     * The user has to provide an Operator Array and a double array representing the
     * probabilities of each Operator. The consistency of the arguments is checked.
     */
    public Breeder() {
        listOperator = new LinkedList<Operator<T>>();
        group = new Group<T>();
    }

    /**
     * This method is called by the parser when all the element are read
     */
    public void objectCreated() {
        operators = (Operator<T>[]) Array.newInstance(Operator.class,probs.length);
        for (int i = 0; i < probs.length; i++) {
            operators[i] = listOperator.get(i);
        }

        //There is no more need to use it
        listOperator = null;

        numSelected = new int[probs.length];
        currentProb = new double[probs.length];
        //Check the consistency
        checkConsistency();
    }

    /**
     * Adds a new operator
     *
     * @param o
     */
    @Forced public void addOperator(Operator<T> o) {
        listOperator.add(o);
    }

    /**
     * @param probabilities The string representing the probability of each operator. This object is defined as a
     *                      double separated with SEPARATOR     
     */
    @Forced public void setProbabilities(String probabilities) {
        probs = ArrayReader.createDoubleArray(probabilities, SEPARATOR);
    }


    private void checkConsistency() {
        if (probs.length == 0 || operators.length == 0 || probs.length != operators.length) {
            throw new IllegalArgumentException("Illegal sizes. Operators.length=" + operators.length
                    + " Probs.length=" + probs.length);
        }
        double sum = 0;
        for (double p : probs) {
            if (p < 0 || p > 1) {
                throw new IllegalArgumentException("The probs have to be between 0 and 1. You gave me: " +
                        ArraysFormatter.dump(probs));
            }
            sum += p;
        }
        if (sum < 1 - (probs.length * Double.MIN_VALUE)) {
            throw new IllegalArgumentException("The probs do not sum to 1: " +
                    ArraysFormatter.dump(probs));
        }
    }


    /**
     * Calculates the number of elements that each operator has to create
     */
    private void calculateNumber(Context context, int number) {
        RNG rng = context.getRng();
        int size = probs.length;
        //We have to increment number because we want exactly number elements and not number-1
        double sum = 0;
        //total is used for debugging
        int total = 0;
        //Calculate a random number depending on the probability of the ith operator
        for (int i = 0; i < size; i++) {
            currentProb[i] = probs[i] * rng.nextDouble();
            sum += currentProb[i];
        }
        //We have to iterate to have the sum equal to 1.
        for (int i = 0; i < size-1; i++) {
            int numberSelected = (int) (currentProb[i] / sum * (number + 1));
            total += numberSelected;
            numSelected[i] = numberSelected;
        }
        numSelected[size-1] = number-total;
        if (total > number) {
            throw new IllegalStateException("UH OH we have created too many individuals");
        }
    }

    public void operate(Population<T> pop, Selector<T> selector, Context context, int number) {
        calculateNumber(context, number);
        for (int i = 0; i < numSelected.length; i++) {
            int numberSelected = numSelected[i];
            if (numberSelected > 0)
                operators[i].operate(pop, selector, context, numberSelected);
        }
    }


    public void operate(Group<T> source, IGroup<T> destination, Selector<T> selector, Context context) {
        calculateNumber(context, source.size());
        for (int i = 0; i < operators.length; i++) {
            //We add the good number of individuals into the group
            for (int j = 0,length = numSelected[i]; j < length; j++){
                group.add(source.remove(0));
            }
            operators[i].operate(group, destination, selector, context);
            group.clear();
        }
    }
}
