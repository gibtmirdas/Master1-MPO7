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


package ch.unige.ng.problems.santafe.operations;

import ch.unige.ng.problems.santafe.context.Board;
import ch.unige.ng.species.stackGP.DataStack;
import ch.unige.ng.species.stackGP.ExecutionPointer;
import ch.unige.ng.species.stackGP.operations.controls.Control;
import ch.unige.ng.species.stackGP.operations.controls.Else;
import ch.unige.ng.species.stackGP.operations.controls.End;
import ch.unige.ng.tools.Forced;

/**
 * @author Jacques Fontignie
 * @version 18 avr. 2005
 */
public class IfFoodAhead implements Control<Integer> {

    private Board board;

    public IfFoodAhead(Board board) {
        this();
        setBoard(board);        
    }

    public IfFoodAhead() {
    }

    @Forced public void setBoard(Board problem) {
        this.board = problem;
    }

    public void exec(DataStack<Integer> dataStack, ExecutionPointer pointer) {
        Integer p = dataStack.peek();
        int x = Board.getX(p);
        int y = Board.getY(p);
        Board.Direction direction = Board.getDirection(p);
        switch (direction) {
            case NORTH:
                y--;
                break;
            case SOUTH:
                y++;
                break;
            case EAST:
                x++;
                break;
            case WEST:
                x--;
                break;
        }
        x = (x + board.getWidth()) % board.getWidth();
        y = (y + board.getHeight()) % board.getHeight();
        if (!board.containsFood(x,y)) pointer.skipFirst();
    }

    public Type getType() {
        return Type.BEGINNER;
    }

    public boolean isCompatible(Control<Integer> control) {
        return (control instanceof Else || control instanceof End);
    }

    public int minStackSize() {
        return 1;
    }

    public int modStackSize() {
        return 0;
    }

    public String toString() {
        return "IF_FOOD_AHEAD";
    }
}
