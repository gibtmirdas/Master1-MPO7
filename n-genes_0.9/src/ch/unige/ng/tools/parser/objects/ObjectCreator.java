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

import ch.unige.ng.tools.parser.ConfigHandler;

/**
 * This class handles the creation of object. A class is given and it create a new ParserObject or a RescueObject
 *
 * @author Fontignie Jacques
 * @version 23 juil. 2005
 */
public class ObjectCreator {
    private ConfigHandler handler;

    public ObjectCreator(ConfigHandler handler) {
        this.handler = handler;
    }

    public IParserObject createObject(String className)  {
        ParserObject object = new ParserObject(className, handler);
        return object.isFatal()? (IParserObject) new RescueObject(className) : object;
    }

    /**
     * @param name
     * @return a rescue Object
     */
    public IParserObject createRescueObject(String name) {
        return new RescueObject(name);
    }
}
