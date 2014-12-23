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


package ch.unige.ng.timer;

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.select.Selector;
import ch.unige.ng.populations.Population;
import ch.unige.ng.species.GlobalIndividual;

/**
 * n-genes
 * <p/>
 * This class is used to inform that this method has to do something.
 * <p/>
 * There are two different events that can be used: print or work, each is called, when some conditions
 * are available. The print work is called when there is something to display.
 * <p/>
 * And the work is used for example, whene there are some datas to fetch: The best example is if you want to take
 * the mean of the fitness during n steps (n is defined in the xml file) and after x steps, you print the value.
 *
 * @author Jacques Fontignie
 * @version 15 dec. 2004
 */
public interface GeneticLogger <T extends GlobalIndividual> {

    /**
     * This method is called to inform the object that there is something to print.
     * It is just a method to print some data.
     *
     * @param population
     */
    public void firePrintEvent(Population<T> population, Selector<T> selector, Context context);

    /**
     * This method is called to inform the listener that the algorithm is finished
     */
    void finish();

}
