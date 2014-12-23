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

import ch.unige.ng.ops.Operator;
import ch.unige.ng.ops.select.Selector;
import ch.unige.ng.species.Individual;
import ch.unige.ng.populations.Population;
import ch.unige.ng.populations.Group;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.gen.Context;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.Flushable;

import java.util.ArrayList;
import java.util.List;

/**
 * Run different operators each, with an equiprobable probability.
 *
 * Each operator has the same probability to occur.
 *
 * @author Jacques Fontignie
 * @version 1 juin 2005
 */
public class SimpleBreeder <G,T extends Individual<G>> implements Operator<T>, Flushable {
    private List<Operator<T>> operatorList;
    private double probs[];

    public SimpleBreeder(Operator<T>... operators) throws Exception {
        this();
        for (Operator<T> operator : operators)
            addOperator(operator);
        objectCreated();
    }

    public void add(double ... x){
        for (int i = 0; i < x.length; i++)
            x[i]++;
    }

    public SimpleBreeder() {
        operatorList = new ArrayList<Operator<T>>();
    }

    @Forced public void addOperator(Operator<T> operator) {
        operatorList.add(operator);
    }

    public void objectCreated() throws Exception {
        probs = new double[operatorList.size()];
    }

    public void operate(Population<T> pop, Selector<T> selector, Context context, int number) {
        //First, we have to estimate the probability of each operator
        double sum = 0;
        for (int i = 0; i < operatorList.size(); i++) {
            double value = context.getRng().nextDouble();
            probs[i] = value;
            sum += value;
        }
        //Go through each operator and create a certain number
        int i = 0;
        int numTreated = 0;
        for (Operator<T> operator : operatorList) {
            if (i < operatorList.size() - 1) {
                int treated = (int) (probs[i] / sum * pop.size());
                if (treated > 0)
                    operator.operate(pop, selector, context, treated);
                numTreated += treated;
            } else if (pop.size() - numTreated > 0)
                operator.operate(pop, selector, context, pop.size() - numTreated);
            i++;
        }
    }

    public void operate(Group<T> group, IGroup<T> pop, Selector<T> selector, Context context) {
        throw new IllegalStateException("Not implemented yet");

    }
}
