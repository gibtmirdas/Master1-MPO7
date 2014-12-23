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
import ch.unige.ng.tools.Flushable;
import ch.unige.ng.tools.Forced;
import static java.lang.Math.min;

/**
 * @author Jacques Fontignie
 * @version 7 janv. 2005
 */
public class MultiplePointMutation <T extends Individual> extends Mutator<T> implements Flushable {

    private MutationMode mutationMode;
    private int toMutate = -1;

    /**
     * Used to determine the mutation mode
     */
    public enum MutationMode {
        /**
         * The mutation is done on a given number of genes
         */
        FIXED,

        /**
         * The muitation si done on a given number of genes that are selected as contiguous
         */
        FIXED_CONTIGUOUS
    };


    public MultiplePointMutation() {
        super();
    }


    public void objectCreated() throws Exception {
        switch (mutationMode) {
            case FIXED:
            case FIXED_CONTIGUOUS:
                if (toMutate < 0) throw new IllegalStateException("The setToMutate method has not been called");
                break;
        }
    }

    /**
     * Sets the number of gene to mutate
     *
     * @param number
     */

    public void setToMutate(int number) {
        this.toMutate = number;
    }


    @Forced public void setMutationMode(MutationMode mutationMode) {
        this.mutationMode = mutationMode;
    }

    public void operate(T newInd, Context context) {
        switch (mutationMode) {
            case FIXED:
                for (int i = 0; i < toMutate; i++)
                    newInd.mutate(context.getRng().nextInt(newInd.size()));
                break;
            case FIXED_CONTIGUOUS:
                int maxValue = newInd.size() - toMutate;
                int position;
                if (maxValue > newInd.size())
                    position = 0;
                else
                    position = context.getRng().nextInt(maxValue);
                for (int i = min(newInd.size(), position + toMutate); i >= position; i--)
                    newInd.mutate(i);
                break;
        }
    }
}
