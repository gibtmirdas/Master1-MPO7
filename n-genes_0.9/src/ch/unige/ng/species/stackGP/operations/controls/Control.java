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


package ch.unige.ng.species.stackGP.operations.controls;

import ch.unige.ng.species.stackGP.operations.Operation;

/**
 * @author Jacques Fontignie
 * @version 8 avr. 2005
 */
public interface Control <T> extends Operation<T> {

    /**
     * The three different type for a control.
     */
    public enum Type {
        BEGINNER,
        SEPARATOR,
        ENDER;
    }

    /**
     *
     * @return the type of the control
     */
    public Type getType();

    /**
     * @param control
     * @return if the control is compatible with the current control. We can not use a else with a while. This function is
     * used to determine which ControlMove works with which one.
     */
    public boolean isCompatible(Control<T> control);
}
