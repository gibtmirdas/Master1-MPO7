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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represent a working stack used to getNumVisited a StackGPIndividual.
 * It containsFood the inputs (terminals) values. The result(s) of the evaluation
 * are on top of the Stack.
 *
 * @author JL Falcone
 *
 * @version Using a LinkedList uses a lot of memory: When you add an element on a linkedList, it creates a new object
 */
public class DataStack <T> implements Iterable<T> {

    private List<T> stack;
    private T[] inputs;

    /**
     * Basic empty constructor.
     */
    public DataStack() {
        stack = new ArrayList<T>();
    }

    /**
     * Set the inputs of the problem. Must be used before
     * evaluation.
     *
     * @param _inputs
     */
    public void setInputs(T... _inputs) {
        inputs = _inputs;
        stack.clear();
    }
    
    /**
     * Return a reference to the top element of the Stack without removing it
     *
     * @return the top element
     */
    public T peek() {
        return stack.get(stack.size()-1);
    }

    /**
     * @param offset
     * @return the object at position <code> peek - offset</code>
     */
    public T peek(int offset) {
        return stack.get(stack.size() - 1 - offset);
    }

    /**
     * Returns and remove the top element of the Stack
     *
     * @return the top element
     */
    public T pop() {
        return stack.remove(stack.size()-1);
    }

    /**
     * Add an element on the top of the stack.
     * <p/>
     * push(x,y,z) <==> push(x); push(y); push(z)
     *
     * @param list
     */
    public void push(T... list) {
        for (T t : list)
            push(t);
    }

    /**
     * Add an element on the top of the stack.
     *
     * @param t
     */
    public void push(T t) {
        if (t == null) throw new IllegalStateException("ici");
        stack.add(stack.size(),t);
    }

    /**
     * Returns the number of elements in the stack
     *
     * @return the stack size
     */
    public int size() {
        return stack.size();
    }

    /**
     * Returns the ith input parameter
     *
     * @param i the rank of the input
     * @return the ith input value.
     */
    public T getInput(int i) {
        return inputs[i];
    }

    /**
     * Erase the stack.
     */
    public void clear() {
        stack.clear();
    }

    /**
     * Remove the bottom element and  push it on the top of the stack
     */
    public void rotate() {
        T top = stack.remove(stack.size()-1);
        stack.add(0,top);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("| ");
        for (T t : stack) {
            sb.append(t).append(" ");
        }
        sb.append(">");
        return sb.toString();
    }

    public Iterator<T> iterator() {
        return stack.iterator();
    }
}
