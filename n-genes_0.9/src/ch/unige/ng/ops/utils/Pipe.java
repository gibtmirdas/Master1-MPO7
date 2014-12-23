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
import ch.unige.ng.tools.Flushable;
import ch.unige.ng.tools.Forced;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jacques Fontignie
 * @version 11 mars 2005
 */
public class Pipe <G,T extends Individual<G>> implements Operator<T>, Flushable {

    private Group<T> group;
    private final List<Operator<T>> operators;
    private final Group<T> tempGroup;

    public Pipe(List<Operator<T>> operators){
        this();
        this.operators.addAll(operators);
    }


    public Pipe() {
        operators = new LinkedList<Operator<T>>();
        tempGroup = new Group<T>();
        group = new Group<T>();
    }

    @Forced public void addOperator(Operator<T> operator) {
        operators.add(operator);
    }

    public void objectCreated() throws Exception {
        if (operators.size() <= 1)
            throw new RuntimeException("There is not enough operators, there must be at least two operators");
    }


    public void operate(Population<T> population, Selector<T> selector, Context context, int number) {
        tempGroup.clear();
        selector.select(population, context, tempGroup, number);
        tempGroup.selfCopy();
        operate(tempGroup, population, selector, context);
    }

    public void operate(Group<T> source, IGroup<T> destination, Selector<T> selector, Context context) {
        Group<T> temp;
        final Group<T> original = group;
        for (int i = 0; i < operators.size(); i++) {
            Operator<T> operator = operators.get(i);
            if (i == operators.size() - 1) {
                operator.operate(source, destination, selector, context);
                source.clear();
            } else {
                operator.operate(source, group, selector, context);
                temp = source;
                source.clear();
                source = group;
                group = temp;
            }
        }
        group = original;
        assert group.size() == 0;
    }
}
