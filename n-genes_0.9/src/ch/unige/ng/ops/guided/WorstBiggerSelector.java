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


package ch.unige.ng.ops.guided;

import ch.unige.ng.gen.Context;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.species.VariableSize;
import ch.unige.ng.tools.Forced;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This selector takes from the x% worst individuals the bigger ones. This algorithm has some effect on the population
 * A sort  is done, thus, the result will not be the same with or without this selector
 *
 *
 * @author Jacques Fontignie
 * @version 7 juin 2005
 */
public class WorstBiggerSelector <T extends VariableSize> implements GuidedSelector<T> {

    private final Comparator<T> COMPARATOR = new Comparator<T>() {
        public int compare(T mate1, T mate2) {
            return mate2.size() - mate1.size();
        }
    };
    private double worstPercentage;
    private ArrayList<T> list;

    public WorstBiggerSelector(double worstPercentage) {
        this();
        setWorstPercentage(worstPercentage);
    }

    public WorstBiggerSelector() {
        list = new ArrayList<T>();
    }

    @Forced public void setWorstPercentage(double worstPercentage) {
        this.worstPercentage = worstPercentage;
    }

    public void select(IGroup<T> population, Context context, T[] ts, int number, SelectionEvaluator<T> selectionEvaluator) {
        //Sorts the population.
        population.sort();
        int length = population.size();
        int numWorst = (int) (length * worstPercentage);
        list.clear();
        for (int i = 0, index = length - 1; i < numWorst; i++, index--) {
            list.add(population.get(index));
        }
        Collections.sort(list, COMPARATOR);
        for (int i = 0; i < number; i++)
            ts[i] = list.get(i);
    }
}
