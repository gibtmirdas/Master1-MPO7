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


package ch.unige.ng.halt;

import ch.unige.ng.gen.Context;
import ch.unige.ng.populations.Population;
import ch.unige.ng.tools.Forced;

import java.util.LinkedList;

/**
 * Makes a logical and between the different Halt Functions
 * <p/>
 * By default the algorithm will use a logical OR between the different haltFunction. In other term,
 * if one function says that is the end of the algorithm, the algorithm will be terminated.
 * With this class, <b> ALL </b> the halt function have to be fired to terminated the algorithm
 *
 * @author Jacques Fontignie
 * @version 17 dec. 2004
 */
public class AndFunction implements HaltFunction {
    private LinkedList<HaltFunction> functions;

    public AndFunction() {
        functions = new LinkedList<HaltFunction>();
    }

    /**
     * Adds a new haltFunction to the list. A And will be done on all the functions that are given.
     *
     * @param function
     */
    @Forced public void addFunction(HaltFunction function) {
        functions.add(function);
    }

    public boolean isTerminated(Population population, Context context) {
        for (HaltFunction function : functions) {
            if (!function.isTerminated(population, context))
                return false;
        }
        return true;
    }
}
