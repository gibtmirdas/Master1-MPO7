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

package ch.unige.ng.ops.permutables;

import ch.unige.ng.species.Permutable;
import ch.unige.ng.ops.crossover.DoubleOffspringCrossOver;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.gen.Context;

/**
 * @author Fontignie Jacques
 * @version 6 aug. 2005
 */
public class SwapCrossover<S,T extends Permutable<S>> extends DoubleOffspringCrossOver<T> {

    public void twoOffspringCrossover(T mate1, T mate2, IGroup<T> destination, Context context) {
        int indSize = mate1.size();

        int crossPoint = context.getRng().nextInt(indSize - 1) + 1;
        //Impossible to avoid easily this warning
        T offspring1 = (T) mate1.emptyCopy();
        T offspring2 = (T) mate2.emptyCopy();

        offspring1.copy(crossPoint, indSize - crossPoint, mate2, crossPoint);

        offspring2.copy(0, crossPoint, mate2, 0);
        offspring2.copy(crossPoint, indSize - crossPoint, mate1, crossPoint);
        offspring1.repair();
        offspring2.repair();
        destination.add(offspring1);
        destination.add(offspring2);
    }

    public void oneOffspringCrossover(T mate1, T mate2, IGroup<T> destination, Context context) {
        int indSize = mate1.size();

        int crossPoint = indSize / 2;

        //Impossible to avoid easily this warning
        T offspring = (T) mate1.emptyCopy();
        offspring.copy(0, crossPoint, mate1, 0);
        offspring.copy(crossPoint, indSize - crossPoint, mate2, crossPoint);
        offspring.repair();
        destination.add(offspring);
    }
}
