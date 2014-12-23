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
 * Created by IntelliJ IDEA.
 * User: falcone
 * Date: Sep 6, 2004
 * Time: 5:58:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Dupl implements Operation<Object> {

    private final static int MIN_SIZE = 1;

    public void exec(DataStack<Object> stack,ExecutionPointer pointer) {
        stack.push(stack.peek());
    }

    public int minStackSize() {
        return MIN_SIZE;
    }

    public int modStackSize() {
        return +1;
    }

    public String toString() {
        return "DUPL";
    }
    
}
