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


package ch.unige.ng.species.printers;

import ch.unige.ng.species.GlobalIndividual;

/**
 * SubClasses have to return some informations about the individual. This method is called by the toString method of
 *  the individual. If you want to print a boolean Individual that is coded in gray. You can use a Printer to retrun the
 * individual into normal coding
 *
 * @author Jacques Fontignie
 * @version 15 avr. 2005
 */
public interface Printer<T extends GlobalIndividual> {

    /**
     *
     * @param ind
     * @return the individual into the string format
     */
    public String toString(T ind);
}
