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

import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * This class is used to rescue the parser when it was asked to create an object, and the class does not
 * exist or an error like this
 *
 * @author Fontignie Jacques
 * @version 23 juil. 2005
 */
public class RescueObject implements IParserObject{

    private String className;

    protected RescueObject(String className) {
        this.className = className;
    }

    public MethodDescriptor[] getDescriptors() {
        return new MethodDescriptor[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean flush(String name) {
        return true;
    }

    public boolean call(String name) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return true;
    }

    public boolean setAttribute(String name, String value) throws Exception, IllegalAccessException, InvocationTargetException {
        return true;
    }

    public boolean setAttribute(String name, IParserObject o) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return true;
    }

    public boolean set(IParserObject o) throws IllegalAccessException, InvocationTargetException {
        return true;
    }
}
