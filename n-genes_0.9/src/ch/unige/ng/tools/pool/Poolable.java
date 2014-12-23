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
 * This interface determines an object that can be reused. The goal is to avoid garbage collecting. When an object is no more used.
 * It has to be released. And this one will be erased.
 */
public interface Poolable <T extends Poolable> {

    /**
     * @return a new instance. Do not use it. It is a handle method used by the objetPool. This method should not be called,
     *         only the ObjectPool has to call this method
     */
    T newInstance();

    /**
     * Erases itself. This method is called by the pool to inform it that it will be released. It is a handle method used by the objetPool.
     * This method should not be called, only the ObjectPool has to call this method. Everything that is not constant in an individual has to
     * be deleted or released
     */
    void deepClean();

    /**
     * Inform the object that it is used again. The idea is to prevent some bugs.
     * <p/>
     * When a class is informed that it is pulled, it has to check if it is not allready done. If it is the case,
     * an exception has to be thrown. This method hsa to be called by the pool
     *
     * @throws RuntimeException If the object has allready been pulled
     */
    void pull() throws RuntimeException;

}
