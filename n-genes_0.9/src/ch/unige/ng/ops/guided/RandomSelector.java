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
import ch.unige.ng.species.Individual;

/**
 * Select Individuals at Random.
 */
public class RandomSelector<T extends Individual> implements GuidedSelector<T> {


    public T select(IGroup<T> p, Context context) {
        int index = context.getRng().nextInt(p.size());
        return p.get(index);
    }

    public void select(IGroup<T> population, Context context, T[] ts, int number, SelectionEvaluator<T> selectionEvaluator) {
        for (int i = 0; i < number; i++)
            ts[i] = select(population,context);
    }
}
