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


package ch.unige.ng.species.stackGP.evaluations;

import ch.unige.ng.species.Evaluable;
import ch.unige.ng.species.stackGP.DataStack;
import ch.unige.ng.species.stackGP.ExecutionPointer;
import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.species.stackGP.operations.controls.Control;
import ch.unige.ng.tools.Forced;

/**
 * This class evaluates a genome.
 * <p/>
 * This class can use some controls like the if, the else and the end.
 * <p/>
 * <p/>
 * There are three main groups for control structures:
 * <p/>
 * <ol>
 * <li>Beginner: if, while{ for{...</li>
 * <li> Separator: else </li>
 * <li> Ender: } } while...</li>
 * </ol>
 * There are not many different possible instructions for each structure:
 * <ol>
 * <li>Beginner: We can:
 * <ul>
 * <li> go to the end of the block: negative response to the while</li>
 * <li> skip the first block: negative answer to a else</li>
 * <li> go to the next instruction</li>
 * </ul>
 * </li>
 * <li> Separator: Only on think can be done: go to the end of the structure</li>
 * <li> Ender: We can go to the beginning of the structure: a while or go to the next instruction.</li>
 * </ol>
 * <p/>
 * Then, there is only three method to work on: skip, goEnd or goBegin
 * <p/>
 * These three methods can be done on a single pass.
 * <p/>
 * A main point of this class is that the object has to still work with syntaxically false programs. The idea is
 * to forget a begin object if there is no end. To forget a end if there is no begin and to forget a a skip if
 * the begin has allready skipped.
 *
 * @author Jacques Fontignie
 * @version 8 avr. 2005
 */
public class ControlEvaluator implements IEvaluator {
    private int[] skip = new int[0];
    private int[] end = new int[0];
    private int[] begin = new int[0];
    private boolean[] wellFormed = new boolean[0];


    private ExecutionPointer pointer = new ExecutionPointer();
    private int[] stackPosition = new int[0];
    private Control[] stackControl = new Control[0];
    private boolean[] stackSeparated = new boolean[0];
    private int[] stackSkipper = new int[0];
    private int iterationMax;

    /**
     * For the evaluation we put everything into an array, we do not know how work the evaluable, in that way, we
     * are sure that the evaluation will be fast.
     */
    private Object[] genome;

    @Forced public void setIterationMax(int iterationMax) {
        this.iterationMax = iterationMax;
    }

    public <S,T extends Operation<S>> int evaluate(DataStack<S> stack, Evaluable<T> evaluable) {
        int size = evaluable.size();
        //First: creating the array.
        if (size > skip.length) {
            skip = new int[size];
            end = new int[size];
            begin = new int[size];
            wellFormed = new boolean[size];
            genome = new Object[size];
            stackPosition = new int[size];
            stackControl = new Control[size];
            stackSeparated = new boolean[size];
            stackSkipper = new int[size];
        }

        //Creating the lookup array
        createLookupArrays(size, evaluable);

        int numSteps = 0;
        pointer.init();
        int head = pointer.getHead();
        //The head should be equal to 0
        assert head == 0;
        while (
                head < size && //Check if The head is in the program
                head >= 0 &&
                numSteps++ < iterationMax) { //And we are not too much evaluating (The counter is incremented)

            //If the current instruction is well-formed, we have to execute the function
            if (wellFormed[head]) {
                T gene = evaluable.get(head);
                //If the function is evaluable
                if (stack.size() >= gene.minStackSize())
                    gene.exec(stack, pointer);
                switch (pointer.getControlMode()) {
                    //If nothing was done
                    case NONE:
                        break;

                    case BEGIN://we have to go to the begin of the block
                        pointer.setAbsoluteJump(begin[head]);
                        break;

                    case END://we have to go to the end of the block
                        pointer.setAbsoluteJump(end[head]);
                        break;

                    case SKIP://we have to skip the pointer to the first skipper.
                        pointer.setAbsoluteJump(skip[head]);
                        break;
                }
            }

            //Update the pointer, if the instruction was not well formed, the head is incremented
            pointer.next();
            //getting the position of the head
            head = pointer.getHead();
        }
        return numSteps;
    }

    /**
     * Creates a lookup array
     *
     * @param size
     * @param evaluable
     */
    private <T> void createLookupArrays(int size, Evaluable<T> evaluable) {

        T array [] = (T[]) genome;

        int indexStack = -1;
        int indexSkip = -1;

        //Now we have to create the link array.
        for (int i = 0; i < size; i++) {
            array[i] = evaluable.get(i);
            T currentOperation = array[i];
            //We reference on the next instruction.
            skip[i] = i + 1;
            end[i] = i + 1;
            begin[i] = i + 1;
            wellFormed[i] = true;
            if (currentOperation instanceof Control) {
                //by default a control is bad formed, if we found a end that matches with a begin, this one is
                // considered as well-formed and the skip function too.
                wellFormed[i] = false;
                Control control = (Control) currentOperation;
                switch (control.getType()) {
                    case BEGINNER:
                        //If we have a new beginner, we add it into the stack
                        indexStack++;
                        stackPosition[indexStack] = i;
                        stackSeparated[indexStack] = false;
                        stackControl[indexStack] = control;
                        break;
                    case SEPARATOR:
                        //If we have a separator, we have to check if it is well formed
                        if (indexStack >= 0 && //There is allready a begin created
                                !stackSeparated[indexStack] && //The begin has not allready a separator.
                                stackControl[indexStack].isCompatible(control)) { //the separator is compatible with the beginner
                            stackSeparated[indexStack] = true;
                            //The skip  pointer of the beginner references to the current instruction+1.
                            skip[stackPosition[indexStack]] = i + 1;
                            indexSkip++;
                            stackSkipper[indexSkip] = i;
                        }
                        break;
                    case ENDER:
                        //We have an end control structure.
                        if (indexStack >= 0 && stackControl[indexStack].isCompatible(control)) {
                            //The end of the current beginner
                            end[stackPosition[indexStack]] = i + 1;
                            begin[i] = stackPosition[indexStack];
                            if (indexSkip >= 0) {
                                skip[stackSkipper[indexSkip]] = i;
                                indexSkip--;
                            }
                            wellFormed[i] = true;
                            wellFormed[stackPosition[indexStack]] = true;
                            //If the begin is separated we have to inform the separator that it is well formed:
                            if (stackSeparated[indexStack]) {
                                wellFormed[skip[indexStack]] = true;
                            }
                            indexStack--;
                        }
                        break;
                }
            }
        }
    }


}
