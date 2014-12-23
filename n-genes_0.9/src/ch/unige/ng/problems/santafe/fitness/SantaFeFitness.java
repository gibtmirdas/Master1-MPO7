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


package ch.unige.ng.problems.santafe.fitness;

import ch.unige.ng.fitness.GPFitness;
import ch.unige.ng.problems.santafe.context.Board;
import ch.unige.ng.species.Evaluable;
import ch.unige.ng.species.stackGP.DataStack;
import ch.unige.ng.species.stackGP.evaluations.IEvaluator;
import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.tools.Forced;

import java.util.LinkedList;

/**
 * @author Jacques Fontignie
 * @version 10 janv. 2005
 */
public class SantaFeFitness implements GPFitness<Operation>{
    private DataStack<Integer> dts;
    private LinkedList<Integer> list;
    private Board board;
    private IEvaluator evaluator;

    public SantaFeFitness(Board board, IEvaluator evaluator) {
        this();
        setBoard(board);
        setEvaluator(evaluator);
    }

    public SantaFeFitness(){
        dts = new DataStack<Integer>();
         list = new LinkedList<Integer>();
    }

    @Forced public void setBoard(Board board) {
        this.board = board;
    }

    @Forced public void setEvaluator(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public double compute(Evaluable<Operation> ind) {
        dts.clear();        
        dts.push(Board.toInteger(board.getBeginX(),board.getBeginY(),board.getBeginDirection()));
        evaluator.evaluate(dts,ind);
        list.clear();
        int sum = board.getNumFoods();
        for (Integer position : dts){
            int p = Board.getPosition(position);
            if (!list.contains(p)){
                sum -= board.getNumFoods(
                        Board.getX(position),
                        Board.getY(position)
                );
                list.add(p);
            }
        }

        return sum;
    }

    public int getInputSize() {
        return 0;
    }

    public int getOutputSize() {
        return 0;
    }

}
