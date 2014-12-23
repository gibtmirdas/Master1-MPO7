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


package ch.unige.ng.tools.pool;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a LIFO queue with an ArrayList of buffers. It's particularly suitable
 * for our ObjectPool Method, since th design guarantees good resizing performances.
 * @author Jean-Luc Falcone
 * @version Mar 1, 2005 - 1:47:36 PM
 */
public class BufferedStack<E> {

    private final int BUFFER_SIZE;
    private final int MAX_VOID_BUFFERS;    

    private final List<Object[]> B_LIST =  new ArrayList<Object[]>();
    private int numBuffers;
    private int currentBuffer;
    private int nextPositionInBuffer;

    /**
     * Basic constructor. The user must provide two parameters, the size of the buffers
     * and the number of void buffer tolerated. The first parameter, must be related to
     * the maximum number of objects contained in this structure. As tule of thumb, use
     * the max number of instances divided by 5 or the maximum number of instances
     * (whichever is greater).
     * The second parameter represents the tolerated number of void buffers. It is recommanded
     * to start with a value of two, to avoid frequent destruction/recreation of buffers.
     *
     * @param bufferSize Size of the Buffers
     * @param maxVoidBuffers Number of tolerated void buffers.
     */
    public BufferedStack(int bufferSize, int maxVoidBuffers) {
        BUFFER_SIZE = bufferSize;
        MAX_VOID_BUFFERS = maxVoidBuffers;
        B_LIST.add(new Object[BUFFER_SIZE]);
        currentBuffer = 0;
        numBuffers = 1;
        nextPositionInBuffer = 0;

    }

    /**
     * This method add an element to the LIFO queue.
      * @param element
     */
    public void add(E element){
        B_LIST.get(currentBuffer)[nextPositionInBuffer] = element;
        nextPositionInBuffer++;
        if (nextPositionInBuffer == BUFFER_SIZE) {
            if (currentBuffer == numBuffers-1) {
                B_LIST.add(new Object[BUFFER_SIZE]);
                //System.out.println("Added new Buffer");
                numBuffers++;
            }
            currentBuffer++;
            nextPositionInBuffer = 0;
        }
    }

    /**
     * Remove and return the last added element.
     * @return an element to be returned.
     */
    public E remove() {
        if (nextPositionInBuffer == 0) {
            currentBuffer--;
            nextPositionInBuffer = BUFFER_SIZE;
            trim();
        }
        nextPositionInBuffer--;
        E element = (E) B_LIST.get(currentBuffer)[nextPositionInBuffer];
        B_LIST.get(currentBuffer)[nextPositionInBuffer] = null;
        return element;
    }

    private void trim() {
        int voidsBuffers = numBuffers - (currentBuffer+1);
        if (voidsBuffers >= MAX_VOID_BUFFERS) {
            int buffersToRemove = voidsBuffers - MAX_VOID_BUFFERS;
            for(int i=0; i<buffersToRemove; i++) {
                B_LIST.remove(B_LIST.size()-1);
                numBuffers--;
            }            
        }
    }

    /**
     * Return the current size of the queue.
     * @return  the size
     */
    public int size() {
        return currentBuffer*BUFFER_SIZE + nextPositionInBuffer;
    }

    /**
     * Return true is the queue is empty. This method should be (a litte) more
     * performant than <pre>(size()==0)</pre>.
     * @return if the stack is empty
     */
    public boolean isEmpty() {
        if (currentBuffer == 0 && nextPositionInBuffer == 0) {
            return true;
        }
        return false;
    }

}
