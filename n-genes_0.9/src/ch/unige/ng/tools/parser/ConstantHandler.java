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


package ch.unige.ng.tools.parser;

import java.util.HashMap;

/**
 * Handle the constants
 *
 * @author Fontignie Jacques
 * @version 8 mai 2005
 */
public class ConstantHandler {

    /**
     * The hashMap containing the constants
     */
    private HashMap<String, String> constants;

    /**
     * The hashMap containing all the constants created at the beginning.
     */
    private HashMap<String, String> externConstants;

    private ConfigHandler handler;


    public ConstantHandler(ConfigHandler handler, String... constantsArray) {
        constants = new HashMap<String, String>();
        externConstants = new HashMap<String, String>();
        this.handler = handler;
        addConstants(constantsArray);
    }

    private void addConstants(String... constantsArray) {
        for (int i = 0; i < constantsArray.length; i++) {
            String current = constantsArray[i];
            int index = current.indexOf("=");
            //Inform that there is an error
            if (index == -1){
                handler.getLogger().severe("The constant is not defined with an equal: " + current);
                return;
            }
            String name = current.substring(0, index);
            String value = current.substring(index + 1);
            constants.put(name, value);
            externConstants.put(name, value);
        }
    }

    public String get(String current) {
        return constants.get(current);
    }

    public void add(String name, String value) {
        if (constants.containsKey(name)) {
            if (!externConstants.containsKey(name))
                handler.severe("You can not redefine a constant, the constant " + name + " was going to be redeclared");
        } else
            constants.put(name, value);
    }

    public ConfigHandler getDefaultHandler() {
        return handler;
    }
}
