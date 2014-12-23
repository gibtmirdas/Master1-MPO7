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


/**
 * This class implements an Object Pool Recycler. It can contains instances
 * of the Poolable interface.
 * User: falcone
 * Date: Aug 25, 2004
 */
public class ObjectPool <T extends Poolable> {

    private BufferedStack<T> freeList;
    private final T PROTOTYPE;

    private static final int INSTANCES_PRODUCTION = 20;
    private static final int BUFFER_SIZE = 500;
    private static final int MAX_VOID_BUFFERS = 2;

    /**
     * Basic constructor.
     *
     * @param _prototype an example of the
     */
    public ObjectPool(T _prototype) {
        freeList = new BufferedStack<T>(BUFFER_SIZE, MAX_VOID_BUFFERS);
        PROTOTYPE = _prototype;
    }

    private void add(T object) {
        freeList.add(object);
    }

    private void createInstances(int size) {
        for (int i = 0; i < size; i++) {
            T object = (T) PROTOTYPE.newInstance();
            add(object);
        }
    }

    public T get() {
        T object;
        if (freeList.isEmpty()) {
            createInstances(INSTANCES_PRODUCTION);
            object = (T) PROTOTYPE.newInstance();
        } else {
            object = freeList.remove();
        }
        //Pull the object. It will just test that the object is in a correct state
        object.pull();
        return object;
    }

    public void release(T t) {
        assert t != null;
        t.deepClean();
        freeList.add(t);
        return;
    }
}
