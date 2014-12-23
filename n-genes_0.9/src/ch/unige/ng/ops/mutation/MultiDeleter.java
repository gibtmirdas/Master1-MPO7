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
import ch.unige.ng.species.VariableSize;
import ch.unige.ng.tools.Forced;

/**
 * Created by IntelliJ IDEA.
 * User: falcone
 * Date: Sep 10, 2004
 * Time: 4:16:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiDeleter <T extends VariableSize> extends Mutator<T> {

    private double wtRate;

    public MultiDeleter(double mutRate) {
        if (mutRate >= 1.0 || mutRate < 0) {
            throw new IllegalArgumentException("mutRate has to be from 0 to 1 (Not incl.). You gave me: " + mutRate);
        }
        wtRate = 1 - mutRate;
    }

    /**
     * Empty constructor for Parser compliance
     */
    public MultiDeleter() {
    }


    /**
     * Defines the new probability
     *
     * @param mutRate
     */
    @Forced public void setProbability(double mutRate) {
        if (mutRate >= 1.0 || mutRate < 0) {
            throw new IllegalArgumentException("mutRate has to be from 0 to 1 (Not incl.). You gave me: " + mutRate);
        }
        wtRate = 1 - mutRate;
    }

    public void operate(T newInd,  Context context) {
        for (int i = newInd.size(); i >= 0; --i) {
            if (context.getRng().nextDouble() > wtRate) {
                newInd.delete(i);
            }
        }
    }

}
