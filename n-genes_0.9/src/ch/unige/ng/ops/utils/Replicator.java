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

/**
 * Created by IntelliJ IDEA.
 * User: falcone
 * Date: Aug 26, 2004
 * Time: 5:14:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Replicator <G,T extends Individual<G>> implements Operator<T> {
    private final Group<T> group;

    public Replicator() {
        group = new Group<T>();
    }

    public void operate(Population<T> pop, Selector<T> selector, Context context, int number) {
        selector.select(pop, context, group, number);
        for (T ind : group) {
            pop.add((T) ind.copy());
        }
        group.removeAll();
    }

    public void operate(Group<T> group, IGroup<T> destination, Selector<T> selector, Context context) {
        while (!group.isEmpty())
            destination.add(group.remove(0));
    }
}
