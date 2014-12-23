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

import org.xml.sax.SAXParseException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * This class defines an universal parser.
 * <p/>
 * The minimum must be:
 * <p/>
 * TODO Supress Sax and work with DOM 
 * @author Fontignie Jacques
 */
public class XMLParser {

    /**
     * Default file
     */
    private File file;

    /**
     * The string arary containing the constants
     */
    private String[] constants;

    /**
     * The parser needs a file name
     *
     * @param filename
     * @param constants The constants used in the program. They have to be writtent like this: name=value
     */
    public XMLParser(String filename, String... constants) {
        file = new File(filename);
        this.constants = constants;
    }

    /**
     * This method parses the file and run the main access reference (the method of
     * the first object defined in program).
     *
     * @param logger The logger used to print informations
     * @throws Throwable
     */
    public void parse(Logger logger) throws Throwable {
        logger = Logger.getLogger(XMLParser.class.toString());
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        SAXParser parser = factory.newSAXParser();
        ConfigHandler handler = new ConfigHandler(logger, constants);
        try {
            parser.parse(file, handler);
        } catch (SAXParseException e) {
            logger.log(Level.SEVERE, "(" + e.getLineNumber() + ";" + e.getColumnNumber() + "):\tError during the parsing", e);
            return;
        }
        try {
            handler.start();
        } catch (InvocationTargetException e) {
            logger.log(Level.SEVERE,"Error during the program",e.getCause());
        }
    }

}
