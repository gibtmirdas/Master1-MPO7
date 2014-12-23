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

import ch.unige.ng.species.stackGP.DataStack;
import ch.unige.ng.species.stackGP.ExecutionPointer;

/**
 * @author Jacques Fontignie
 * @version 8 avr. 2005
 */
public class End<T extends Object> implements Control<T> {
    public Type getType() {
        return Type.ENDER;
    }


    public int minStackSize() {
        return 0;
    }

    public int modStackSize() {
        return 0;
    }

    public void exec(DataStack<T> dataStack, ExecutionPointer pointer) {
    }

    public boolean isCompatible(Control<T> control) {
        return (control instanceof Else || control instanceof If);
    }

    public String toString() {
        return "end";
    }
}

