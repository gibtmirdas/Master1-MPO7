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

package ch.unige.ng.tools.parser.objects;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Fontignie Jacques
 * @version 23 juil. 2005
 */
public interface IParserObject {

    /**
     * Flush the object: call the objectCreated method
     *
     * @param name
     * @return if the method success
     * @see ch.unige.ng.tools.Flushable
     */
    public boolean flush(String name);

    /**
     * Calls the method name without arguments: the method used to run the algorithm
     *
     * @param name
     * @return if the method success
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public boolean call(String name) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    /**
     * Sets the attribute name with value value
     *
     * @param name
     * @param value
     * @return if the method success
     * @throws Exception
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public boolean setAttribute(String name, String value) throws Exception, IllegalAccessException, InvocationTargetException;

    /**
     * Sets the attribute name with the value value
     *
     * @param name
     * @param o
     * @return if the method success
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public boolean setAttribute(String name, IParserObject o) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;


    /**
     * Sets the object o to the class
     *
     * @param o
     * @return if the method success
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public boolean set(IParserObject o) throws IllegalAccessException, InvocationTargetException;

    @Override public String toString();
}
