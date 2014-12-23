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
public class If implements Control<Double> {
    public Type getType() {
        return Type.BEGINNER;
    }

    public int minStackSize() {
        return 1;
    }

    public int modStackSize() {
        return -1;
    }

    public void exec(DataStack<Double> dataStack, ExecutionPointer pointer) {
        double value = dataStack.pop();
        if (value < 0) {
            pointer.skipFirst();
        }
        return;
    }

    public boolean isCompatible(Control<Double> control) {
        if (control instanceof Else || control instanceof End) return true;
        return false;
    }

    public String toString() {
        return "if";
    }
}
