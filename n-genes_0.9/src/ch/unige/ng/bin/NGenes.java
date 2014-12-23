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


package ch.unige.ng.bin;

import ch.unige.ng.tools.parser.XMLParser;

import java.beans.IntrospectionException;
import java.util.logging.Logger;
import java.util.logging.LogManager;

/**
 * n-genes
 * <p/>
 *n-genes will be one day (maybe) the entry reference of the genetic algorithm, the goal is to used it with an option and
 * it will run the graphical interface, otherwise it can be used to just run the parser.
 *
 * @author Jacques Fontignie
 * @version 4 febr. 2005
 */
public class NGenes {
    private static final String FILE_CONSTANT = "--file";


    private static void errorInput() {
        System.err.println("The parameters are wrong: \n" +
                "java ch.unige.ng.bin.NGenes --file filename [constants] \n" +
                "\tThe filename gives the configuration file of the object" +
                "\tA constant is defined like this: name=value");
        System.exit(0);
    }

    /**
     * This function parses the input.
     *
     * @param args
     * @return the name of the file in which find the config
     */
    private static String parseFile(String args[]) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().equals(FILE_CONSTANT)) {
                if (i == args.length - 1) errorInput();
                return args[i + 1];
            }
        }
        errorInput();
        return null;
    }

    private static String[] parseConstants(String[] args) {
        String constants[] = new String[args.length - 2];
        int index = 0;
        for (int i = 0; i < args.length; i++) {
            String lower = args[i].toLowerCase();
            if (!lower.equals(FILE_CONSTANT)) {
                constants[index++] = args[i];
            } else
                i++;
        }
        return constants;
    }

    public static void main(String[] args) throws Throwable, InstantiationException, ClassNotFoundException, IntrospectionException {
        String configFile = parseFile(args);
        LogManager.getLogManager().readConfiguration();
        Logger logger = Logger.getLogger(NGenes.class.toString());
        logger.info("Running 'n-genes' with config file: " + configFile);
        String[] constants = parseConstants(args);
        XMLParser parser = new XMLParser(configFile, constants);
        parser.parse(logger);
    }


}
