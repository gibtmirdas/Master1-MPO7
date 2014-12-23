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
import ch.unige.ng.tools.rng.RNG;

//TODO Javadoc !!!

/**
 * @author Jacques Fontignie
 * @version 14 juin 2005
 */
public class RandomBranchDeleter <T extends VariableSize> extends Mutator<T> {
    private double defaultSize;
    private double std;


    public RandomBranchDeleter(double defaultSize, double std) {
        this.defaultSize = defaultSize;
        this.std = std;
    }

    public RandomBranchDeleter() {
    }

    @Forced public void setDefaultSize(double defaultSize) {
        this.defaultSize = defaultSize;
    }

    @Forced public void setStd(double std) {
        this.std = std;
    }

    public void operate(T mate2, Context context) {
        RNG rng = context.getRng();
        int size = mate2.size();
        if (size == 0) return;
        int length = (int) (rng.nextGaussian() * std + defaultSize);
        if (length < 0) return;
        int beginIndex = (size-length <= 0)? 0 :rng.nextInt(size-length);        

        mate2.delete(beginIndex,Math.min(size,
                beginIndex+length));
    }
}
