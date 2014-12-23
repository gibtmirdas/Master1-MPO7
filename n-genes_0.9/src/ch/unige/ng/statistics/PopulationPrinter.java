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


package ch.unige.ng.statistics;

import ch.unige.ng.populations.Population;
import ch.unige.ng.species.GlobalIndividual;

/**
 * @author Jacques Fontignie
 * @version 18 avr. 2005
 */
public class PopulationPrinter<T extends GlobalIndividual> implements Statistic<String,T>{

    public String evaluate(Population<T> population) {
        StringBuffer buffer = new StringBuffer();
        for (T ind : population){
            for (int i = 0, length = ind.size(); i < length; i++){
                buffer.append(ind.get(i));
                buffer.append(" ");
            }
            buffer.append(" ");
        }
        return buffer.toString();
    }

    public String getName() {
        return "Population";
    }
}
