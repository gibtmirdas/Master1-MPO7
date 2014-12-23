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


package ch.unige.ng.ops.mutation;

import ch.unige.ng.gen.Context;
import ch.unige.ng.species.Individual;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.ops.mutation.Mutator;

/**
 * @author Jacques Fontignie
 * @version 12 janv. 2005
 */
public class PriorityMutation <T extends Individual> extends Mutator<T> {

    /**
     * The politic to set the probability of change on each set
     */
    public enum PriorityMode {
        /**
         * If the number of genes is set to n, then the least important gene will have a probability equal to n/sum(1..n).
         */
        NORMAL,

        /**
         * If the number of genes is set to n, then the least important gene will have a probability equal to n*n/sum(1*1,..,n*n).
         */
        SQR
    };

    /**
     * The order in which are set the probabilty
     */
    public enum Order {
        /**
         * The gene with the most probability to mute is at the end
         */
        ASCENDING,

        /**
         * The gene with the most probability to mute is at the begin
         */
        DESCENDING

    }

    private Order order;
    private PriorityMode priorityMode;

    @Forced
            public void setOrder(Order order) {
        this.order = order;
    }

    @Forced
            public void setPriorityMode(PriorityMode priorityMode) {
        this.priorityMode = priorityMode;
    }  

    public void operate(T newInd, Context context) {
        int index = 0;
        int size = newInd.size();
        if (size == 0) return;
        switch (priorityMode) {
            case NORMAL:
                int sum = (size * (size - 1)) / 2;
                int random = context.getRng().nextInt(sum);
                index = (int) ((Math.sqrt(-1 + 8 * random) + 1) / 2);
                break;
            case SQR:
                //TODO A implémenter
            default:
                throw new IllegalStateException("Not implemented yet");
        }

        switch (order) {
            case ASCENDING:
                break;
            case DESCENDING:
                index = newInd.size() - 1 - index;
                break;
        }
        newInd.mutate(index);
    }
}
