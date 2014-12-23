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


package ch.unige.ng.species.stackGP.operations;

import ch.unige.ng.species.stackGP.DataStack;
import ch.unige.ng.species.stackGP.ExecutionPointer;

/**
 * This Interface represent the instuction used with the StackGP.
 * Each Operation can be applied on a DataStack.
 * @see DataStack
 * @author JL Falcone
 *
 * @version The function exec has been chanegd to work with jumps
 */
public interface Operation<T> {

    /**
     * Executes the instruction on a Datastack and gives the offset applied to the
     * execution head. For simple functions this call will just modify the stack and return modStackSize()
     *
     * @param stack the working stack
     * @param pointer the pointer of the current execution     
     */
    public void exec(DataStack<T> stack, ExecutionPointer pointer);

    /**
     * Returns the number of operands needed by the operator to succeed.
     * (ie. the minimal stack size)
     * @return the number of needed operands
     */
    public int minStackSize();

    /**
     * This method returns an int corresponding to the stack size modification, if the
     * Operation is applied on a Stack. For example a "plus" operations will decrease
     * the stack by one, so the method will return -1.
     * @return the stack size modification.
     */
    public int modStackSize();
    
    /**
     * Return the name of the operations.
     * @return the operations name.
     */
    @Override public String toString();
}
