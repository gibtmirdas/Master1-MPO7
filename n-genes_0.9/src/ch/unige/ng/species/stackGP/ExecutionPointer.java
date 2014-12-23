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


package ch.unige.ng.species.stackGP;

/**
 * This class contains the position of the head in the program.
 *
 * @author Jacques Fontignie
 * @version 8 avr. 2005 Rajout des structures de controle.
 */
public class ExecutionPointer <T> {

    /**
     * The head of the program
     */
    private int head;

    /**
     * If the head has been modified during the current execution
     */
    private boolean modified;

    /**
     * The control used.
     */
    public enum ControlMove {
        NONE,
        SKIP,
        BEGIN,
        END;
    };

    private ControlMove controlMoveMove = ControlMove.NONE;

    /**
     * @return the position at the end
     */
    public int getHead() {
        return head;
    }

    /**
     * Inform the pointer it has to skip the first element
     */
    public void skipFirst() {
        modified = true;
        controlMoveMove = ControlMove.SKIP;
    }

    /**
     * Goes to the begin node.
     */
    public void goBegin() {
        modified = true;
        controlMoveMove = ControlMove.BEGIN;
    }

    /**
     * Goes to the end
     */
    public void goEnd() {
        modified = true;
        controlMoveMove = ControlMove.END;
    }

    /**
     * @return the last control Node used.
     */
    public ControlMove getControlMode() {
        ControlMove current = controlMoveMove;
        controlMoveMove = ControlMove.NONE;
        return current;
    }

    /**
     * Update the head
     */
    public void next() {
        if (!modified) head++;
        modified = false;
    }

    /**
     * Initialzes the state
     */
    public void init() {
        head = 0;
        modified = false;
        controlMoveMove = ControlMove.NONE;
    }

    /**
     * Sets the head considering we have a relative jump: head += displacement
     *
     * @param displacement
     */
    public void setRelativeJump(int displacement) {
        head += displacement;
        modified = true;
    }

    /**
     * Sets the new value considering we have an absolute jump: head = position
     *
     * @param position
     */
    public void setAbsoluteJump(int position) {
        head = position;
        modified = true;
    }
}
